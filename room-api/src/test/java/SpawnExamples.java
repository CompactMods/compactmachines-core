import dev.compactmods.compactmachines.api.room.Rooms;
import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawn;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManagerManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawns;
import net.minecraft.Util;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class SpawnExamples {

    static void exampleSpawnManagement() throws NonexistentRoomException {

        final IRoomSpawnManagerManager managerManager = Rooms.SPAWN_MANAGER_MANAGER.get();

        IRoomSpawnManager roomSpecificManager = managerManager.manager("MY_ROOM_CODE");

        roomSpecificManager.setDefaultSpawn(Vec3.ZERO, Vec2.ZERO);

        // Player-specific management
        roomSpecificManager.setPlayerSpawn(Util.NIL_UUID, Vec3.ZERO, Vec2.ZERO);
        roomSpecificManager.resetPlayerSpawn(Util.NIL_UUID);

        // Lookup features
        IRoomSpawns spawns = roomSpecificManager.spawns();

        IRoomSpawn defaultSpawn = spawns.defaultSpawn();
        Optional<IRoomSpawn> playerSpawn = spawns.forPlayer(Util.NIL_UUID);
    }
}
