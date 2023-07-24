package dev.compactmods.machines.api.tunnels;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import static dev.compactmods.machines.api.core.Constants.MOD_ID;

public interface TunnelDefinition {

    ResourceKey<Registry<TunnelDefinition>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "tunnel_types"));

    /**
     * The color of a non-indicator (the same color as the wall)
     */
    int NO_INDICATOR_COLOR = 3751749;

    /**
     * Constant value used to indicate that a tunnel is receiving a resource from
     * outside a machine room.
     */
    int IMPORT_COLOR = 0xff2462cd;

    /**
     * Constant value used to indicate that a tunnel is pushing a resource out of
     * a machine room.
     */
    int EXPORT_COLOR = 0xffe6a709;
    ResourceLocation TUNNEL_ID = new ResourceLocation("compactmachines", "tunnel");

    /**
     * The central ring color of the tunnel. Shown in the tunnel item and on blocks.
     *
     * @return An AARRGGBB-formatted integer indicating color.
     */
    int ringColor();

    /**
     * Gets the color for the indicator at the top-right of the block texture.
     * For import- and export-style tunnels, see {@link #IMPORT_COLOR} and {@link #EXPORT_COLOR}.
     *
     * @return An AARRGGBB-formatted integer indicating color.
     */
    default int indicatorColor() {
        return NO_INDICATOR_COLOR;
    }

}
