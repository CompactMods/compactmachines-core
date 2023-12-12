package dev.compactmods.machines.machine.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.feather.node.Node;
import dev.compactmods.machines.api.core.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a machine's external point. This can be either inside a machine or in a dimension somewhere.
 */
public record CompactMachineNode(UUID id, Data data) implements Node<CompactMachineNode.Data> {

    public CompactMachineNode(ResourceKey<Level> level, BlockPos machine) {
        this(UUID.randomUUID(), new Data(level, machine));
    }

    public record Data(ResourceKey<Level> dimension, BlockPos position) {
        public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
                Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(Data::dimension),
                BlockPos.CODEC.fieldOf("position").forGetter(Data::position)
        ).apply(i, Data::new));

        public GlobalPos dimpos() {
            return GlobalPos.of(dimension, position);
        }
    }
}
