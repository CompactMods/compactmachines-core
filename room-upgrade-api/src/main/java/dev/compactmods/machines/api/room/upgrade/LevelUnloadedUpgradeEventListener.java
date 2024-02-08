package dev.compactmods.machines.api.room.upgrade;

import dev.compactmods.machines.api.room.RoomInstance;
import net.minecraft.server.level.ServerLevel;

public interface LevelUnloadedUpgradeEventListener extends RoomUpgrade {

    /**
     * Called when a level is unloaded.
     */
    void onLevelUnloaded(ServerLevel level, RoomInstance room);
}
