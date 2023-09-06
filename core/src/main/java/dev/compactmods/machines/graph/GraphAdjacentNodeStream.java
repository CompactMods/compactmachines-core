package dev.compactmods.machines.graph;

import com.google.common.graph.ValueGraph;
import dev.compactmods.machines.graph.edge.IGraphEdge;
import dev.compactmods.machines.graph.node.IGraphNode;

import java.util.stream.Stream;

@FunctionalInterface
public interface GraphAdjacentNodeStream<Origin extends IGraphNode<Origin>, Destination extends IGraphNode<Destination>> {

    Stream<Destination> nodes(ValueGraph<IGraphNode<?>, IGraphEdge<?>> graph, Origin originNode);
}
