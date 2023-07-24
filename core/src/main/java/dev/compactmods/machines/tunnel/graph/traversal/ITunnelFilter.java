package dev.compactmods.machines.tunnel.graph.traversal;

import dev.compactmods.machines.tunnel.graph.TunnelConnectionGraph;
import dev.compactmods.machines.tunnel.graph.node.TunnelNode;

@FunctionalInterface
public interface ITunnelFilter {
    boolean filter(TunnelConnectionGraph graph, TunnelNode node);
}
