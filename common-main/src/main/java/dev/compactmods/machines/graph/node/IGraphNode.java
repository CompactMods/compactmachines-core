package dev.compactmods.machines.graph.node;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

public interface IGraphNode<T extends IGraphNode<T>> {
    @NotNull
    Codec<T> codec();
}
