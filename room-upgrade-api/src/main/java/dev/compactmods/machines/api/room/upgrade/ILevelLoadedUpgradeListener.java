package dev.compactmods.machines.api.room.upgrade;

import dev.compactmods.compactmachines.api.room.IRoomInstance;
import net.minecraft.server.level.ServerLevel;

public interface ILevelLoadedUpgradeListener extends RoomUpgrade {

    /**
     * Called when a level is loaded, typically when the server first boots up.
     */
    default void onLevelLoaded(ServerLevel level, IRoomInstance room) {}

    /**
     * Called when a level is unloaded.
     */
    default void onLevelUnloaded(ServerLevel level, IRoomInstance room) {}

}
