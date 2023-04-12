package dev.compactmods.machines.graph;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

public interface IGraphEdge<T extends IGraphEdge<T>> {
    @NotNull Codec<T> codec();
}
