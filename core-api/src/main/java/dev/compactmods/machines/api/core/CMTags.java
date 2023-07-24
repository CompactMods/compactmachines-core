package dev.compactmods.machines.api.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface CMTags {
    /**
     * Marks an item as a room upgrade. Not yet used by the main mod.
     */
    TagKey<Item> ROOM_UPGRADE_ITEM = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Constants.MOD_ID, "room_upgrade"));

    /**
     * Marks an item as a Compact Machine item.
     */
    TagKey<Item> MACHINE_ITEM = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Constants.MOD_ID, "machine"));

    /**
     * Marks a block as a Compact Machine; applied to both legacy sized machines and the new machine blocks.
     */
    TagKey<Block> MACHINE_BLOCK = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Constants.MOD_ID, "machine"));

    /**
     * Marks a block as an unbound Compact Machine; applied only to machines that are not yet bound to a room.
     */
    TagKey<Block> UNBOUND_MACHINE_BLOCK = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Constants.MOD_ID, "new_machine"));

    /**
     * Applied to solid wall items.
     */
    TagKey<Item> SOLID_WALL_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Constants.MOD_ID, "solid_walls"));

    /**
     * Applied to solid walls and tunnel blocks.
     */
    TagKey<Block> SOLID_WALL_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Constants.MOD_ID, "solid_walls"));
}
