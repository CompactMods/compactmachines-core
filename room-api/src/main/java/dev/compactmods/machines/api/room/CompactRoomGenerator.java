package dev.compactmods.machines.api.room;

import dev.compactmods.machines.api.util.BlockSpaceUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CompactRoomGenerator {

    /**
     * Generates a wall or platform in a given direction.
     * Uses the solid wall block.
     *
     * @param world
     * @param outerBounds
     * @param wallDirection
     * @since 3.0.0
     */
    public static void generateCompactWall(LevelAccessor world, AABB outerBounds, Direction wallDirection, BlockState block) {
        AABB wallBounds = BlockSpaceUtil.getWallBounds(outerBounds, wallDirection);
        BlockSpaceUtil.blocksInside(wallBounds).forEach(wallBlock -> {
            world.setBlock(wallBlock, block, Block.UPDATE_ALL);
        });
    }

    public static void generateRoom(ServerLevel level, RoomTemplate template, Vec3 roomCenter) {
        var bounds = AABB
                .ofSize(roomCenter, template.dimensions().getX(), template.dimensions().getY(), template.dimensions().getZ())
                .inflate(1);

        generateRoom(level, bounds);
        if (!template.prefillTemplate().equals(RoomTemplate.NO_TEMPLATE)) {
            fillWithTemplate(level, template.prefillTemplate(), template.dimensions(), roomCenter);
        }
    }

    /**
     * Generates a machine "internal" structure in a world via a machine size and a central point.
     *
     * @param world
     * @param outerBounds Outer dimensions of the room.
     */
    public static void generateRoom(LevelAccessor world, AABB outerBounds) {
        final var block = BuiltInRegistries.BLOCK.get(WallConstants.SOLID_WALL);
        if (block != null) {
            final var solidWall = block.defaultBlockState();
            generateRoom(world, outerBounds, solidWall);
        }
    }

    /**
     * Generates a machine structure in a world via machine boundaries and a wall block.
     *
     * @param world
     * @param outerBounds Outer dimensions of the room.
     * @param block Block to use for walls.
     */
    public static void generateRoom(LevelAccessor world, AABB outerBounds, BlockState block) {

        // Generate the walls
        for (final var dir : Direction.values())
            generateCompactWall(world, outerBounds, dir, block);

        // Clear out the inside of the room
        AABB machineInternal = outerBounds.deflate(1);
        BlockSpaceUtil.blocksInside(machineInternal)
                .forEach(p -> world.setBlock(p, Blocks.AIR.defaultBlockState(), 7));
    }

    public static BlockPos cornerFromSize(Vec3i dimensions, Vec3 center) {
        Vec3 offset = new Vec3(
                Math.floor(dimensions.getX() / 2f),
                Math.floor(dimensions.getY() / 2f),
                Math.floor(dimensions.getZ() / 2f)
        );

        return BlockPos.containing(center.subtract(offset));
    }

    public static void fillWithTemplate(ServerLevel level, ResourceLocation template, Vec3i dimensions, Vec3 center) {
        level.getStructureManager().get(template).ifPresent(tem -> {
            BlockPos placeAt = cornerFromSize(dimensions, center);
            tem.placeInWorld(level, placeAt, placeAt, new StructurePlaceSettings(), level.random, Block.UPDATE_ALL);
        });
    }
}
