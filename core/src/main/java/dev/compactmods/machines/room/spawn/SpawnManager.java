package dev.compactmods.machines.room.spawn;

import dev.compactmods.compactmachines.api.room.spatial.IRoomBoundaries;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawn;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawns;
import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.api.dimension.MissingDimensionException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SpawnManager extends SavedData implements IRoomSpawnManager {

    private final String roomCode;
    private IRoomBoundaries roomBounds;
    private RoomSpawn defaultSpawn;
    private final Map<UUID, RoomSpawn> playerSpawns;

    public SpawnManager(String roomCode) {
        this.roomCode = roomCode;
        this.playerSpawns = new HashMap<>();
    }

    private static Factory<SpawnManager> factory(MinecraftServer server, String roomCode) {
        return new SavedData.Factory<>(() -> new SpawnManager(roomCode), tag -> {
            final var inst = new SpawnManager(roomCode);
            return SpawnManager.Serializer.loadInto(inst, tag);
        }, null);
    }

    public static SpawnManager forRoom(MinecraftServer server, String roomCode, IRoomBoundaries roomBounds) throws MissingDimensionException {
        String roomFilename = Constants.MOD_ID + "_room_" + roomCode;
        var manager = CompactDimension.forServer(server)
                .getDataStorage()
                .computeIfAbsent(factory(server, roomCode), roomFilename);

        manager.setBoundaries(roomBounds);
        manager.setDefaultSpawn(roomBounds.defaultSpawn(), Vec2.ZERO);
        return manager;
    }

    private void setBoundaries(IRoomBoundaries roomBounds) {
        this.roomBounds = roomBounds;
    }

    @Override
    public void resetPlayerSpawn(UUID player) {
        playerSpawns.remove(player);
    }

    @Override
    public void setDefaultSpawn(Vec3 position, Vec2 rotation) {
        defaultSpawn = new RoomSpawn(position, rotation);
    }

    @Override
    public IRoomSpawns spawns() {
        final var ps = new HashMap<UUID, RoomSpawn>();
        playerSpawns.forEach(ps::putIfAbsent);
        return new RoomSpawns(defaultSpawn, ps);
    }

    @Override
    public void setPlayerSpawn(UUID player, Vec3 location, Vec2 rotation) {
        if(playerSpawns.containsKey(player))
            playerSpawns.replace(player, new RoomSpawn(location, rotation));
        else
            playerSpawns.put(player, new RoomSpawn(location, rotation));
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag tag) {
        return tag;
    }

    private record RoomSpawns(RoomSpawn defaultSpawn, Map<UUID, RoomSpawn> playerSpawnsSnapshot) implements IRoomSpawns {

        @Override
        public Optional<IRoomSpawn> forPlayer(UUID player) {
            return Optional.ofNullable(playerSpawnsSnapshot.get(player));
        }
    }

    private static class Serializer {

        public static SpawnManager loadInto(SpawnManager inst, CompoundTag tag) {
            return inst;
        }
    }
}
