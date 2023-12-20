package dev.compactmods.machines.api;

import dev.compactmods.machines.api.core.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public interface CMBlocks {

    ResourceKey<Block> BREAKABLE_WALL = blockKey("wall");
    ResourceKey<Block> SOLID_WALL = blockKey("solid_wall");

    private static ResourceKey<Block> blockKey(String id) {
        return ResourceKey.create(Registries.BLOCK, new ResourceLocation(Constants.MOD_ID, id));
    }
}
