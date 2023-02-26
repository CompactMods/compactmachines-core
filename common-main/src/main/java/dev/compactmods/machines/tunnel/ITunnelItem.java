package dev.compactmods.machines.tunnel;

import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface ITunnelItem {
    static Optional<ResourceLocation> getDefinition(ItemStack stack) {
        CompoundTag defTag = stack.getOrCreateTagElement("definition");
        if (defTag.isEmpty() || !defTag.contains("id"))
            return Optional.empty();

        return Optional.of(new ResourceLocation(defTag.getString("id")));
    }
}
