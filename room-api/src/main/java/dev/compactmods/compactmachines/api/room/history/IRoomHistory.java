package dev.compactmods.compactmachines.api.room.history;

public interface IRoomHistory<T extends IRoomHistoryItem> {

    void clear();
    boolean hasHistory();
    T peek();
    T pop();

    void addHistory(T item);
}
