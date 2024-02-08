package dev.compactmods.machines.machine.client;

import dev.compactmods.machines.api.machine.IColoredMachine;
import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.api.machine.item.ICompactMachineItem;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;

public class MachineColors {

    public static final ItemColor ITEM = (stack, pTintIndex) -> {
        if(!stack.is(MachineConstants.MACHINE_ITEM)) return 0xFFFFFFFF;
        if(stack.getItem() instanceof ICompactMachineItem compactMachineItem)
            return pTintIndex == 0 ? compactMachineItem.getMachineColor(stack) : 0xFFFFFFFF;
        else
            return 0xFFFFFFFF;
    };

    public static final BlockColor BLOCK = (state, level, pos, tintIndex) -> {
        if(!state.is(MachineConstants.MACHINE_BLOCK) || level == null || pos == null)
            return 0xFFFFFFFF;

        if(!(level.getBlockEntity(pos) instanceof IColoredMachine machineData))
            return 0xFFFFFFFF;

        return tintIndex == 0 ? machineData.getColor() : 0xFFFFFFFF;
    };
}
