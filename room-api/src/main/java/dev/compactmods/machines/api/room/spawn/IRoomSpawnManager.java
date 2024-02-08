package dev.compactmods.machines.api.room.spawn;

import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public interface IRoomSpawnManager {

    void setPlayerSpawn(UUID player, Vec3 location, Vec2 rotation);

    void resetPlayerSpawn(UUID player);

    void setDefaultSpawn(Vec3 location, Vec2 rotation);

    IRoomSpawns spawns();
}
