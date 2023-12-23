package dev.compactmods.machines.room;

import dev.compactmods.compactmachines.api.room.IRoomApi;
import dev.compactmods.compactmachines.api.room.IRoomRegistrar;
import dev.compactmods.compactmachines.api.room.owner.IRoomOwners;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunks;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManagers;

public record RoomApiInstance(
        IRoomRegistrar registrar,
        IRoomOwners owners,
        IRoomSpawnManagers spawnManagers,
        IRoomChunkManager chunkManager
) implements IRoomApi {

    @Override
    public IRoomSpawnManager spawnManager(String roomCode) {
        return spawnManagers.get(roomCode);
    }

    @Override
    public IRoomChunks chunks(String roomCode) {
        return chunkManager.get(roomCode);
    }
}
