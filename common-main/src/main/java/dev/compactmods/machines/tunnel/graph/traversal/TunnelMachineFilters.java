package dev.compactmods.machines.tunnel.graph.traversal;

import dev.compactmods.machines.machine.graph.CompactMachineNode;
import dev.compactmods.machines.tunnel.graph.edge.TunnelMachineEdge;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;

public class TunnelMachineFilters {

    public static ITunnelFilter all(GlobalPos machine) {
        return (graph, tunnel) -> GraphTraversalHelper.successors(graph, tunnel, CompactMachineNode.class)
                .map(cm -> cm.dimpos().equals(machine))
                .findFirst()
                .orElse(false);
    }

    public static ITunnelFilter sided(GlobalPos machine, Direction side) {
        return (graph, tunnel) -> GraphTraversalHelper.edges(graph, tunnel, TunnelMachineEdge.class, CompactMachineNode.class)
                .filter(edge -> edge.edgeValue().side() == side)
                .anyMatch(edge -> {
                    // [TunnelNode] --> (TunnelMachineEdge) --> [CompactMachineNode]
                    return edge.target().dimpos().equals(machine);
                });
    }
}
