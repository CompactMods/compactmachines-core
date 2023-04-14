package dev.compactmods.machines.room.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.room.IRoomLookup;
import dev.compactmods.machines.api.room.registration.IRoomRegistration;
import dev.compactmods.machines.graph.IGraphNode;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the inside of a Compact Machine.
 */
public record RoomReferenceNode(String code) implements IGraphNode<RoomReferenceNode> {

    public static final Codec<RoomReferenceNode> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("code").forGetter(RoomReferenceNode::code)
    ).apply(i, RoomReferenceNode::new));

    public IRoomRegistration getFullInfo(IRoomLookup lookup) {
        return lookup.forRoom(code).orElseThrow();
    }

    @Override
    public String toString() {
        return "RoomReference[id=%s]".formatted(code);
    }

    @Override
    public @NotNull Codec<RoomReferenceNode> codec() {
        return CODEC;
    }
}
