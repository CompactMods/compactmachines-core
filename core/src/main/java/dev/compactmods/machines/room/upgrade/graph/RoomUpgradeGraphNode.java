package dev.compactmods.machines.room.upgrade.graph;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.graph.node.IGraphNode;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record RoomUpgradeGraphNode(ResourceLocation key) implements IGraphNode<RoomUpgradeGraphNode> {

    public static final Codec<RoomUpgradeGraphNode> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResourceLocation.CODEC.fieldOf("upgrade").forGetter(RoomUpgradeGraphNode::key)
    ).apply(i, RoomUpgradeGraphNode::new));

    @Override
    public @NotNull Codec<RoomUpgradeGraphNode> codec() {
        return CODEC;
    }
}
