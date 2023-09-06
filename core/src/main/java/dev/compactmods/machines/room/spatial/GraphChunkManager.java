package dev.compactmods.machines.room.spatial;

import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import dev.compactmods.compactmachines.api.room.registration.IRoomRegistration;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunks;
import dev.compactmods.machines.room.graph.MemoryGraph;
import dev.compactmods.machines.room.graph.GraphFunctions;
import dev.compactmods.machines.room.graph.node.RoomChunkNode;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import net.minecraft.world.level.ChunkPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GraphChunkManager implements IRoomChunkManager {

    private final MemoryGraph graph;
    private final Map<ChunkPos, RoomChunkNode> chunks;
    private final Map<String, RoomRegistrationNode> registrationNodes;

    public GraphChunkManager(MemoryGraph graph) {
        this.graph = graph;
        this.chunks = new HashMap<>();
        this.registrationNodes = new HashMap<>();
    }

    @Override
    public Optional<IRoomRegistration> findRoomByChunk(ChunkPos chunk) {
        if (!chunks.containsKey(chunk)) return Optional.empty();
        final var chunkNode = chunks.get(chunk);

        return graph.adjacentNodes(GraphFunctions.LOOKUP_ROOM_REGISTRATION, chunkNode)
                .map(IRoomRegistration.class::cast)
                .findFirst();
    }

    @Override
    public IRoomChunks get(String room) throws NonexistentRoomException {
        final var regNode = registrationNodes.get(room);
        if(regNode == null)
            throw new NonexistentRoomException(room);

        final var chunks = graph.stream(GraphFunctions.ROOM_CHUNKS, regNode)
                .collect(Collectors.toSet());

        return new RoomChunks(chunks);
    }
}
