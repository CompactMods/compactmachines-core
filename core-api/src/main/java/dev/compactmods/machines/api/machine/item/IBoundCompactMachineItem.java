package dev.compactmods.machines.api.machine.item;

import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface IBoundCompactMachineItem extends ICompactMachineItem {

    public static final String NBT_ROOM_CODE = "room_code";

    default Optional<String> getRoom(ItemStack stack) {
        if (!stack.hasTag())
            return Optional.empty();

        var tag = stack.getTag();
        if (tag == null || !tag.contains(NBT_ROOM_CODE))
            return Optional.empty();

        return Optional.of(tag.getString(NBT_ROOM_CODE));
    }

    default void setRoom(ItemStack stack, String room) {
        var tag = stack.getOrCreateTag();
        tag.putString(NBT_ROOM_CODE, room);
    }

}
