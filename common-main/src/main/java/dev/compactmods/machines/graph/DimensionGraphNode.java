package dev.compactmods.machines.graph;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record DimensionGraphNode(ResourceKey<Level> dimension) implements IGraphNode<DimensionGraphNode> {

    public static final Codec<DimensionGraphNode> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResourceKey.codec(Registry.DIMENSION_REGISTRY).fieldOf("dim").forGetter(DimensionGraphNode::dimension)
    ).apply(i, DimensionGraphNode::new));

    @Override
    public String toString() {
        return "DimensionGraphNode[%s]".formatted(dimension);
    }

    @Override
    public @NotNull Codec<DimensionGraphNode> codec() {
        return CODEC;
    }
}
