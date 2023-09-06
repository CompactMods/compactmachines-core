package dev.compactmods.machines.room.graph;

import com.google.common.graph.ValueGraph;
import dev.compactmods.machines.graph.edge.IGraphEdge;
import dev.compactmods.machines.graph.node.IGraphNode;

    @FunctionalInterface
public interface GraphNodeScalarFunction<ON, Out> {
    Out apply(ValueGraph<IGraphNode<?>, IGraphEdge<?>> graph, ON input);
}
