package dev.compactmods.machines.api.machine.item;

import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface ICompactMachineItem {

    String NBT_COLOR = "machine_color";

    String NBT_CUSTOM_NAME = "custom_name";

    default Optional<String> getMachineName(ItemStack stack) {
        if(!stack.hasTag()) return Optional.empty();

        final var tag = stack.getTag();
        if(!tag.contains(NBT_CUSTOM_NAME, Tag.TAG_STRING))
            return Optional.empty();

        return Optional.of(tag.getString(NBT_CUSTOM_NAME));
    }

    default ItemStack setColor(ItemStack stack, int color) {
        var tag = stack.getOrCreateTag();
        tag.putInt(NBT_COLOR, color);
        return stack;
    }

    default int getMachineColor(ItemStack stack) {
        if (!stack.hasTag()) return 0xFFFFFFFF;

        final var tag = stack.getTag();
        if (tag == null || tag.isEmpty() || !tag.contains(NBT_COLOR))
            return 0xFFFFFFFF;

        return tag.getInt(NBT_COLOR);
    }

}
