package dev.compactmods.machines.api.tunnels;

import net.minecraft.resources.ResourceKey;

public interface ITunnelHolder {

    TunnelDefinition getTunnelType();

    @Deprecated(forRemoval = true, since = "2.2.1")
    void setTunnelType(TunnelDefinition type);

    void setTunnelType(ResourceKey<TunnelDefinition> type);
}
