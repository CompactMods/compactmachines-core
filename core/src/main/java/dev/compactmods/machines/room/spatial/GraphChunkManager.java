package dev.compactmods.machines.room.spatial;

import dev.compactmods.compactmachines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunks;
import dev.compactmods.feather.MemoryGraph;
import dev.compactmods.feather.edge.GraphEdge;
import dev.compactmods.machines.room.graph.GraphNodes;
import dev.compactmods.machines.room.graph.node.RoomChunkNode;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import net.minecraft.world.level.ChunkPos;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class GraphChunkManager implements IRoomChunkManager {

    private final MemoryGraph graph;
    private final Map<ChunkPos, RoomChunkNode> chunks;

    public GraphChunkManager(MemoryGraph graph) {
        this.graph = graph;
        this.chunks = new HashMap<>();
    }

    @Override
    public Optional<String> findRoomByChunk(ChunkPos chunk) {
        if (!chunks.containsKey(chunk)) return Optional.empty();
        final var chunkNode = chunks.get(chunk);

        return graph.adjNodeStream(GraphNodes.LOOKUP_ROOM_REGISTRATION, chunkNode)
                .map(RoomRegistrationNode::code)
                .findFirst();
    }

    @Override
    public IRoomChunks get(String room) {
        final var regNode = graph.nodes(RoomRegistrationNode.class)
                .filter(rn -> rn.code().equals(room))
                .findFirst();

        return regNode.map(node -> {
            final var chunks = graph.outboundEdges(GraphNodes.ROOM_CHUNKS, node)
                    .map(GraphEdge::target)
                    .map(WeakReference::get)
                    .filter(Objects::nonNull)
                    .peek(chunkNode -> this.chunks.putIfAbsent(chunkNode.data().chunk(), chunkNode))
                    .map(c -> c.data().chunk())
                    .collect(Collectors.toSet());

            return new RoomChunks(chunks);
        }).orElseThrow();
    }
}
