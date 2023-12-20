package dev.compactmods.machines.room;

import dev.compactmods.compactmachines.api.room.registration.IRoomBuilder;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class NewRoomBuilder implements IRoomBuilder {
    private final String code;
    private int color = 0;
    private Vec3i dimensions = Vec3i.ZERO;
    private Vec3 center = Vec3.ZERO;
    private Vec3 centerOffset = Vec3.ZERO;
    UUID owner;

    NewRoomBuilder() {
        this.code = RoomCodeGenerator.generateRoomId();
    }

    public NewRoomBuilder dimensions(Vec3i dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    public NewRoomBuilder offsetCenter(Vec3 offset) {
        this.centerOffset = offset;
        return this;
    }

    public NewRoomBuilder owner(UUID owner) {
        this.owner = owner;
        return this;
    }

    public NewRoomBuilder defaultMachineColor(int color) {
        this.color = color;
        return this;
    }

    int yOffset() {
        return Math.floorDiv(dimensions.getY(), 2);
    }

    public NewRoomBuilder setCenter(BlockPos newCenter) {
        this.center = Vec3.atCenterOf(newCenter);
        return this;
    }

    RoomRegistrationNode.Data build() {
        return new RoomRegistrationNode.Data(code, color, dimensions, center.add(centerOffset));
    }
}
