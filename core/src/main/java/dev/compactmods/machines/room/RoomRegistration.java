package dev.compactmods.machines.room;

import dev.compactmods.compactmachines.api.room.IRoomRegistrar;
import dev.compactmods.compactmachines.api.room.RoomApi;
import dev.compactmods.compactmachines.api.room.RoomInstance;
import dev.compactmods.compactmachines.api.room.registration.IRoomBuilder;
import dev.compactmods.feather.MemoryGraph;
import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.codec.NbtListCollector;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class RoomRegistration extends SavedData implements IRoomRegistrar {

    public static final Logger LOGS = LogManager.getLogger();

    public static final String DATA_NAME = Constants.MOD_ID + "_rooms";
    public static final String NBT_NODE_ID_KEY = "node_id";

    private final MemoryGraph graph;
    private final Map<String, RoomRegistrationNode> registrationNodes;

    public RoomRegistration() {
        this.graph = new MemoryGraph();
        this.registrationNodes = new HashMap<>();
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag tag) {
        return Serializer.serialize(this, tag);

        //region Room Owner nodes
//        final HashMap<UUID, UUID> ownerByUuidMap = new HashMap<>();
//        owners.values().forEach(ownerNode -> ownerByUuidMap.put(ownerNode.owner(), ownerNode.id()));
//        ListTag ownerList = (ListTag) RoomOwnerNode.CODEC.listOf()
//                .encodeStart(NbtOps.INSTANCE, List.copyOf(owners.values()))
//                .getOrThrow(false, LOGS::fatal);
//
//        ownerList.stream().map(CompoundTag.class::cast)
//                .forEach(oct -> oct.putUUID(NBT_NODE_ID_KEY, ownerByUuidMap.get(oct.getUUID("owner"))));
//
//        tag.put("owners", ownerList);
        //endregion

        //region Room-Owner connections
//        if (!registrationNodes.isEmpty() && !owners.isEmpty()) {
//            final ListTag roomOwnerConnections = registrationNodes.values()
//                    .stream()
//                    .map(roomNode -> graph.adjacentNodes(roomNode)
//                            .stream()
//                            .filter(RoomOwnerNode.class::isInstance)
//                            .map(RoomOwnerNode.class::cast)
//                            .findFirst()
//                            .map(roomOwner -> {
//                                UUID roomId = metaNodeIdMap.get(roomNode.code());
//                                UUID ownerId = ownerByUuidMap.get(roomOwner.owner());
//                                CompoundTag connection = new CompoundTag();
//                                connection.putUUID("room", roomId);
//                                connection.putUUID("owner", ownerId);
//                                return connection;
//                            }))
//                    .filter(Optional::isPresent)
//                    .map(Optional::get)
//                    .collect(NbtListCollector.toNbtList());
//
//            tag.put("roomOwners", roomOwnerConnections);
//        }
        //endregion
    }

    @Override
    public RoomInstance createNew(Consumer<IRoomBuilder> build) {
        final var builder = new NewRoomBuilder();
        build.accept(builder);
        final var data = builder.build();

        var node = new RoomRegistrationNode(UUID.randomUUID(), data);
        this.registrationNodes.put(data.code(), node);
        this.graph.addNode(node);
        return makeRoomInstance(node);
    }

    @Override
    public boolean isRegistered(String room) {
        return registrationNodes.containsKey(room);
    }

    @Override
    public Optional<RoomInstance> get(String room) {
        final var regNode = registrationNodes.get(room);
        if (regNode == null)
            return Optional.empty();

        RoomInstance inst = makeRoomInstance(regNode);
        return Optional.of(inst);
    }

    @NotNull
    private static RoomInstance makeRoomInstance(RoomRegistrationNode regNode) {
        return new RoomInstance(regNode.code(), regNode.defaultMachineColor(),
                regNode,
                () -> RoomApi.spawnManager(regNode.code()),
                () -> RoomApi.chunks(regNode.code())
        );
    }

    @Override
    public long count() {
        return registrationNodes.size();
    }

    @Override
    public Stream<String> allRoomCodes() {
        return registrationNodes.keySet().stream();
    }

    @Override
    public Stream<RoomInstance> allRooms() {
        return registrationNodes.values()
                .stream()
                .map(RoomRegistration::makeRoomInstance);
    }

    public static class Serializer {

        public static CompoundTag serialize(RoomRegistration instance, CompoundTag tag) {

            final var roomData = instance.registrationNodes.values()
                    .stream()
                    .map(regNode -> {
                        CompoundTag ct = (CompoundTag) RoomRegistrationNode.Data.CODEC
                                .encodeStart(NbtOps.INSTANCE, regNode.data())
                                .getOrThrow(false, LOGS::error);

                        ct.putUUID(NBT_NODE_ID_KEY, regNode.id());
                        return ct;
                    }).collect(NbtListCollector.toNbtList());

            tag.put("rooms", roomData);

            return tag;
        }
    }
}
