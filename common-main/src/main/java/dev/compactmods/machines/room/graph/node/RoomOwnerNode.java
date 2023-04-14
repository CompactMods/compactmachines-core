package dev.compactmods.machines.room.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.graph.IGraphNode;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record RoomOwnerNode(UUID owner) implements IGraphNode<RoomOwnerNode> {

    private static final ResourceLocation TYPE = new ResourceLocation(Constants.MOD_ID, "room");

    public static final Codec<RoomOwnerNode> CODEC = RecordCodecBuilder.create((i) -> i.group(
            UUIDUtil.CODEC.fieldOf("owner").forGetter(RoomOwnerNode::owner)
    ).apply(i, RoomOwnerNode::new));

    @Override
    public @NotNull Codec<RoomOwnerNode> codec() {
        return CODEC;
    }
}
