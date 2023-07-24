import dev.compactmods.compactmachines.api.room.Rooms;
import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunks;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawns;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class RoomSpawnTest {

    static void example() {

        try {
            final var lookup = Rooms.SPAWN_MANAGER_MANAGER.get().manager("MY_ROOM_CODE");
        } catch (NonexistentRoomException e) {
            throw new RuntimeException(e);
        }
    }

    static void exampleAddonHook(IRoomSpawns spawns) {
        Vec3 spawnLocation = spawns.defaultSpawn().location();
        BlockPos doNotAllowBlocksHere = new BlockPos(spawnLocation);

    }

    static void chunkloaderHook(String room) {
        // mod constructor
        // Rooms.CHUNK_MANAGER = GraphRoomChunkManager::manager;

        // Somewhere else
        try {
            IRoomChunks chunks = Rooms.CHUNK_MANAGER.get().get(room);
            chunks.stream().forEach(chunkPos -> {
                System.out.printf("Chunk position: [%s,%s]", chunkPos.x, chunkPos.z);
            });
        } catch (NonexistentRoomException e) {
            throw new RuntimeException(e);
        }
    }
}
