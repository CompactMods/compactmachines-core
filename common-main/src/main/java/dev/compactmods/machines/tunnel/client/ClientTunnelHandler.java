package dev.compactmods.machines.tunnel.client;

import dev.compactmods.machines.api.tunnels.ITunnelHolder;
import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;

public class ClientTunnelHandler {

    @Deprecated(forRemoval = true, since = "2.2.1")
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

    public static void setTunnel(BlockPos position, ResourceKey<TunnelDefinition> type) {
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
