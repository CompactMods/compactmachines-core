package dev.compactmods.machines.tunnel.graph.nbt;

import dev.compactmods.machines.graph.node.IGraphNode;
import net.minecraft.nbt.ListTag;

import java.util.Map;
import java.util.UUID;

public record TunnelGraphNodeSerializationResult<T extends IGraphNode<T>>(Map<T, UUID> idMap, ListTag listTag) {}
