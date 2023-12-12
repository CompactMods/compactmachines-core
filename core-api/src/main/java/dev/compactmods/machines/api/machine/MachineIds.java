package dev.compactmods.machines.api.machine;

import dev.compactmods.machines.api.core.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public interface MachineIds {

    ResourceLocation BOUND_MACHINE_ENTITY = new ResourceLocation(Constants.MOD_ID, "machine");

    ResourceLocation UNBOUND_MACHINE_ENTITY = new ResourceLocation(Constants.MOD_ID, "new_machine");
    ResourceLocation UNBOUND_MACHINE_ITEM_ID = new ResourceLocation(Constants.MOD_ID, "new_machine");
    ResourceKey<Item> UNBOUND_MACHINE_ITEM_KEY = ResourceKey.create(Registries.ITEM, UNBOUND_MACHINE_ITEM_ID);
}
