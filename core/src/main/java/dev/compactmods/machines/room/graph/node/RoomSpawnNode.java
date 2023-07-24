package dev.compactmods.machines.room.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.codec.CodecExtensions;
import dev.compactmods.machines.graph.node.IGraphNode;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class RoomSpawnNode implements IGraphNode<RoomSpawnNode> {

    public static final Codec<RoomSpawnNode> CODEC = RecordCodecBuilder.create(i -> i.group(
            Vec3.CODEC.fieldOf("position").forGetter(RoomSpawnNode::position),
            CodecExtensions.VEC2.fieldOf("rotation").forGetter(RoomSpawnNode::rotation)
    ).apply(i, RoomSpawnNode::new));

    public Vec3 position;
    public Vec2 rotation;

    public RoomSpawnNode(Vec3 position, Vec2 rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    @Override
    public @NotNull Codec<RoomSpawnNode> codec() {
        return CODEC;
    }

    public Vec3 position() {
        return position;
    }

    public Vec2 rotation() {
        return rotation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RoomSpawnNode) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.rotation, that.rotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, rotation);
    }

    @Override
    public String toString() {
        return "RoomSpawnNode[" +
                "position=" + position + ", " +
                "rotation=" + rotation + ']';
    }

}
