package dev.compactmods.compactmachines.api.room;

import dev.compactmods.compactmachines.api.room.owner.IRoomOwners;
import dev.compactmods.compactmachines.api.room.spatial.IRoomArea;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunks;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawns;

public interface IRoomInstance {

    IRoomArea area();

    IRoomSpawns spawns();

    IRoomOwners owners();

    IRoomChunks chunks();
}
