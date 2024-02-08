package dev.compactmods.machines.api.machine.item;

import dev.compactmods.machines.api.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IUnboundCompactMachineItem extends ICompactMachineItem {

    String NBT_TEMPLATE_ID = "template_id";
    ResourceLocation NO_ROOM_TEMPLATE = new ResourceLocation(Constants.MOD_ID, "empty");

    default ItemStack setTemplate(ItemStack stack, ResourceLocation templateId) {
        var tag = stack.getOrCreateTag();
        tag.putString(NBT_TEMPLATE_ID, templateId.toString());
        return stack;
    }

    @NotNull
    default ResourceLocation getTemplateId(ItemStack stack) {
        if (!stack.hasTag()) return NO_ROOM_TEMPLATE;

        final var tag = stack.getTag();
        if (tag == null || tag.isEmpty() || !tag.contains(NBT_TEMPLATE_ID))
            return NO_ROOM_TEMPLATE;

        return new ResourceLocation(tag.getString(NBT_TEMPLATE_ID));
    }
}
