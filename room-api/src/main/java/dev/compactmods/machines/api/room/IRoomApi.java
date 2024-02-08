package dev.compactmods.machines.api.room;

import dev.compactmods.machines.api.room.owner.IRoomOwners;
import dev.compactmods.machines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.machines.api.room.spatial.IRoomChunks;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManager;

public interface IRoomApi {

    IRoomRegistrar registrar();

    IRoomSpawnManager spawnManager(String roomCode);

    IRoomChunkManager chunkManager();

    IRoomChunks chunks(String roomCode);

    IRoomOwners owners();
}
