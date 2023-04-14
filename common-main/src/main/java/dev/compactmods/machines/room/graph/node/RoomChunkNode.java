package dev.compactmods.machines.room.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.room.IRoomLookup;
import dev.compactmods.machines.api.room.registration.IRoomRegistration;
import dev.compactmods.machines.codec.CodecExtensions;
import dev.compactmods.machines.graph.IGraphNode;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

public record RoomChunkNode(ChunkPos chunk) implements IGraphNode<RoomChunkNode> {

    public static final Codec<RoomChunkNode> CODEC = RecordCodecBuilder.create(i -> i.group(
            CodecExtensions.CHUNKPOS.fieldOf("chunk").forGetter(RoomChunkNode::chunk)
    ).apply(i, RoomChunkNode::new));

    public IRoomRegistration room(IRoomLookup lookup) {
        return lookup.findByChunk(chunk).orElseThrow();
    }

    @Override
    public @NotNull Codec<RoomChunkNode> codec() {
        return CODEC;
    }
}
