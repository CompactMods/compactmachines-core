package dev.compactmods.machines.tunnel.client;

import dev.compactmods.machines.api.tunnels.ITunnelHolder;
import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class ClientTunnelHandler {
    public static void setTunnel(BlockPos position, TunnelDefinition type) {
        var level = Minecraft.getInstance().level;
        if(level == null) return;

        Minecraft.getInstance().tell(() -> {
            if (level.getBlockEntity(position) instanceof ITunnelHolder tun) {
                try {
                    tun.setTunnelType(type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
