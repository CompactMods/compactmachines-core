package dev.compactmods.machines.api.location;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

@Deprecated(forRemoval = true, since = "5.2.0")
public interface IDimensionalBlockPosition extends IDimensionalPosition {
    BlockPos getBlockPosition();

    Optional<BlockEntity> getBlockEntity(MinecraftServer server);

    BlockState state(MinecraftServer server);

    IDimensionalBlockPosition relative(Direction direction);
}
