package dev.compactmods.machines.graph;

import com.google.common.graph.ValueGraph;
import dev.compactmods.machines.graph.edge.IGraphEdge;
import dev.compactmods.machines.graph.node.IGraphNode;

import java.util.stream.Stream;

@FunctionalInterface
public interface GraphNodeStream<T extends IGraphNode<T>> {

    Stream<T> nodes(ValueGraph<IGraphNode<?>, IGraphEdge<?>> graph);
}
