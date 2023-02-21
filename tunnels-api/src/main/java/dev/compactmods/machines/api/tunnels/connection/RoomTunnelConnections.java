package dev.compactmods.machines.api.tunnels.connection;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;

import java.util.Optional;
import java.util.stream.Stream;

public interface RoomTunnelConnections {
    /**
     * Fetches sided redstone tunnel locations inside a machine room, based on connected machine
     * location and the side the tunnel is pointed to.
     * @param machine The connected machine (non-compact level)
     * @param facing The side the tunnels are queried for
     * @return
     */
    Stream<BlockPos> getRedstoneTunnels(GlobalPos machine, Direction facing);

    Optional<Direction> getConnectedSide(BlockPos position);
}
