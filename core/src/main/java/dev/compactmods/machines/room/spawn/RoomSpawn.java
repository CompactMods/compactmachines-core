package dev.compactmods.machines.room.spawn;

import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawn;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public record RoomSpawn(Vec3 location, Vec2 rotation) implements IRoomSpawn {
}