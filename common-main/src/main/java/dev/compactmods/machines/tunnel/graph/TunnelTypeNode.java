package dev.compactmods.machines.tunnel.graph;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.graph.IGraphNode;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record TunnelTypeNode(ResourceLocation id) implements IGraphNode<TunnelTypeNode> {
    private static final ResourceLocation TYPE = new ResourceLocation(Constants.MOD_ID, "tunnel_type");

    public static final Codec<TunnelTypeNode> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResourceLocation.CODEC.fieldOf("tunnel_type").forGetter(TunnelTypeNode::id)
    ).apply(i, TunnelTypeNode::new));

    @Override
    public @NotNull Codec<TunnelTypeNode> codec() {
        return CODEC;
    }
}
