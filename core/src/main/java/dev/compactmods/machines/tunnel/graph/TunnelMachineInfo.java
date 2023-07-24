package dev.compactmods.machines.tunnel.graph;

import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public record TunnelMachineInfo(BlockPos location, ResourceKey<TunnelDefinition> type, GlobalPos machine, Direction side) {
}
