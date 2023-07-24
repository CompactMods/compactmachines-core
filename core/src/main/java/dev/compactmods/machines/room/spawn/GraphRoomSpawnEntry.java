package dev.compactmods.machines.room.spawn;

import dev.compactmods.machines.api.room.spawn.IRoomSpawnLookup;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public record GraphRoomSpawnEntry(Vec3 location, Vec2 rotation) implements IRoomSpawnLookup {}
