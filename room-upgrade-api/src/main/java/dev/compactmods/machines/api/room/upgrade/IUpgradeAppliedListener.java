package dev.compactmods.machines.api.room.upgrade;

import dev.compactmods.compactmachines.api.room.IRoomInstance;
import net.minecraft.server.level.ServerLevel;

public interface IUpgradeAppliedListener extends RoomUpgrade {

    /**
     * Called when an upgrade is first applied to a room.
     */
    default void onAdded(ServerLevel level, IRoomInstance room) {}
}
