package dev.compactmods.machines.room.graph;

import dev.compactmods.machines.graph.GraphAdjacentNodeStream;
import dev.compactmods.machines.graph.GraphEdgeStream;
import dev.compactmods.machines.graph.GraphNodeStream;
import dev.compactmods.machines.graph.GraphTraversalHelper;
import dev.compactmods.machines.machine.graph.node.CompactMachineNode;
import dev.compactmods.machines.room.graph.edge.RoomSpawnEdge;
import dev.compactmods.machines.room.graph.node.RoomChunkNode;
import dev.compactmods.machines.room.graph.node.RoomReferenceNode;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import dev.compactmods.machines.room.graph.node.RoomSpawnNode;
import net.minecraft.world.level.ChunkPos;

import java.util.Optional;

public class GraphFunctions {

    public static final GraphNodeStream<RoomRegistrationNode> ALL_REGISTRATIONS = (graph) -> GraphTraversalHelper.nodes(graph, RoomRegistrationNode.class);

    public static final GraphAdjacentNodeStream<CompactMachineNode, RoomReferenceNode> MACHINE_TO_ROOM_REF =
            (graph, machineNode) -> GraphTraversalHelper.successors(graph, machineNode, RoomReferenceNode.class);

    public static final GraphNodeScalarFunction<CompactMachineNode, Optional<String>> MACHINE_TO_ROOM_CODE =
            (graph, machineNode) -> GraphTraversalHelper.successors(graph, machineNode, RoomReferenceNode.class).map(RoomReferenceNode::code).findFirst();

    public static final GraphAdjacentNodeStream<RoomRegistrationNode, RoomChunkNode> LOOKUP_CHUNK_NODES =
            (graph, regNode) -> GraphTraversalHelper.successors(graph, regNode, RoomChunkNode.class);

    public static final GraphNodeStreamFunction<RoomRegistrationNode, ChunkPos> ROOM_CHUNKS =
            (graph, regNode) -> LOOKUP_CHUNK_NODES.nodes(graph, regNode).map(RoomChunkNode::chunk);

    public static final GraphAdjacentNodeStream<RoomChunkNode, RoomRegistrationNode> LOOKUP_ROOM_REGISTRATION =
            (g, on) -> GraphTraversalHelper.predecessors(g, on, RoomRegistrationNode.class);
    public static final GraphEdgeStream<RoomRegistrationNode, RoomSpawnEdge, RoomSpawnNode> SPAWNS = (graph, regNode) ->
            GraphTraversalHelper.edges(graph, regNode, RoomSpawnEdge.class, RoomSpawnNode.class);

}
