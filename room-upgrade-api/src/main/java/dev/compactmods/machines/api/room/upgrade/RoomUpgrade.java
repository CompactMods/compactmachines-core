package dev.compactmods.machines.api.room.upgrade;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import static dev.compactmods.machines.api.core.Constants.MOD_ID;

public interface RoomUpgrade {

    ResourceKey<Registry<RoomUpgrade>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "room_upgrades"));

    Codec<? extends RoomUpgrade> codec();
}
