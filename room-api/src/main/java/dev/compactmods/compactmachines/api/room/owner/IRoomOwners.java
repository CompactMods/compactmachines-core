package dev.compactmods.compactmachines.api.room.owner;

import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import dev.compactmods.compactmachines.api.room.registration.IRoomRegistration;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface IRoomOwners {

    Stream<IRoomRegistration> findByOwner(UUID owner);

    UUID getRoomOwner(String roomCode) throws NonexistentRoomException;
}
