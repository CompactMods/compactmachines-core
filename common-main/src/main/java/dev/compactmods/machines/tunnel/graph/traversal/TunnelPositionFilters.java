package dev.compactmods.machines.tunnel.graph.traversal;

import dev.compactmods.machines.machine.graph.CompactMachineNode;
import dev.compactmods.machines.tunnel.graph.edge.TunnelMachineEdge;
import net.minecraft.core.BlockPos;

public class TunnelPositionFilters {

    public static ITunnelFilter position(BlockPos position) {
        return (graph, node) -> GraphTraversalHelper.edges(graph, node, TunnelMachineEdge.class, CompactMachineNode.class)
                .map(t -> t.source().position().equals(position))
                .findFirst()
                .orElse(false);
    }
}
