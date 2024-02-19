package dev.compactmods.machines.player;

import com.google.common.base.Predicate;
import dev.compactmods.feather.MemoryGraph;
import dev.compactmods.feather.edge.GraphEdge;
import dev.compactmods.feather.edge.impl.EmptyEdge;
import dev.compactmods.feather.node.Node;
import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.api.codec.NbtListCollector;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.api.dimension.MissingDimensionException;
import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.api.room.history.RoomEntryPoint;
import dev.compactmods.machines.room.graph.node.RoomReferenceNode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PlayerEntryPointHistory extends SavedData {

    public static final String DATA_NAME = Constants.MOD_ID + "_player_history";


    private final MemoryGraph graph;
    private final int maxDepth;

    private final HashMap<String, RoomReferenceNode> roomNodes;
    private final HashMap<UUID, PlayerReferenceNode> playerNodes;
    private final HashMap<UUID, PlayerEntryPointNode> latestEntryPoints;
    private final java.util.function.Predicate<String> roomValidator;

    public PlayerEntryPointHistory(int maxDepth, java.util.function.Predicate<String> roomValidator) {
        this.roomValidator = roomValidator;
        this.graph = new MemoryGraph();
        this.maxDepth = maxDepth;
        this.roomNodes = new HashMap<>();
        this.playerNodes = new HashMap<>();
        this.latestEntryPoints = new HashMap<>();
    }

    public static PlayerEntryPointHistory forServer(MinecraftServer server, int maxDepth) throws MissingDimensionException {
        return CompactDimension.forServer(server)
                .getDataStorage()
                .computeIfAbsent(factory(maxDepth), DATA_NAME);
    }

    private static SavedData.Factory<PlayerEntryPointHistory> factory(int maxDepth) {
        Predicate<String> isRegistered = RoomApi.registrar()::isRegistered;
        return new SavedData.Factory<>(() -> new PlayerEntryPointHistory(maxDepth, isRegistered), Serializer::load, null);
    }

    private Optional<PlayerEntryPointNode> getPreviousEntryPoint(PlayerEntryPointNode node) {
        return graph.inboundEdges(node, PlayerEntryPointNode.class)
                .findFirst()
                .map(e -> e.source().get());
    }

    public Deque<RoomEntryPoint> history(Player player, int depth) {
        final var playerNode = playerNodes.get(player.getUUID());
        if(playerNode == null)
            return new ArrayDeque<>(0);

        final var latest = latestEntryPoints.get(player.getUUID());
        if(depth <= 1)
            return new ArrayDeque<>(Collections.singleton(latest.data()));

        Deque<RoomEntryPoint> entries = new ArrayDeque<>(depth);

        PlayerEntryPointNode needle = latest;
        for(int d = 0; d < depth; d++) {
            var prev = getPreviousEntryPoint(needle);
            if(prev.isEmpty())
                return entries;

            needle = prev.get();
            entries.push(needle.data());
        }

        return entries;
    }

    public RoomEntryResult enterRoom(Player player, String roomCode, RoomEntryPoint entryPoint) {
        if (!roomValidator.test(roomCode))
            return RoomEntryResult.FAILED_ROOM_INVALID;

        PlayerReferenceNode playerNode = getOrCreatePlayer(player);

        long depth = graph.outboundEdges(playerNode, PlayerEntryPointNode.class).count();
        if (depth >= maxDepth)
            return RoomEntryResult.FAILED_TOO_FAR_DOWN;

        RoomReferenceNode roomNode = getOrCreateRoom(roomCode);

        PlayerEntryPointNode entryNode = new PlayerEntryPointNode(UUID.randomUUID(), entryPoint);
        if(latestEntryPoints.containsKey(player.getUUID())) {
            final var prev = latestEntryPoints.replace(player.getUUID(), entryNode);
            if(prev != null)
                graph.connectNodes(prev, entryNode, new EmptyEdge<>(prev, entryNode));
        } else {
            latestEntryPoints.put(player.getUUID(), entryNode);
        }

        // PlayerReferenceNode ---> RoomEntryPointNode
        // RoomEntryPointNode -[PlayerRoomEntryEdge]-> RoomReferenceNode
        graph.connectNodes(playerNode, entryNode, new EmptyEdge<>(playerNode, entryNode));
        graph.connectNodes(entryNode, roomNode, new PlayerRoomEntryEdge(entryNode, roomNode));

        this.setDirty();
        return RoomEntryResult.SUCCESS;
    }

    @NotNull
    private RoomReferenceNode getOrCreateRoom(String roomCode) {
        return roomNodes.computeIfAbsent(roomCode, (code) -> {
            var node = new RoomReferenceNode(roomCode);
            graph.addNode(node);

            this.setDirty();
            return node;
        });
    }

    @NotNull
    private PlayerReferenceNode getOrCreatePlayer(Player player) {
        return playerNodes.computeIfAbsent(player.getUUID(), (o) -> {
            var node = new PlayerReferenceNode(UUID.randomUUID(), player.getUUID());
            graph.addNode(node);

            this.setDirty();
            return node;
        });
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag compoundTag) {
        return Serializer.save(this, compoundTag);
    }

    public void clearHistory(ServerPlayer player) {
        if(!playerNodes.containsKey(player.getUUID()))
            return;

        final var playerNode = playerNodes.get(player.getUUID());
        final var list = graph.successors(playerNode)
                .filter(PlayerEntryPointNode.class::isInstance)
                .toList();

        for (Node<?> node : list) {
            graph.removeNode(node);
        }

        latestEntryPoints.remove(player.getUUID());
        setDirty();
    }



    private static class Serializer {

        private static final Logger LOGS = LogManager.getLogger();
        public static final String NBT_MAX_DEPTH = "max_depth";

        @NotNull
        public static CompoundTag save(@NotNull PlayerEntryPointHistory instance, @NotNull CompoundTag compoundTag) {
            compoundTag.putInt(NBT_MAX_DEPTH, instance.maxDepth);

            final var playerData = instance.playerNodes.values()
                    .stream()
                    .map((PlayerReferenceNode player) -> writePlayerEntry(instance, player))
                    .collect(NbtListCollector.toNbtList());

            compoundTag.put("player_history", playerData);
            return compoundTag;
        }

        private static CompoundTag writePlayerEntry(PlayerEntryPointHistory history, PlayerReferenceNode playerReferenceNode) {

            // PlayerReferenceNode ---> RoomEntryPointNode
            // RoomEntryPointNode -[PlayerRoomEntryEdge]-> RoomReferenceNode

            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("player_id", playerReferenceNode.playerID());

            final var latestEntry = history.latestEntryPoints.get(playerReferenceNode.playerID());

            // early exit if no history
            if (latestEntry == null) return playerTag;

            final var historyList = writePlayerHistory(history, playerReferenceNode, playerTag);
            writeHistoryToRooms(history, historyList, playerTag);

            return playerTag;
        }

        private static List<PlayerEntryPointNode> writePlayerHistory(PlayerEntryPointHistory history, PlayerReferenceNode playerReferenceNode, CompoundTag playerTag) {
            final var historyList = history.graph.outboundEdges(playerReferenceNode, PlayerEntryPointNode.class)
                    .map(GraphEdge::target)
                    .map(WeakReference::get)
                    .filter(Objects::nonNull)
                    .toList();

            final var nbtHistory = PlayerEntryPointNode.CODEC.listOf()
                    .encodeStart(NbtOps.INSTANCE, historyList)
                    .getOrThrow(false, LOGS::error);

            playerTag.put("history", nbtHistory);

            return historyList;
        }

        private static void writeHistoryToRooms(PlayerEntryPointHistory history, List<PlayerEntryPointNode> entryPoints, CompoundTag playerTag) {
            final var entrypoints = entryPoints.stream()
                    .map(epn -> history.graph.outboundEdges(epn, RoomReferenceNode.class, PlayerRoomEntryEdge.class).findFirst())
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            ListTag list = new ListTag();
            entrypoints.forEach(edge -> {
                CompoundTag entrypointTag = new CompoundTag();
                entrypointTag.putUUID("entry", edge.source().get().id());
                entrypointTag.putUUID("room", edge.target().get().id());
                entrypointTag.putString("timestamp", DateTimeFormatter.ISO_DATE_TIME.format(edge.entryTime()));
                list.add(entrypointTag);
            });

            playerTag.put("history_rooms", list);
        }

        @NotNull
        public static PlayerEntryPointHistory load(CompoundTag tag) {
            final int maxDepth = tag.getInt(NBT_MAX_DEPTH);
            final var inst = new PlayerEntryPointHistory(maxDepth, RoomApi.registrar()::isRegistered);

            return inst;
        }
    }
}

