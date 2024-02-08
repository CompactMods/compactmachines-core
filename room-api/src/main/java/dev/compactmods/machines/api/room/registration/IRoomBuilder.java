package dev.compactmods.machines.api.room.registration;

import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public interface IRoomBuilder {

    IRoomBuilder dimensions(Vec3i dimensions);

    IRoomBuilder offsetCenter(Vec3 offset);

    IRoomBuilder owner(UUID owner);

    IRoomBuilder defaultMachineColor(int color);

}
