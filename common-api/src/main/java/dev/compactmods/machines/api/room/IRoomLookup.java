package dev.compactmods.machines.api.room;

import dev.compactmods.machines.api.room.registration.IRoomRegistration;
import net.minecraft.world.level.ChunkPos;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface IRoomLookup {

    Stream<IRoomRegistration> findByOwner(UUID owner);

    Optional<IRoomRegistration> forRoom(String room);

    Optional<IRoomRegistration> findByChunk(ChunkPos chunk);

    boolean isRoomChunk(ChunkPos chunk);

    long count();

    Stream<IRoomRegistration> allRooms();
}
