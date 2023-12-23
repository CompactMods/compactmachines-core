package dev.compactmods.compactmachines.api.room;

import dev.compactmods.compactmachines.api.room.owner.IRoomOwners;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.compactmachines.api.room.spatial.IRoomChunks;
import dev.compactmods.compactmachines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.api.dimension.MissingDimensionException;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.UUID;

public class RoomApi {

    /**
     * Set up when the server or single-player instance changes.
     * NOT for API consumers to use! Use the methods provided here for safety.
     *
     * @since 3.0.0
     */
    @ApiStatus.Internal
    public static IRoomApi INSTANCE;

    /**
     * Fetches a room instance from the registrar.
     *
     * @param roomCode The room identifier.
     * @since 3.0.0
     */
    public static Optional<RoomInstance> room(String roomCode) {
        return INSTANCE.registrar().get(roomCode);
    }

    /**
     * Registers a new room instance and generates the structure in the compact world.
     *
     * @param server Server to generate room on.
     * @param template
     * @param owner
     * @return
     */
    public static RoomInstance newRoom(MinecraftServer server, RoomTemplate template, UUID owner) throws MissingDimensionException {
        final var instance = INSTANCE.registrar().createNew(template, owner);
        final var compactDim = CompactDimension.forServer(server);
        CompactRoomGenerator.generateRoom(compactDim, template, instance.boundaries().center());
        return instance;
    }

    public static IRoomRegistrar registrar() {
        return INSTANCE.registrar();
    }

    public static IRoomOwners owners() {
        return INSTANCE.owners();
    }

    public static IRoomSpawnManager spawnManager(String roomCode) {
        return INSTANCE.spawnManager(roomCode);
    }

    public static IRoomChunkManager chunkManager() {
        return INSTANCE.chunkManager();
    }

    public static IRoomChunks chunks(String roomCode) {
        return INSTANCE.chunks(roomCode);
    }

    public static Registry<RoomTemplate> getTemplates(MinecraftServer server) {
        final var regAccess = server.registryAccess();
        return regAccess.registryOrThrow(RoomTemplate.REGISTRY_KEY);
    }
}
