package dev.compactmods.compactmachines.api.room;

import dev.compactmods.compactmachines.api.room.registration.IRoomRegistration;
import net.minecraft.world.level.ChunkPos;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface IRoomRegistrar {

    boolean isRegistered(String room);

    Optional<IRoomInstance> get(String room);

    long count();

    Stream<IRoomRegistration> allRooms();
}
