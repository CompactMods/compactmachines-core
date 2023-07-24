package dev.compactmods.machines.api.room.upgrade;

import dev.compactmods.compactmachines.api.room.IRoomInstance;
import net.minecraft.server.level.ServerLevel;

public interface IUpgradeRemovedListener extends RoomUpgrade {

    /**
     * Called when an update is removed from a room.
     */
    default void onRemoved(ServerLevel level, IRoomInstance room) {}
}
