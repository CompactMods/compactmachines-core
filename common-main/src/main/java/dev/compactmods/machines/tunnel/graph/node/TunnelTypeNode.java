package dev.compactmods.machines.tunnel.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import dev.compactmods.machines.graph.IGraphNode;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record TunnelTypeNode(ResourceKey<TunnelDefinition> key) implements IGraphNode<TunnelTypeNode> {
    private static final ResourceLocation TYPE = new ResourceLocation(Constants.MOD_ID, "tunnel_type");

    public static final Codec<TunnelTypeNode> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResourceKey.codec(TunnelDefinition.REGISTRY_KEY).fieldOf("tunnel_type").forGetter(TunnelTypeNode::key)
    ).apply(i, TunnelTypeNode::new));

    @Override
    public @NotNull Codec<TunnelTypeNode> codec() {
        return CODEC;
    }
}
