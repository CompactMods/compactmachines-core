package dev.compactmods.machines.tunnel.graph.nbt;

import dev.compactmods.machines.graph.node.IGraphNode;

import java.util.Map;
import java.util.UUID;

public record TunnelGraphNodeDeserializationResult<T extends IGraphNode<T>>(Map<UUID, T> results) {
}
