package dev.compactmods.machines.tunnel.graph;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.graph.IGraphEdge;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Bridges connection between a tunnel and a given machine side.
 */
public record TunnelMachineEdge(Direction side) implements IGraphEdge<TunnelMachineEdge> {
    private static final ResourceLocation TYPE = new ResourceLocation(Constants.MOD_ID, "tunnel_machine");

    public static final Codec<TunnelMachineEdge> CODEC = RecordCodecBuilder.create(i -> i.group(
            Direction.CODEC.fieldOf("side").forGetter(TunnelMachineEdge::side)
    ).apply(i, TunnelMachineEdge::new));

    @Override
    public String toString() {
        return "TunnelMachineEdge[" +
                "side=" + side + ']';
    }

    @Override
    public @NotNull Codec<TunnelMachineEdge> codec() {
        return CODEC;
    }
}
