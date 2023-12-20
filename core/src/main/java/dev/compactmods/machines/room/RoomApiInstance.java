package dev.compactmods.machines.room;

import dev.compactmods.compactmachines.api.room.IRoomApi;
import dev.compactmods.compactmachines.api.room.IRoomRegistrar;
import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import dev.compactmods.compactmachines.api.room.owner.IRoomOwners;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManagers;
import org.jetbrains.annotations.ApiStatus;

public class RoomApiInstance implements IRoomApi {

    @ApiStatus.Internal
    private static IRoomRegistrar ROOM_REGISTRY;

    @ApiStatus.Internal
    private static IRoomOwners OWNER_REGISTRY;

    @ApiStatus.Internal
    private static IRoomSpawnManagers SPAWN_MANAGERS;

    @ApiStatus.Internal
    private static IRoomChunkManager CHUNK_MANAGER;

    @Override
    public IRoomRegistrar registrar() {
        return ROOM_REGISTRY;
    }

    @Override
    public IRoomOwners owners() {
        return OWNER_REGISTRY;
    }

    @Override
    public IRoomSpawnManager spawnManager(String roomCode) throws NonexistentRoomException {
        return SPAWN_MANAGERS.get(roomCode);
    }

    @Override
    public IRoomChunkManager chunkManager() {
        return CHUNK_MANAGER;
    }
}
