package dev.compactmods.compactmachines.api.room.spawn;

import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;

public interface IRoomSpawnManagers {

    void make(String roomCode);

    IRoomSpawnManager get(String roomCode) throws NonexistentRoomException;
}
