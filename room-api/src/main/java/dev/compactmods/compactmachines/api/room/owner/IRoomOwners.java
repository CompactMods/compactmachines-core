package dev.compactmods.compactmachines.api.room.owner;

import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;

import java.util.UUID;
import java.util.stream.Stream;

public interface IRoomOwners {

    Stream<String> findByOwner(UUID owner);

    UUID getRoomOwner(String roomCode) throws NonexistentRoomException;
}
