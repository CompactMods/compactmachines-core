package dev.compactmods.compactmachines.api.room;

import dev.compactmods.machines.api.CMBlocks;
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

    public static AABB getWallBounds(Vec3i size, Vec3 roomCenter, Direction wall) {
        BlockPos cubeFloor = BlockPos.containing(roomCenter).below(Math.floorDiv(size.getY(), 2));
        BlockPos start;

        switch (wall) {
            case NORTH, SOUTH -> {
                int offsetNorthSouth = (int) Math.ceil(size.getZ() / 2f);
                start = cubeFloor.relative(wall,  offsetNorthSouth);

                return AABB.encapsulatingFullBlocks(start, start)
                        .expandTowards(0, size.getY(), 0)
                        .inflate(Math.ceil(size.getX() / 2f), 0, 0);
            }

            case WEST, EAST -> {
                final var offsetWestEast = (int) Math.ceil(size.getX() / 2f);
                start = cubeFloor.relative(wall,  offsetWestEast);

                return AABB.encapsulatingFullBlocks(start, start)
                        .expandTowards(0, size.getY(), 0)
                        .inflate(0, 0, Math.ceil(size.getZ() / 2f));
            }

            case UP, DOWN -> {
                start = wall == Direction.DOWN ? cubeFloor : cubeFloor.relative(wall, size.getY());
                var aabb = AABB.encapsulatingFullBlocks(start, start)
                        .inflate(Math.ceil(size.getX() / 2f), 0, Math.ceil(size.getZ() / 2f));

                if(wall == Direction.UP)
                    aabb = aabb.inflate(0, 1, 0);

                return aabb;
            }
        }

        // catch-all
        return AABB.ofSize(Vec3.ZERO, 0, 0, 0);
    }

    /**
     * Generates a wall or platform in a given direction.
     * Uses the solid wall block.
     *
     * @param world
     * @param dimensions
     * @param cubeCenter
     * @param wallDirection
     *
     * @since 3.0.0
     */
    public static void generateCompactWall(LevelAccessor world, Vec3i dimensions, Vec3 cubeCenter, Direction wallDirection) {
        final var block = BuiltInRegistries.BLOCK.get(CMBlocks.SOLID_WALL);
        if(block != null) {
            final var solidWall = block.defaultBlockState();
            generateCompactWall(world, dimensions, cubeCenter, wallDirection, solidWall);
        }
    }

    /**
     * Generates a wall or platform in a given direction.
     *
     * @param world
     * @param dimensions
     * @param cubeCenter
     * @param wallDirection
     * @param block
     */
    public static void generateCompactWall(LevelAccessor world, Vec3i dimensions, Vec3 cubeCenter, Direction wallDirection, BlockState block) {
        final var wallBounds = getWallBounds(dimensions, cubeCenter, wallDirection);
        BlockPos.betweenClosedStream(wallBounds)
                // .filter(world::isEmptyBlock)
                .map(BlockPos::immutable)
                .forEach(p -> world.setBlock(p, block, Block.UPDATE_ALL));
    }

    public static void generateRoom(ServerLevel level, RoomTemplate template, Vec3 roomCenter) {
        generateRoom(level, template.dimensions(), roomCenter);
        if(!template.prefillTemplate().equals(RoomTemplate.NO_TEMPLATE)) {
            fillWithTemplate(level, template.prefillTemplate(), template.dimensions(), roomCenter);
        }
    }

    /**
     * Generates a machine "internal" structure in a world via a machine size and a central point.
     *
     * @param world
     * @param dimensions Internal dimensions of the room.
     * @param roomCenter
     */
    public static void generateRoom(LevelAccessor world, Vec3i dimensions, Vec3 roomCenter) {
        AABB floorBlocks = getWallBounds(dimensions, roomCenter, Direction.DOWN);
        AABB machineInternal = floorBlocks
                .move(1, 1, 1)
                .contract(2, 0, 2)
                .expandTowards(0, dimensions.getY() - 1, 0);

        boolean anyAir = BlockPos.betweenClosedStream(floorBlocks).anyMatch(world::isEmptyBlock);

        if (anyAir) {
            // Generate the walls
            for(final var dir : Direction.values())
                generateCompactWall(world, dimensions, roomCenter, dir);

            // Clear out the inside of the room
            BlockPos.betweenClosedStream(machineInternal)
                    .forEach(p -> world.setBlock(p, Blocks.AIR.defaultBlockState(), 7));

        }
    }

    /**
     * Generates a machine "internal" structure in a world via a machine size and a central point.
     *
     * @param world
     * @param dimensions Internal dimensions of the room.
     * @param roomCenter
     */
    public static void generateRoom(LevelAccessor world, Vec3i dimensions, Vec3 roomCenter, BlockState block) {
        AABB floorBlocks = getWallBounds(dimensions, roomCenter, Direction.DOWN);
        AABB machineInternal = floorBlocks
                .move(1, 1, 1)
                .contract(2, 0, 2)
                .expandTowards(0, dimensions.getY() - 1, 0);


        boolean anyAir = BlockPos.betweenClosedStream(floorBlocks).anyMatch(world::isEmptyBlock);

        if (anyAir) {
            // Generate the walls
            for(final var dir : Direction.values())
                generateCompactWall(world, dimensions, roomCenter, dir, block);

            // Clear out the inside of the room
            BlockPos.betweenClosedStream(machineInternal)
                    .forEach(p -> world.setBlock(p, Blocks.AIR.defaultBlockState(), 7));

        }
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
