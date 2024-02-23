package dev.compactmods.machines.api.util;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class AABBHelper {

    public static Vec3 minCorner(AABB aabb) {
        return new Vec3(aabb.minX, aabb.minY, aabb.minZ);
    }

    public static Vec3 maxCorner(AABB aabb) {
        return new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public static AABB normalize(AABB source) {
        Vec3 offset = minCorner(source).reverse();
        return source.move(offset);
    }

    public static AABB normalizeWithin(AABB source, AABB within) {
        Vec3 offset = minCorner(source).subtract(minCorner(within)).reverse();
        return source.move(offset);
    }

    public static AABB alignFloor(AABB source, AABB within) {
        double targetY = within.minY;
        return alignFloor(source, targetY);
    }

    public static AABB alignFloor(AABB source, double targetY) {
        double offset = source.minY - targetY;
        return source.move(0, offset * -1, 0);
    }

    public static String toString(AABB aabb) {
        return "%s,%s,%s".formatted(aabb.getXsize(), aabb.getYsize(), aabb.getZsize());
    }
}
