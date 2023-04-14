package dev.compactmods.machines.tunnel.graph.edge;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.graph.IGraphEdge;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record TunnelTypeEdge() implements IGraphEdge<TunnelTypeEdge> {
    private static final ResourceLocation TYPE = new ResourceLocation(Constants.MOD_ID, "tunnel_type");

    public static final Codec<TunnelTypeEdge> CODEC = Codec.unit(new TunnelTypeEdge());

    @Override
    public String toString() {
        return "TunnelTypeEdge[]";
    }

    @Override
    public @NotNull Codec<TunnelTypeEdge> codec() {
        return CODEC;
    }
}
