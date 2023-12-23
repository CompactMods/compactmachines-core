package dev.compactmods.machines.room.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.compactmachines.api.room.spatial.IRoomBoundaries;
import dev.compactmods.feather.node.Node;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

/**
 * Hosts core information about a machine room, such as how large it is and its code.
 */
public record RoomRegistrationNode(UUID id, Data data) implements Node<RoomRegistrationNode.Data>, IRoomBoundaries {

    public record Data(String code, int defaultMachineColor, Vec3i dimensions, Vec3 center) {
        public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("code").forGetter(Data::code),
                Codec.INT.fieldOf("color").forGetter(Data::defaultMachineColor),
                Vec3i.CODEC.fieldOf("dimensions").forGetter(Data::dimensions),
                Vec3.CODEC.fieldOf("center").forGetter(Data::center)
        ).apply(i, Data::new));
    }

    public String code() {
        return data.code;
    }

    public int defaultMachineColor() {
        return data.defaultMachineColor;
    }

    @Override
    public Vec3i dimensions() {
        return null;
    }

    @Override
    public Vec3 center() {
        return null;
    }

    @Override
    public AABB innerBounds() {
        return AABB.ofSize(data.center, data.dimensions.getX() - 2, data.dimensions.getY() - 2, data.dimensions.getZ() - 2);
    }

    @Override
    public AABB outerBounds() {
        return AABB.ofSize(data.center, data.dimensions.getX(), data.dimensions.getY(), data.dimensions.getZ());
    }
}
