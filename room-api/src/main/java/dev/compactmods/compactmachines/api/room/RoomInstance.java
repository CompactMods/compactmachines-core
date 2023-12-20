package dev.compactmods.compactmachines.api.room;

import dev.compactmods.compactmachines.api.room.owner.IRoomOwners;
import dev.compactmods.compactmachines.api.room.spatial.IRoomArea;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunks;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawns;

import java.util.function.Supplier;

public record RoomInstance(String code,
                           int defaultMachineColor,
                           Supplier<IRoomArea> area,
                           Supplier<IRoomSpawns> spawns,
                           Supplier<IRoomOwners> owners,
                           Supplier<IRoomChunks> chunks) {

}
