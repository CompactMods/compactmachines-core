package dev.compactmods.compactmachines.api.room;

import dev.compactmods.compactmachines.api.room.owner.IRoomOwners;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunks;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;

public interface IRoomApi {

    IRoomRegistrar registrar();

    IRoomSpawnManager spawnManager(String roomCode);

    IRoomChunkManager chunkManager();

    IRoomChunks chunks(String roomCode);

    IRoomOwners owners();
}
