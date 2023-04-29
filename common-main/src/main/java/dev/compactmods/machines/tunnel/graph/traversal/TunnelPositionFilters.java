package dev.compactmods.machines.tunnel.graph.traversal;

import dev.compactmods.machines.graph.GraphTraversalHelper;
import dev.compactmods.machines.machine.graph.node.CompactMachineNode;
import dev.compactmods.machines.tunnel.graph.edge.TunnelMachineEdge;
import net.minecraft.core.BlockPos;

public class TunnelPositionFilters {

    public static ITunnelFilter position(BlockPos position) {
        return (graph, node) -> GraphTraversalHelper.edges(graph.graph(), node, TunnelMachineEdge.class, CompactMachineNode.class)
                .map(t -> t.source().position().equals(position))
                .findFirst()
                .orElse(false);
    }
}
