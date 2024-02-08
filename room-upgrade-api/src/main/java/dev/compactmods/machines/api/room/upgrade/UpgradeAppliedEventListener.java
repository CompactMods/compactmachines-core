package dev.compactmods.machines.api.room.upgrade;

import dev.compactmods.machines.api.room.RoomInstance;
import net.minecraft.server.level.ServerLevel;

public interface UpgradeAppliedEventListener extends RoomUpgrade {

    /**
     * Called when an upgrade is first applied to a room.
     */
    void onAdded(ServerLevel level, RoomInstance room);
}
