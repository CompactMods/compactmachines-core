package dev.compactmods.machines.api.machine.item;

import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.api.machine.block.IBoundCompactMachineBlockEntity;
import dev.compactmods.machines.api.machine.block.ICompactMachineBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ICompactMachineItem {

    String NBT_CUSTOM_NAME = "custom_name";

    default Optional<String> getMachineName(ItemStack stack) {
        if(!stack.hasTag()) return Optional.empty();

        final var tag = stack.getTag();
        if(!tag.contains(NBT_CUSTOM_NAME, Tag.TAG_STRING))
            return Optional.empty();

        return Optional.of(tag.getString(NBT_CUSTOM_NAME));
    }


    default void addBlockEntityData(@NotNull ItemStack stack, @NotNull CompoundTag stackTag) {
        final var existingTag = stack.getOrCreateTagElement("BlockEntityTag");
        existingTag.merge(stackTag);
    }
}
