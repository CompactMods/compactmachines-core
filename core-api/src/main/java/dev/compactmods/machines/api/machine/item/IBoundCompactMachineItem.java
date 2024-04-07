package dev.compactmods.machines.api.machine.item;

import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.api.machine.block.IBoundCompactMachineBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Optional;

public interface IBoundCompactMachineItem extends ICompactMachineItem {

    String NBT_ROOM_CODE = "room_code";

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

        var blockEntityData = new CompoundTag();
        blockEntityData.putString("id", MachineConstants.BOUND_MACHINE_ENTITY.toString());
        blockEntityData.putString(IBoundCompactMachineBlockEntity.NBT_ROOM_CODE, room);
        addBlockEntityData(stack, blockEntityData);
    }

}
