package dev.compactmods.machines.room.graph.edge;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.graph.edge.IGraphEdge;
import org.jetbrains.annotations.NotNull;

public record RoomChunkEdge() implements IGraphEdge<RoomChunkEdge> {

    public static final Codec<RoomChunkEdge> CODEC = Codec.unit(new RoomChunkEdge());

    @Override
    public @NotNull Codec<RoomChunkEdge> codec() {
        return CODEC;
    }
}
