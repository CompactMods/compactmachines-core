package dev.compactmods.machines.room.graph;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.api.dimension.MissingDimensionException;
import dev.compactmods.machines.api.room.IRoomLookup;
import dev.compactmods.machines.api.room.IRoomOwnerLookup;
import dev.compactmods.machines.api.room.registration.IMutableRoomRegistration;
import dev.compactmods.machines.api.room.registration.IRoomRegistration;
import dev.compactmods.machines.api.room.registration.IRoomSpawnLookup;
import dev.compactmods.machines.codec.NbtListCollector;
import dev.compactmods.machines.graph.IGraphEdge;
import dev.compactmods.machines.graph.IGraphNode;
import dev.compactmods.machines.room.MutableRoomRegistration;
import dev.compactmods.machines.room.RoomCodeGenerator;
import dev.compactmods.machines.room.exceptions.NonexistentRoomException;
import dev.compactmods.machines.room.graph.edge.RoomChunkEdge;
import dev.compactmods.machines.room.graph.edge.RoomOwnerEdge;
import dev.compactmods.machines.room.graph.node.RoomChunkNode;
import dev.compactmods.machines.room.graph.node.RoomMetadataNode;
import dev.compactmods.machines.room.graph.node.RoomOwnerNode;
import dev.compactmods.machines.room.graph.node.RoomSpawnNode;
import dev.compactmods.machines.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class CompactRoomProvider extends SavedData implements IRoomLookup, IRoomOwnerLookup, IRoomSpawnLookup {

    public static final Logger LOGS = LogManager.getLogger();

    public static final String DATA_NAME = Constants.MOD_ID + "_rooms";
    public static final String NBT_NODE_ID_KEY = "node_id";

    private final Map<String, RoomMetadataNode> metadata;

    private final Map<String, RoomSpawnNode> roomSpawns;

    private final Map<ChunkPos, RoomChunkNode> chunks;
    private final Map<UUID, RoomOwnerNode> owners;

    private final MutableValueGraph<IGraphNode<?>, IGraphEdge> graph;

    private CompactRoomProvider() {
        this.metadata = new HashMap<>();
        this.roomSpawns = new HashMap<>();
        this.chunks = new HashMap<>();
        this.owners = new HashMap<>();
        this.graph = ValueGraphBuilder
                .directed()
                .build();
    }

    public static CompactRoomProvider empty() {
        return new CompactRoomProvider();
    }

    @Nullable
    public static CompactRoomProvider instance(MinecraftServer server) {
        try {
            final ServerLevel level = CompactDimension.forServer(server);
            return level.getDataStorage()
                    .computeIfAbsent(CompactRoomProvider::fromDisk, CompactRoomProvider::empty, DATA_NAME);
        } catch (MissingDimensionException e) {
            LOGS.fatal(e);
            return null;
        }
    }

    public static CompactRoomProvider instance(ServerLevel compactDim) {
        return compactDim.getDataStorage()
                .computeIfAbsent(CompactRoomProvider::fromDisk, CompactRoomProvider::empty, DATA_NAME);
    }

    public static CompactRoomProvider fromDisk(CompoundTag compoundTag) {
        final var graph = new CompactRoomProvider();

        final HashMap<UUID, RoomMetadataNode> metaNodeIdMap = new HashMap<>();
        if (compoundTag.contains("rooms")) {
            compoundTag.getList("rooms", ListTag.TAG_COMPOUND)
                    .stream()
                    .map(CompoundTag.class::cast)
                    .forEach(roomNode -> {
                        UUID id = roomNode.getUUID(NBT_NODE_ID_KEY);
                        final var node = RoomMetadataNode.CODEC.parse(NbtOps.INSTANCE, roomNode)
                                .getOrThrow(false, LOGS::fatal);

                        metaNodeIdMap.put(id, node);
                        graph.metadata.put(node.code(), node);

                        node.chunks().forEach(chunk -> {
                            RoomChunkNode chunkNode = new RoomChunkNode(chunk);
                            graph.graph.putEdgeValue(node, chunkNode, new RoomChunkEdge());
                            graph.chunks.put(chunk, chunkNode);
                        });
                    });
        }

        final HashMap<UUID, RoomOwnerNode> roomOwnerNodeMap = new HashMap<>();
        if (compoundTag.contains("owners")) {
            compoundTag.getList("owners", ListTag.TAG_COMPOUND)
                    .stream()
                    .map(CompoundTag.class::cast)
                    .forEach(ownerNode -> {
                        UUID id = ownerNode.getUUID(NBT_NODE_ID_KEY);
                        final var node = RoomOwnerNode.CODEC.parse(NbtOps.INSTANCE, ownerNode)
                                .getOrThrow(false, LOGS::fatal);

                        roomOwnerNodeMap.put(id, node);
                        graph.owners.put(node.owner(), node);
                    });
        }

        if (compoundTag.contains("roomOwners")) {
            compoundTag.getList("roomOwners", ListTag.TAG_COMPOUND)
                    .stream()
                    .map(CompoundTag.class::cast)
                    .forEach(roomOwnerConn -> {
                        RoomMetadataNode meta = metaNodeIdMap.get(roomOwnerConn.getUUID("room"));
                        RoomOwnerNode owner = roomOwnerNodeMap.get(roomOwnerConn.getUUID("owner"));
                        graph.graph.putEdgeValue(meta, owner, new RoomOwnerEdge());
                    });
        }

        LOGS.debug("Number of rooms loaded from disk: {}", metaNodeIdMap.size());
        return graph;
    }

    @NotNull
    @Override
    public CompoundTag save(@NotNull CompoundTag tag) {
        //region Room Metadata Nodes
        final HashMap<String, UUID> metaNodeIdMap = new HashMap<>();
        metadata.values().forEach(metaNode -> metaNodeIdMap.put(metaNode.code(), UUID.randomUUID()));
        ListTag meta = (ListTag) RoomMetadataNode.CODEC.listOf()
                .encodeStart(NbtOps.INSTANCE, List.copyOf(metadata.values()))
                .getOrThrow(false, LOGS::fatal);

        meta.stream()
                .filter(CompoundTag.class::isInstance)
                .map(CompoundTag.class::cast)
                .forEach(mct -> mct.putUUID(NBT_NODE_ID_KEY, metaNodeIdMap.get(mct.getString("code"))));

        tag.put("rooms", meta);
        //endregion

        //region Room Owner nodes
        final HashMap<UUID, UUID> ownerByUuidMap = new HashMap<>();
        owners.values().forEach(ownerNode -> ownerByUuidMap.put(ownerNode.owner(), UUID.randomUUID()));
        ListTag ownerList = (ListTag) RoomOwnerNode.CODEC.listOf()
                .encodeStart(NbtOps.INSTANCE, List.copyOf(owners.values()))
                .getOrThrow(false, LOGS::fatal);

        ownerList.stream().map(CompoundTag.class::cast)
                .forEach(oct -> oct.putUUID(NBT_NODE_ID_KEY, ownerByUuidMap.get(oct.getUUID("owner"))));

        tag.put("owners", ownerList);
        //endregion

        //region Room-Owner connections
        if (!metadata.isEmpty() && !owners.isEmpty()) {
            final ListTag roomOwnerConnections = metadata.values()
                    .stream()
                    .map(roomNode -> graph.adjacentNodes(roomNode)
                            .stream()
                            .filter(RoomOwnerNode.class::isInstance)
                            .map(RoomOwnerNode.class::cast)
                            .findFirst()
                            .map(roomOwner -> {
                                UUID roomId = metaNodeIdMap.get(roomNode.code());
                                UUID ownerId = ownerByUuidMap.get(roomOwner.owner());
                                CompoundTag connection = new CompoundTag();
                                connection.putUUID("room", roomId);
                                connection.putUUID("owner", ownerId);
                                return connection;
                            }))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(NbtListCollector.toNbtList());

            tag.put("roomOwners", roomOwnerConnections);
        }
        //endregion

        return tag;
    }

    @Override
    public Stream<IRoomRegistration> allRooms() {
        return metadata.values().stream()
                .map(mn -> mn);
    }

    @Override
    public Stream<IRoomRegistration> findByOwner(UUID owner) {
        if (!owners.containsKey(owner))
            return Stream.empty();

        return graph.adjacentNodes(owners.get(owner)).stream()
                .filter(RoomMetadataNode.class::isInstance)
                .map(RoomMetadataNode.class::cast);
    }

    @Override
    public Optional<IRoomRegistration> forRoom(String room) {
        return Optional.ofNullable(metadata.get(room));
    }

    @Override
    public Optional<IRoomRegistration> findByChunk(ChunkPos chunk) {
        if (!isRoomChunk(chunk)) return Optional.empty();
        final var chunkNode = chunks.get(chunk);

        return graph.predecessors(chunkNode).stream()
                .filter(RoomMetadataNode.class::isInstance)
                .map(RoomMetadataNode.class::cast)
                .map(IRoomRegistration.class::cast)
                .findFirst();
    }

    @Override
    public boolean isRoomChunk(ChunkPos chunk) {
        return chunks.containsKey(chunk);
    }

    @Override
    public long count() {
        return metadata.size();
    }

    private IRoomRegistration finalizeNew(String code, RoomMetadataNode roomNode, UUID owner) {
        this.metadata.put(code, roomNode);
        this.owners.computeIfAbsent(owner, RoomOwnerNode::new);
        final var ownerNode = owners.get(owner);

        graph.putEdgeValue(roomNode, ownerNode, new RoomOwnerEdge());

        // calculate chunks
        roomNode.chunks().forEach(c -> {
            final var roomChunkNode = new RoomChunkNode(c);
            chunks.put(c, roomChunkNode);
            graph.putEdgeValue(roomNode, roomChunkNode, new RoomChunkEdge());
        });

        setDirty();

        return roomNode;
    }

    /**
     * Registers a new room with a specified room code and data from the builder.
     * This assumes that data is coming from a previous source such as a migrator, and
     * will take all values from the returned builder.
     *
     * @param code
     * @param newRoom
     * @return
     */
    public IRoomRegistration registerNew(String code, Function<NewRoomBuilder, NewRoomBuilder> newRoom) {
        final var builder = newRoom.apply(new NewRoomBuilder(code));

        final var roomNode = builder.build();
        return finalizeNew(code, roomNode, builder.owner);
    }

    public IRoomRegistration registerNew(Function<NewRoomBuilder, NewRoomBuilder> newRoom) {
        final var newRoomCode = RoomCodeGenerator.generateRoomId();

        Vec3i location = MathUtil.getRegionPositionByIndex(metadata.size());

        final var builder = newRoom.apply(new NewRoomBuilder(newRoomCode));
        BlockPos newCenter = MathUtil.getCenterWithY(location, 40).above(builder.yOffset());

        builder.setCenter(newCenter);

        final var roomNode = builder.build();
        return finalizeNew(newRoomCode, roomNode, builder.owner);
    }

    public IMutableRoomRegistration edit(String room) {
        return new MutableRoomRegistration(this, this.metadata.get(room));
    }

    public void resetSpawn(String room) throws NonexistentRoomException {
        if (!metadata.containsKey(room))
            throw new NonexistentRoomException(room);

        final var meta = metadata.get(room);
        final var newSpawn = meta.center().subtract(0, (meta.dimensions().getY() / 2f), 0);
        final var newSpawnNode = new RoomSpawnNode(newSpawn, Vec2.ZERO);

        // TODO - Graph data
        if (!roomSpawns.containsKey(room)) {
            // make new spawn data
            roomSpawns.put(room, newSpawnNode);
        } else {
            // reset spawn data
            roomSpawns.put(room, newSpawnNode);
        }
    }

    @Override
    public Optional<UUID> getRoomOwner(String roomCode) {
        // TODO - Graph data
        if (!metadata.containsKey(roomCode))
            return Optional.empty();

        final var roomNode = metadata.get(roomCode);
        return graph.adjacentNodes(roomNode).stream()
                .filter(RoomOwnerNode.class::isInstance)
                .map(RoomOwnerNode.class::cast)
                .map(RoomOwnerNode::owner)
                .findFirst();
    }
}
