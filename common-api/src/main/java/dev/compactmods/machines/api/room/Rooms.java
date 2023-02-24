package dev.compactmods.machines.api.room;

import dev.compactmods.machines.api.core.CMRegistryKeys;
import dev.compactmods.machines.api.upgrade.RoomUpgrade;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import static dev.compactmods.machines.api.core.Constants.MOD_ID;

public class Rooms {

    @Deprecated(forRemoval = true)
    public static final ResourceKey<Registry<RoomTemplate>> TEMPLATE_REG_KEY = CMRegistryKeys.ROOM_TEMPLATES;

    @Deprecated(forRemoval = true)
    public static final ResourceKey<Registry<RoomUpgrade>> ROOM_UPGRADES_REG_KEY = CMRegistryKeys.ROOM_UPGRADES;

    public static Registry<RoomTemplate> getTemplates(MinecraftServer server) {
        final var regAccess = server.registryAccess();
        return regAccess.registryOrThrow(CMRegistryKeys.ROOM_TEMPLATES);
    }
}
