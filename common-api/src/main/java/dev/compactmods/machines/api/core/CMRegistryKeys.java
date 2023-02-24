package dev.compactmods.machines.api.core;

import dev.compactmods.machines.api.room.RoomTemplate;
import dev.compactmods.machines.api.upgrade.RoomUpgrade;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import static dev.compactmods.machines.api.core.Constants.MOD_ID;

public interface CMRegistryKeys {

    /**
     * Used to store upgrade definitions for rooms (such as chunkloading)
     */
    ResourceKey<Registry<RoomUpgrade>> ROOM_UPGRADES = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "room_upgrades"));

    /**
     * Used to look up and store definitions for room templates.
     */
    ResourceKey<Registry<RoomTemplate>> ROOM_TEMPLATES = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "room_templates"));
}
