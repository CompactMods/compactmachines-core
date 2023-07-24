package dev.compactmods.machines.room.spatial;

import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import dev.compactmods.compactmachines.api.room.registration.IRoomRegistration;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunks;
import net.minecraft.world.level.ChunkPos;

import java.util.Optional;

public class GraphChunkManager implements IRoomChunkManager {
    @Override
    public Optional<IRoomRegistration> findRoomByChunk(ChunkPos chunk) {
        return;
    }

    @Override
    public IRoomChunks get(String room) throws NonexistentRoomException {
        return null;
    }
}
