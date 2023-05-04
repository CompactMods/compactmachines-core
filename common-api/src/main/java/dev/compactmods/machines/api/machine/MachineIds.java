package dev.compactmods.machines.api.machine;

import dev.compactmods.machines.api.core.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public interface MachineIds {

    /**
     * @deprecated Machines will now use either {@link #BOUND_MACHINE_ENTITY} or {@link #UNBOUND_MACHINE_ENTITY}.
     */
    @Deprecated(forRemoval = true, since = "5.2.0")
    ResourceLocation OLD_MACHINE_ENTITY = new ResourceLocation(Constants.MOD_ID, "compact_machine");

    /**
     * @deprecated Machines will now use either {@link #BOUND_MACHINE_ENTITY} or {@link #UNBOUND_MACHINE_ENTITY}.
     */
    @Deprecated(forRemoval = true, since = "5.2.0")
    ResourceLocation BLOCK_ENTITY = new ResourceLocation(Constants.MOD_ID, "compact_machine");


    ResourceLocation BOUND_MACHINE_ENTITY = new ResourceLocation(Constants.MOD_ID, "machine");
    ResourceLocation UNBOUND_MACHINE_ENTITY = new ResourceLocation(Constants.MOD_ID, "new_machine");

    ResourceLocation UNBOUND_MACHINE_ITEM_ID = new ResourceLocation(Constants.MOD_ID, "new_machine");
    ResourceKey<Item> UNBOUND_MACHINE_ITEM_KEY = ResourceKey.create(Registry.ITEM_REGISTRY, UNBOUND_MACHINE_ITEM_ID);
}
