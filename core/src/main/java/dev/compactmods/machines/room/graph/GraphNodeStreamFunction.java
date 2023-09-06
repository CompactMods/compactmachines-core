package dev.compactmods.machines.room.graph;

import com.google.common.graph.ValueGraph;
import dev.compactmods.machines.graph.edge.IGraphEdge;
import dev.compactmods.machines.graph.node.IGraphNode;

import java.util.stream.Stream;

@FunctionalInterface
public interface GraphNodeStreamFunction<ON, Out> {

    Stream<Out> apply(ValueGraph<IGraphNode<?>, IGraphEdge<?>> graph, ON input);
}
