package dev.compactmods.machines.api.tunnels.redstone;

import dev.compactmods.machines.api.tunnels.TunnelPosition;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;

public interface RedstoneReaderTunnel extends RedstoneTunnel {

    int powerLevel(MinecraftServer server, GlobalPos machine, TunnelPosition tunnel);

}
