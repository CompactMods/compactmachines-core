package dev.compactmods.machines.room.graph;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import dev.compactmods.machines.graph.GraphAdjacentNodeStream;
import dev.compactmods.machines.graph.GraphEdgeStream;
import dev.compactmods.machines.graph.GraphNodeStream;
import dev.compactmods.machines.graph.edge.GraphEdgeLookupResult;
import dev.compactmods.machines.graph.edge.IGraphEdge;
import dev.compactmods.machines.graph.node.IGraphNode;
import dev.compactmods.machines.room.graph.edge.RoomSpawnEdge;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import dev.compactmods.machines.room.graph.node.RoomSpawnNode;

import java.util.stream.Stream;


public class MemoryGraph {

    @SuppressWarnings("UnstableApiUsage")
    private final MutableValueGraph<IGraphNode<?>, IGraphEdge<?>> graph;

    @SuppressWarnings("UnstableApiUsage")
    public MemoryGraph() {
        this.graph = ValueGraphBuilder
                .directed()
                .build();
    }

    public <T extends IGraphNode<T>> Stream<T> nodes(GraphNodeStream<T> streamFunc) {
        return streamFunc.nodes(graph);
    }

    public <ON extends IGraphNode<ON>, DN extends IGraphNode<DN>> Stream<DN> adjacentNodes(GraphAdjacentNodeStream<ON, DN> streamFunc, ON originNode) {
        return streamFunc.nodes(graph, originNode);
    }

    public <ON extends IGraphNode<ON>, Out> Stream<Out> stream(GraphNodeStreamFunction<ON, Out> streamFunc, ON originNode) {
        return streamFunc.apply(graph, originNode);
    }

    public <ON extends IGraphNode<ON>, Out> Out scalar(GraphNodeScalarFunction<ON, Out> streamFunc, ON originNode) {
        return streamFunc.apply(graph, originNode);
    }

    public <ON extends IGraphNode<ON>, E extends IGraphEdge<E>, DN extends IGraphNode<DN>> Stream<GraphEdgeLookupResult<E, ON, DN>> edges(GraphEdgeStream<ON, E, DN> lookupFunc, ON originNode) {
        return lookupFunc.edges(graph, originNode);
    }

    public <T extends IGraphNode<T>> void removeNode(T node) {
        this.graph.removeNode(node);
    }

    public <T extends IGraphNode<T>> void addNode(T node) {
        this.graph.addNode(node);
    }

    public <U extends IGraphNode<U>, V extends IGraphNode<V>, E extends IGraphEdge<E>> void putEdgeValue(U u, V v, E edge) {
        this.graph.putEdgeValue(u, v, edge);
    }
}
