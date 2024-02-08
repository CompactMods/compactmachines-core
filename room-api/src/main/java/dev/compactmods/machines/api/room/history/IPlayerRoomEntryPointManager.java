package dev.compactmods.machines.api.room.history;

public interface IPlayerRoomEntryPointManager {

    void clear();
    boolean hasHistory();

    RoomEntryPoint pop();

    void addHistory(RoomEntryPoint item);
}
