package dev.compactmods.machines.graph;

import com.google.common.graph.ValueGraph;
import dev.compactmods.machines.graph.edge.GraphEdgeLookupResult;
import dev.compactmods.machines.graph.edge.IGraphEdge;
import dev.compactmods.machines.graph.node.IGraphNode;

import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class GraphTraversalHelper {

    public static <Node extends IGraphNode<?>, Edge extends IGraphEdge<?>, T extends IGraphNode<T>>
    Stream<T> nodes(ValueGraph<Node, Edge> graph, Class<T> type) {
        return graph.nodes()
                .stream()
                .filter(type::isInstance)
                .map(type::cast);
    }

    public static <S extends IGraphNode<S>, T extends IGraphNode<T>, Edge extends IGraphEdge<?>> Stream<T> predecessors(
            ValueGraph<IGraphNode<?>, Edge> graph, S sourceNode, Class<T> targetNodeClass) {
        return graph.predecessors(sourceNode)
                .stream()
                .filter(targetNodeClass::isInstance)
                .map(targetNodeClass::cast);
    }

    public static <S extends IGraphNode<S>, T extends IGraphNode<T>, Edge extends IGraphEdge<?>> Stream<T> successors(
            ValueGraph<IGraphNode<?>, Edge> graph, S sourceNode, Class<T> targetNodeClass) {
        return graph.successors(sourceNode)
                .stream()
                .filter(targetNodeClass::isInstance)
                .map(targetNodeClass::cast);
    }

    public static <Node extends IGraphNode<?>, Edge extends IGraphEdge<?>, S extends IGraphNode<S>, T extends IGraphNode<T>, E extends IGraphEdge<E>> Stream<GraphEdgeLookupResult<E, S, T>> edges(
            ValueGraph<IGraphNode<?>, Edge> graph, Class<E> type) {
        return graph.edges()
                .stream()
                .map(e -> {
                    final var ev = graph.edgeValue(e);
                    return ev.map(ige -> type.isInstance(ige) ? new GraphEdgeLookupResult<E, S, T>(e, type.cast(ige)) : null)
                            .orElse(null);
                })
                .filter(Objects::nonNull);
    }

    public static <Node extends IGraphNode<?>, Edge extends IGraphEdge<?>, S extends IGraphNode<S>, T extends IGraphNode<T>, E extends IGraphEdge<E>> Stream<GraphEdgeLookupResult<E, S, T>> edges(
            ValueGraph<IGraphNode<?>, Edge> graph, IGraphNode<S> sourceNode, Class<E> edgeType) {
        return graph.incidentEdges(sourceNode)
                .stream()
                .map(e -> {
                    final var ev = graph.edgeValue(e);
                    return ev.map(ige -> edgeType.isInstance(ige) ? new GraphEdgeLookupResult<E, S, T>(e, edgeType.cast(ige)) : null)
                            .orElse(null);
                })
                .filter(Objects::nonNull);
    }

    public static <Node extends IGraphNode<?>, Edge extends IGraphEdge<?>, S extends IGraphNode<S>, T extends IGraphNode<T>, E extends IGraphEdge<E>>
    Stream<GraphEdgeLookupResult<E, S, T>> edges(ValueGraph<IGraphNode<?>, Edge> graph, IGraphNode<S> sourceNode, Class<E> edgeType, Class<T> targetNodeType) {
        return graph.incidentEdges(sourceNode)
                .stream()
                .map(e -> {
                    final var ev = graph.edgeValue(e);
                    return ev.map(ige -> edgeType.isInstance(ige) ? new GraphEdgeLookupResult<E, S, T>(e, edgeType.cast(ige)) : null)
                            .orElse(null);
                })
                .filter(Objects::nonNull);
    }
}
