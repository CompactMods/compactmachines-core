package dev.compactmods.machines.room;

import dev.compactmods.compactmachines.api.room.spatial.IRoomArea;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RoomUtil {

    public static Vec3 calculateRoomDefaultSpawn(AABB roomInternal) {
        var newFloorCenter = BlockPos.containing(roomInternal.getCenter()).mutable();
        newFloorCenter.setY((int) (roomInternal.minY + 1));
        return Vec3.atBottomCenterOf(newFloorCenter);
    }

    public static Vec3 calculateRoomDefaultSpawn(IRoomArea roomArea) {
        return calculateRoomDefaultSpawn(roomArea.innerBounds());
    }
}
