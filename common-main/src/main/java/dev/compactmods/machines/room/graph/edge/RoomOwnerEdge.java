package dev.compactmods.machines.room.graph.edge;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.graph.IGraphEdge;
import org.jetbrains.annotations.NotNull;

public record RoomOwnerEdge() implements IGraphEdge<RoomOwnerEdge> {
    public static final Codec<RoomOwnerEdge> CODEC = Codec.unit(new RoomOwnerEdge());

    @Override
    public @NotNull Codec<RoomOwnerEdge> codec() {
        return CODEC;
    }
}
