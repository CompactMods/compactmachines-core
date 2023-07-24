package dev.compactmods.compactmachines.api.room.spatial;

import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import dev.compactmods.compactmachines.api.room.registration.IRoomRegistration;
import net.minecraft.world.level.ChunkPos;

import java.util.Optional;

public interface IRoomChunkManager {

    Optional<IRoomRegistration> findRoomByChunk(ChunkPos chunk);

    IRoomChunks get(String room) throws NonexistentRoomException;
}
