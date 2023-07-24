package dev.compactmods.machines.room.spawn;

import com.google.common.graph.MutableValueGraph;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawn;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawns;
import dev.compactmods.machines.graph.GraphTraversalHelper;
import dev.compactmods.machines.graph.edge.GraphEdgeLookupResult;
import dev.compactmods.machines.graph.edge.IGraphEdge;
import dev.compactmods.machines.graph.node.IGraphNode;
import dev.compactmods.machines.room.RoomUtil;
import dev.compactmods.machines.room.graph.CompactRoomProvider;
import dev.compactmods.machines.room.graph.edge.RoomSpawnEdge;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import dev.compactmods.machines.room.graph.node.RoomSpawnNode;
import net.minecraft.Util;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class GraphSpawnManager implements IRoomSpawnManager {

    private final CompactRoomProvider roomProvider;
    private final RoomRegistrationNode roomRegNode;
    private RoomSpawnNode defaultSpawn;
    private final Map<UUID, RoomSpawnNode> playerSpawns;

    public GraphSpawnManager(CompactRoomProvider roomProvider, String roomCode) {
        this.roomProvider = roomProvider;
        this.playerSpawns = new HashMap<>();

        this.roomRegNode = roomProvider.roomNode(roomCode).orElseThrow();

        // [ROOM REG NODE (code, room color, template) ]
        //         | RoomSpawnEdge (player UUID)
        //         v
        //  [RoomSpawnNode]
        final var allRoomSpawns = GraphTraversalHelper.edges(roomProvider.getGraph(), roomRegNode, RoomSpawnEdge.class, RoomSpawnNode.class)
                .collect(Collectors.toSet());

        setOrGenerateDefaultSpawn(roomProvider, allRoomSpawns);
        loadPlayerSpawns(allRoomSpawns);
    }

    private void loadPlayerSpawns(Set<GraphEdgeLookupResult<RoomSpawnEdge, RoomRegistrationNode, RoomSpawnNode>> allRoomSpawns) {
        allRoomSpawns.forEach(res -> {
            final var playerId = res.edgeValue().player();
            final var spawn = res.target();
            if (!playerId.equals(Util.NIL_UUID))
                playerSpawns.putIfAbsent(playerId, spawn);
        });
    }

    private void setOrGenerateDefaultSpawn(CompactRoomProvider roomProvider, Set<GraphEdgeLookupResult<RoomSpawnEdge, RoomRegistrationNode, RoomSpawnNode>> allRoomSpawns) {
        final var graphDefaultSpawn = allRoomSpawns.stream()
                .filter(s -> s.edgeValue().player().equals(Util.NIL_UUID))
                .findFirst();

        graphDefaultSpawn.ifPresentOrElse(ds -> this.defaultSpawn = ds.target(), () -> {
            final var newDefCenter = RoomUtil.calculateRoomDefaultSpawn(roomRegNode);
            this.defaultSpawn = new RoomSpawnNode(newDefCenter, Vec2.ZERO);
            roomProvider.getGraph().putEdgeValue(roomRegNode, defaultSpawn, new RoomSpawnEdge(Util.NIL_UUID));
        });
    }

    @Override
    public void resetPlayerSpawn(UUID player) {
        final var graph = roomProvider.getGraph();

        // If we have a reference
        if (!playerSpawns.containsKey(player)) {
            createPlayerSpawn(player);
        } else {
            final var existing = playerSpawns.get(player);
            existing.position = defaultSpawn.position();
            existing.rotation = defaultSpawn.rotation();
        }
    }

    private RoomSpawnNode createPlayerSpawn(UUID player) {
        final var graph = roomProvider.getGraph();
        final var newSpawnNode = new RoomSpawnNode(defaultSpawn.position(), Vec2.ZERO);
        playerSpawns.put(player, newSpawnNode);
        graph.putEdgeValue(roomRegNode, newSpawnNode, new RoomSpawnEdge(player));
        return newSpawnNode;
    }

    @Override
    public void setDefaultSpawn(Vec3 position, Vec2 rotation) {
        defaultSpawn.position = position;
        defaultSpawn.rotation = rotation;
    }

    @Override
    public IRoomSpawns spawns() {
        final var ds = new RoomSpawn(defaultSpawn.position, defaultSpawn.rotation);
        final var ps = new HashMap<UUID, RoomSpawn>();
        playerSpawns.forEach((id, sn) -> ps.putIfAbsent(id, new RoomSpawn(sn.position, sn.rotation)));
        return new RoomSpawns(ds, ps);
    }

    @Override
    public void setPlayerSpawn(UUID player, Vec3 location, Vec2 rotation) {
        final var playerNode = playerSpawns.computeIfAbsent(player, this::createPlayerSpawn);
        playerNode.position = location;
        playerNode.rotation = rotation;
    }

    private record RoomSpawns(RoomSpawn defaultSpawn, Map<UUID, RoomSpawn> playerSpawnsSnapshot) implements IRoomSpawns {

        @Override
        public Optional<IRoomSpawn> forPlayer(UUID player) {
            return Optional.ofNullable(playerSpawnsSnapshot.get(player));
        }
    }
}
