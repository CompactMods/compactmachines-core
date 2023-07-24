package dev.compactmods.machines.machine.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.graph.node.IGraphNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a machine's external point. This can be either inside a machine or in a dimension somewhere.
 */
public record CompactMachineNode(ResourceKey<Level> dimension, BlockPos position)
        implements IGraphNode<CompactMachineNode> {

    public static final ResourceLocation TYPE = new ResourceLocation(Constants.MOD_ID, "machine");

    public static final Codec<CompactMachineNode> CODEC = RecordCodecBuilder.create(i -> i.group(
            Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(CompactMachineNode::dimension),
            BlockPos.CODEC.fieldOf("position").forGetter(CompactMachineNode::position)
    ).apply(i, CompactMachineNode::new));

    public String toString() {
        return "Compact Machine {%s}".formatted(position);
    }

    public GlobalPos dimpos() {
        return GlobalPos.of(dimension, position);
    }

    @Override
    public @NotNull Codec<CompactMachineNode> codec() {
        return CODEC;
    }
}
