package dev.compactmods.compactmachines.api.room.spawn;

import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;

public interface IRoomSpawnManagerManager {

    IRoomSpawnManager manager(String roomCode) throws NonexistentRoomException;
}
