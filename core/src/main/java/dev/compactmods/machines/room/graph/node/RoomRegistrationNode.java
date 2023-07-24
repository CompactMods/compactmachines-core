package dev.compactmods.machines.room.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.compactmachines.api.room.registration.IRoomRegistration;
import dev.compactmods.compactmachines.api.room.spatial.IRoomArea;
import dev.compactmods.machines.graph.node.IGraphNode;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Hosts core information about a machine room, such as how large it is and its code.
 * @param code
 * @param dimensions
 */
public record RoomRegistrationNode(String code, int defaultMachineColor, Vec3i dimensions, Vec3 center)
        implements IGraphNode<RoomRegistrationNode>, IRoomRegistration, IRoomArea {

    public static final Codec<RoomRegistrationNode> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("code").forGetter(RoomRegistrationNode::code),
            Codec.INT.fieldOf("color").forGetter(RoomRegistrationNode::defaultMachineColor),
            Vec3i.CODEC.fieldOf("dimensions").forGetter(RoomRegistrationNode::dimensions),
            Vec3.CODEC.fieldOf("center").forGetter(RoomRegistrationNode::center)
    ).apply(i, RoomRegistrationNode::new));

    @Override
    public String toString() {
        return "Room Meta [id=%s]".formatted(code);
    }

    @Override
    public AABB innerBounds() {
        return AABB.ofSize(center, dimensions.getX() - 2, dimensions.getY() - 2, dimensions.getZ() - 2);
    }

    @Override
    public AABB outerBounds() {
        return AABB.ofSize(center, dimensions.getX(), dimensions.getY(), dimensions.getZ());
    }

    @Override
    public @NotNull Codec<RoomRegistrationNode> codec() {
        return CODEC;
    }
}
