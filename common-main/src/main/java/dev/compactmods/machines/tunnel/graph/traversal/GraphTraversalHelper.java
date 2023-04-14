package dev.compactmods.machines.tunnel.graph.traversal;

import dev.compactmods.machines.tunnel.graph.edge.GraphEdgeLookupResult;
import dev.compactmods.machines.tunnel.graph.TunnelConnectionGraph;
import dev.compactmods.machines.graph.IGraphEdge;
import dev.compactmods.machines.graph.IGraphNode;

import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class GraphTraversalHelper {

    public static <T extends IGraphNode<T>> Stream<T> nodes(TunnelConnectionGraph graph, Class<T> type) {
        return graph.graph().nodes()
                .stream()
                .filter(type::isInstance)
                .map(type::cast);
    }

    public static <S extends IGraphNode<S>, T extends IGraphNode<T>> Stream<T> successors(
            TunnelConnectionGraph graph, S sourceNode, Class<T> targetNodeClass) {
        return graph.graph().successors(sourceNode)
                .stream()
                .filter(targetNodeClass::isInstance)
                .map(targetNodeClass::cast);
    }

    public static <S extends IGraphNode<S>, T extends IGraphNode<T>, E extends IGraphEdge<E>> Stream<GraphEdgeLookupResult<E, S, T>> edges(
            TunnelConnectionGraph graph, Class<E> type) {
        return graph.graph().edges()
                .stream()
                .map(e -> {
                    final var ev = graph.graph().edgeValue(e);
                    return ev.map(ige -> type.isInstance(ige) ? new GraphEdgeLookupResult<E, S, T>(e, type.cast(ige)) : null)
                            .orElse(null);
                })
                .filter(Objects::nonNull);
    }

    public static <S extends IGraphNode<S>, T extends IGraphNode<T>, E extends IGraphEdge<E>> Stream<GraphEdgeLookupResult<E, S, T>> edges(
            TunnelConnectionGraph graph, IGraphNode<S> sourceNode, Class<E> edgeType) {
        return graph.graph().incidentEdges(sourceNode)
                .stream()
                .map(e -> {
                    final var ev = graph.graph().edgeValue(e);
                    return ev.map(ige -> edgeType.isInstance(ige) ? new GraphEdgeLookupResult<E, S, T>(e, edgeType.cast(ige)) : null)
                            .orElse(null);
                })
                .filter(Objects::nonNull);
    }

    public static <S extends IGraphNode<S>, T extends IGraphNode<T>, E extends IGraphEdge<E>>
        Stream<GraphEdgeLookupResult<E, S, T>> edges(TunnelConnectionGraph graph, IGraphNode<S> sourceNode, Class<E> edgeType, Class<T> targetNodeType) {
        return graph.graph().incidentEdges(sourceNode)
                .stream()
                .map(e -> {
                    final var ev = graph.graph().edgeValue(e);
                    return ev.map(ige -> edgeType.isInstance(ige) ? new GraphEdgeLookupResult<E, S, T>(e, edgeType.cast(ige)) : null)
                            .orElse(null);
                })
                .filter(Objects::nonNull);
    }
}
