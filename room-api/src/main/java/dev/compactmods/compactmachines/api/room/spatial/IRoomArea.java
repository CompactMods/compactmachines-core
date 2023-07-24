package dev.compactmods.compactmachines.api.room.spatial;

import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public interface IRoomArea {

    Vec3i dimensions();

    Vec3 center();

    AABB innerBounds();

    AABB outerBounds();

}
