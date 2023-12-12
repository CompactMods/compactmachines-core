package dev.compactmods.machines.machine.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.feather.node.Node;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record DimensionGraphNode(UUID id, Data data) implements Node<DimensionGraphNode.Data> {

    public @NotNull Codec<DimensionGraphNode.Data> codec() {
        return Data.CODEC;
    }

    public record Data(ResourceKey<Level> dimension) {
        public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
                ResourceKey.codec(Registries.DIMENSION).fieldOf("dim").forGetter(Data::dimension)
        ).apply(i, Data::new));
    }
}
