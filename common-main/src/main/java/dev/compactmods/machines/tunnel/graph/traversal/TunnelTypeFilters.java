package dev.compactmods.machines.tunnel.graph.traversal;

import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import dev.compactmods.machines.api.tunnels.redstone.RedstoneTunnel;
import dev.compactmods.machines.tunnel.graph.edge.GraphEdgeLookupResult;
import dev.compactmods.machines.tunnel.graph.edge.TunnelTypeEdge;
import dev.compactmods.machines.tunnel.graph.node.TunnelTypeNode;
import net.minecraft.resources.ResourceKey;

import java.util.function.Function;
import java.util.function.Predicate;

public class TunnelTypeFilters {

    public static ITunnelFilter definition(Predicate<TunnelDefinition> predicate, Function<ResourceKey<TunnelDefinition>, TunnelDefinition> definitionLookup) {
        return (graph, node) -> GraphTraversalHelper.edges(graph, node, TunnelTypeEdge.class, TunnelTypeNode.class)
                .map(GraphEdgeLookupResult::target)
                .map(type -> definitionLookup.apply(type.key()))
                .findFirst()
                .map(predicate::test)
                .orElse(false);
    }

    public static ITunnelFilter key(ResourceKey<TunnelDefinition> key) {
        return (graph, node) -> GraphTraversalHelper.edges(graph, node, TunnelTypeEdge.class, TunnelTypeNode.class)
                .map(GraphEdgeLookupResult::target)
                .map(type -> type.key().equals(key))
                .findFirst()
                .orElse(false);
    }

    public static ITunnelFilter redstone(Function<ResourceKey<TunnelDefinition>, TunnelDefinition> definitionLookup) {
        return definition(def -> def instanceof RedstoneTunnel, definitionLookup);
    }
}
