package dev.compactmods.compactmachines.api.room.spawn;

public interface IRoomSpawnManagers {

    void make(String roomCode);

    IRoomSpawnManager get(String roomCode);
}
