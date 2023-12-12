package dev.compactmods.machines.api.machine;

import dev.compactmods.machines.api.core.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface MachineTags {

    TagKey<Block> BLOCK = TagKey.create(Registries.BLOCK, new ResourceLocation(Constants.MOD_ID, "machine"));
    TagKey<Item> ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Constants.MOD_ID, "machine"));

}
