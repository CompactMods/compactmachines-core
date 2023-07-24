package dev.compactmods.machines.api.room.upgrade;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import static dev.compactmods.machines.api.core.Constants.MOD_ID;

public class RoomUpgrades {

    public static final ResourceKey<Registry<RoomUpgrade>> ROOM_UPGRADES_REG_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "room_upgrades"));

}
