package dev.compactmods.machines.machine.graph.edge;

import dev.compactmods.feather.edge.GraphEdge;
import dev.compactmods.machines.machine.graph.node.CompactMachineNode;
import dev.compactmods.machines.room.graph.node.RoomReferenceNode;

import java.lang.ref.WeakReference;

public record MachineRoomEdge(WeakReference<CompactMachineNode> source, WeakReference<RoomReferenceNode> target)
        implements GraphEdge<CompactMachineNode, RoomReferenceNode> {
}
