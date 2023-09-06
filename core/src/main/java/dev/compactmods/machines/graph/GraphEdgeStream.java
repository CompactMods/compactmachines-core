package dev.compactmods.machines.graph;

import com.google.common.graph.ValueGraph;
import dev.compactmods.machines.graph.edge.GraphEdgeLookupResult;
import dev.compactmods.machines.graph.edge.IGraphEdge;
import dev.compactmods.machines.graph.node.IGraphNode;

import java.util.stream.Stream;

@FunctionalInterface
public interface GraphEdgeStream<Origin extends IGraphNode<Origin>, Edge extends IGraphEdge<Edge>, Destination extends IGraphNode<Destination>> {

    Stream<GraphEdgeLookupResult<Edge, Origin, Destination>> edges(ValueGraph<IGraphNode<?>, IGraphEdge<?>> graph, Origin originNode);
}
