package dev.compactmods.compactmachines.api.room;

import dev.compactmods.compactmachines.api.room.spatial.IRoomBoundaries;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunks;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;

import java.util.function.Supplier;

public record RoomInstance(String code,
                           int defaultMachineColor,
                           IRoomBoundaries boundaries,
                           Supplier<IRoomSpawnManager> spawns,
                           Supplier<IRoomChunks> chunks) {

}
