package dev.compactmods.compactmachines.api.room;

import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;
import dev.compactmods.compactmachines.api.room.owner.IRoomOwners;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManagers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import static dev.compactmods.machines.api.core.Constants.MOD_ID;

public class Rooms {

    private static IRoomRegistrar ROOM_REGISTRY;
    private static IRoomOwners OWNER_REGISTRY;
    private static IRoomSpawnManagers SPAWN_MANAGERS;
    private static IRoomChunkManager CHUNK_MANAGER;

    public static final ResourceKey<Registry<RoomTemplate>> TEMPLATE_REG_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "room_templates"));

    public static IRoomRegistrar registrar() {
        return ROOM_REGISTRY;
    }

    public static IRoomOwners owners() {
        return OWNER_REGISTRY;
    }

    public static IRoomSpawnManager spawnManager(String roomCode) throws NonexistentRoomException {
        return SPAWN_MANAGERS.get(roomCode);
    }

    public static IRoomChunkManager chunkManager() {
        return CHUNK_MANAGER;
    }

    public static Registry<RoomTemplate> getTemplates(MinecraftServer server) {
        final var regAccess = server.registryAccess();
        return regAccess.registryOrThrow(TEMPLATE_REG_KEY);
    }
}
