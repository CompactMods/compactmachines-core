package dev.compactmods.machines.tunnel.graph;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.api.tunnels.connection.RoomTunnelConnections;
import dev.compactmods.machines.graph.IGraphNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public record TunnelNode(BlockPos position) implements IGraphNode<TunnelNode> {
    private static final ResourceLocation TYPE = new ResourceLocation(Constants.MOD_ID, "tunnel");

    public static final Codec<TunnelNode> CODEC = RecordCodecBuilder.create((i) -> i.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(TunnelNode::position)
    ).apply(i, TunnelNode::new));

    @Override
    public String toString() {
        return "TunnelNode[position=%s]".formatted(position);
    }

    public Optional<Direction> getTunnelSide(RoomTunnelConnections connections) {
        return connections.getConnectedSide(position);
    }

    @Override
    public @NotNull Codec<TunnelNode> codec() {
        return CODEC;
    }
}
