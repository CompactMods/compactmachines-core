package dev.compactmods.compactmachines.api.room;

import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import dev.compactmods.compactmachines.api.room.owner.IRoomOwners;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;

public interface IRoomApi {

    IRoomRegistrar registrar();

    IRoomOwners owners();

    IRoomSpawnManager spawnManager(String roomCode) throws NonexistentRoomException;

    IRoomChunkManager chunkManager();
}
