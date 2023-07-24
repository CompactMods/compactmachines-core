package dev.compactmods.machines.room.graph.edge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.graph.edge.IGraphEdge;
import net.minecraft.core.UUIDUtil;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record RoomSpawnEdge(UUID player) implements IGraphEdge<RoomSpawnEdge> {

    public static final Codec<RoomSpawnEdge> CODEC = RecordCodecBuilder.create(i -> i.group(
            UUIDUtil.CODEC.fieldOf("player").forGetter(RoomSpawnEdge::player)
    ).apply(i, RoomSpawnEdge::new));

    @Override
    public @NotNull Codec<RoomSpawnEdge> codec() {
        return CODEC;
    }
}