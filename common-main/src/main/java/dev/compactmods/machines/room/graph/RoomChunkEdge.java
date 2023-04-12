package dev.compactmods.machines.room.graph;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.graph.IGraphEdge;
import org.jetbrains.annotations.NotNull;

public record RoomChunkEdge() implements IGraphEdge<RoomChunkEdge> {

    public static final Codec<RoomChunkEdge> CODEC = Codec.unit(new RoomChunkEdge());

    @Override
    public @NotNull Codec<RoomChunkEdge> codec() {
        return CODEC;
    }
}
