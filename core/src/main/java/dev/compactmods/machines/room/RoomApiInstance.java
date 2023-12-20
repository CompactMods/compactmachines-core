package dev.compactmods.machines.room;

import dev.compactmods.compactmachines.api.room.IRoomApi;
import dev.compactmods.compactmachines.api.room.IRoomRegistrar;
import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import dev.compactmods.compactmachines.api.room.owner.IRoomOwners;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManagers;

public record RoomApiInstance(
        IRoomRegistrar registrar,
        IRoomOwners owners,
        IRoomSpawnManagers spawnManagers,
        IRoomChunkManager chunkManager
) implements IRoomApi {
    @Override
    public IRoomSpawnManager spawnManager(String roomCode) throws NonexistentRoomException {
        return spawnManagers.get(roomCode);
    }
}
