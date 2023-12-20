package dev.compactmods.compactmachines.api.room.spatial;

import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import net.minecraft.world.level.ChunkPos;

import java.util.Optional;

public interface IRoomChunkManager {

    Optional<String> findRoomByChunk(ChunkPos chunk);

    IRoomChunks get(String room) throws NonexistentRoomException;
}
