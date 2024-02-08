package dev.compactmods.machines.api.room.upgrade;

import dev.compactmods.machines.api.room.RoomInstance;
import net.minecraft.server.level.ServerLevel;

public interface LevelLoadedUpgradeEventListener extends RoomUpgrade {

    /**
     * Called when a level is loaded, typically when the server first boots up.
     */
    void onLevelLoaded(ServerLevel level, RoomInstance room);

}
