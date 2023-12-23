package dev.compactmods.machines.room.spawn;

import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawn;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawns;
import dev.compactmods.feather.MemoryGraph;
import dev.compactmods.machines.room.graph.GraphNodes;
import dev.compactmods.machines.room.graph.edge.RoomSpawnEdge;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import dev.compactmods.machines.room.graph.node.RoomSpawnNode;
import net.minecraft.Util;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class GraphSpawnManager implements IRoomSpawnManager {

    private final MemoryGraph roomProvider;
    private final RoomRegistrationNode roomRegNode;
    private RoomSpawnNode defaultSpawn;
    private final Map<UUID, RoomSpawnNode> playerSpawns;

    public GraphSpawnManager(MemoryGraph roomGraph, String roomCode) {
        this.roomProvider = roomGraph;
        this.playerSpawns = new HashMap<>();

        this.roomRegNode = roomGraph.nodes(RoomRegistrationNode.class)
                .filter(node -> node.code().equals(roomCode))
                .findFirst()
                .orElseThrow();

        // [ROOM REG NODE (code, room color, template) ]
        //         | RoomSpawnEdge (player UUID)
        //         v
        //  [RoomSpawnNode]
        final var allRoomSpawns = roomGraph.outboundEdges(GraphNodes.SPAWNS, roomRegNode)
                .filter(RoomSpawnEdge.class::isInstance)
                .map(RoomSpawnEdge.class::cast)
                .collect(Collectors.toSet());

        setOrGenerateDefaultSpawn(roomGraph, allRoomSpawns);
        loadPlayerSpawns(allRoomSpawns);
    }

    private void loadPlayerSpawns(Set<RoomSpawnEdge> allRoomSpawns) {
        allRoomSpawns.forEach(res -> {
            final var playerId = res.value().player();
            final var spawn = res.target();
            if (!playerId.equals(Util.NIL_UUID))
                playerSpawns.putIfAbsent(playerId, spawn.get());
        });
    }

    private void setOrGenerateDefaultSpawn(MemoryGraph roomGraph, Set<RoomSpawnEdge> allRoomSpawns) {
        final var graphDefaultSpawn = allRoomSpawns.stream()
                .filter(s -> s.value().player().equals(Util.NIL_UUID))
                .findFirst();

        graphDefaultSpawn.ifPresentOrElse(ds -> this.defaultSpawn = ds.target().get(), () -> {
            this.defaultSpawn = new RoomSpawnNode(UUID.randomUUID(), new RoomSpawn(roomRegNode.defaultSpawn(), Vec2.ZERO));

            final var newSpawnEdge = new RoomSpawnEdge(new WeakReference<>(roomRegNode), new WeakReference<>(defaultSpawn), new RoomSpawnEdge.EdgeData(Util.NIL_UUID));
            roomGraph.connectNodes(roomRegNode, defaultSpawn, newSpawnEdge);
        });
    }

    @Override
    public void resetPlayerSpawn(UUID player) {
        // If we have a reference
        if (!playerSpawns.containsKey(player)) {
            createPlayerSpawn(player);
        } else {
            final var existing = playerSpawns.get(player);
            existing.setData(defaultSpawn.data());
        }
    }

    private RoomSpawnNode createPlayerSpawn(UUID player) {
        final var newSpawnNode = new RoomSpawnNode(UUID.randomUUID(), defaultSpawn.data());
        playerSpawns.put(player, newSpawnNode);
        roomProvider.connectNodes(roomRegNode, newSpawnNode, new RoomSpawnEdge(new WeakReference<>(roomRegNode),
                new WeakReference<>(newSpawnNode),
                new RoomSpawnEdge.EdgeData(player)));

        return newSpawnNode;
    }

    @Override
    public void setDefaultSpawn(Vec3 position, Vec2 rotation) {
        defaultSpawn.setData(new RoomSpawn(position, rotation));
    }

    @Override
    public IRoomSpawns spawns() {
        final var ds = new RoomSpawn(defaultSpawn.data());
        final var ps = new HashMap<UUID, RoomSpawn>();
        playerSpawns.forEach((id, sn) -> ps.putIfAbsent(id, sn.data()));
        return new RoomSpawns(ds, ps);
    }

    @Override
    public void setPlayerSpawn(UUID player, Vec3 location, Vec2 rotation) {
        final var playerNode = playerSpawns.computeIfAbsent(player, this::createPlayerSpawn);
        playerNode.setData(new RoomSpawn(location, rotation));
    }

    private record RoomSpawns(RoomSpawn defaultSpawn, Map<UUID, RoomSpawn> playerSpawnsSnapshot) implements IRoomSpawns {

        @Override
        public Optional<IRoomSpawn> forPlayer(UUID player) {
            return Optional.ofNullable(playerSpawnsSnapshot.get(player));
        }
    }
}
