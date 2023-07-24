package dev.compactmods.machines.tunnel.client;

import dev.compactmods.machines.api.tunnels.ITunnelHolder;
import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

public class ClientTunnelHandler {

    @Deprecated(forRemoval = true, since = "2.2.1")
    public static void setTunnel(BlockPos position, TunnelDefinition type) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;

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

    @Nullable
    public static ITunnelHolder getTunnelHolder(BlockPos position) {
        var level = Minecraft.getInstance().level;
        if (level == null) return null;
        if (level.getBlockEntity(position) instanceof ITunnelHolder tun)
            return tun;

        return null;
    }

    public static void setTunnel(BlockPos position, ResourceKey<TunnelDefinition> type) {
        try {
            final var tun = getTunnelHolder(position);
            if (tun != null)
                tun.setTunnelType(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
