package dev.compactmods.machines.api.room.upgrade;

import dev.compactmods.machines.api.room.RoomInstance;
import net.minecraft.server.level.ServerLevel;

public interface UpgradeRemovedEventListener extends RoomUpgrade {

    /**
     * Called when an update is removed from a room.
     */
    void onRemoved(ServerLevel level, RoomInstance room);
}
