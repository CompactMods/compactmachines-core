package dev.compactmods.compactmachines.api.room.spawn;

import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public interface IRoomSpawn {
    Vec3 location();
    Vec2 rotation();
}
