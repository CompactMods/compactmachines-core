package dev.compactmods.compactmachines.api.room;

import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import dev.compactmods.compactmachines.api.room.owner.IRoomOwners;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManagerManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.util.function.Supplier;

import static dev.compactmods.machines.api.core.Constants.MOD_ID;

public class Rooms {

    public static Supplier<IRoomRegistrar> ROOM_LOOKUP;
    public static Supplier<IRoomOwners> OWNER_LOOKUP;
    public static Supplier<IRoomSpawnManagerManager> SPAWN_MANAGER_MANAGER;

    public static Supplier<IRoomChunkManager> CHUNK_MANAGER;

    public static final ResourceKey<Registry<RoomTemplate>> TEMPLATE_REG_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "room_templates"));

    public static IRoomSpawnManager spawnManager(String roomCode) throws NonexistentRoomException {
        return SPAWN_MANAGER_MANAGER.get().manager(roomCode);
    }

    public static Registry<RoomTemplate> getTemplates(MinecraftServer server) {
        final var regAccess = server.registryAccess();
        return regAccess.registryOrThrow(TEMPLATE_REG_KEY);
    }
}
