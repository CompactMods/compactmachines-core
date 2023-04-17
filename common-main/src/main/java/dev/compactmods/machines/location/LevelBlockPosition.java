package dev.compactmods.machines.location;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.location.IDimensionalBlockPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

@Deprecated(forRemoval = true, since = "2.2.0")
public record LevelBlockPosition(ResourceKey<Level> dimension, BlockPos blockPos)
        implements IDimensionalBlockPosition {

    public static final Codec<LevelBlockPosition> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResourceKey.codec(Registry.DIMENSION_REGISTRY).fieldOf("dim").forGetter(LevelBlockPosition::dimension),
            BlockPos.CODEC.fieldOf("pos").forGetter(LevelBlockPosition::blockPos)
    ).apply(i, LevelBlockPosition::new));

    public LevelBlockPosition(IDimensionalBlockPosition machine) {
        this(machine.dimension(), machine.getBlockPosition());
    }

    @Override
    public Vec3 position() {
        return Vec3.atCenterOf(blockPos);
    }

    public ServerLevel level(@NotNull MinecraftServer server) {
        return server.getLevel(this.dimension);
    }

    public BlockState state(MinecraftServer server) {
        final var level = level(server);
        return level.getBlockState(getBlockPosition());
    }

    @Override
    public IDimensionalBlockPosition relative(Direction direction) {
        return new LevelBlockPosition(this.dimension, this.blockPos.relative(direction, 1));
    }

    public boolean isLoaded(MinecraftServer server) {
        final var level = level(server);
        return level.isLoaded(blockPos);
    }

    @Override
    public ChunkPos chunkPos() {
        return new ChunkPos(blockPos);
    }

    public static LevelBlockPosition fromNBT(CompoundTag nbt) {
        return CODEC.parse(NbtOps.INSTANCE, nbt).getOrThrow(false, e -> LogManager.getLogger().error(e));
    }

    public BlockPos getBlockPosition() {
        return blockPos.immutable();
    }

    @Override
    public Optional<BlockEntity> getBlockEntity(MinecraftServer server) {
        if(!state(server).hasBlockEntity())
            return Optional.empty();

        return Optional.ofNullable(level(server).getBlockEntity(blockPos));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        LevelBlockPosition that = (LevelBlockPosition) o;
        if (!dimension.equals(that.dimension))
            return false;

        return blockPos.equals(that.blockPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimension, blockPos);
    }

    @Override
    public String toString() {
        return "LevelBlockPosition{d=%s, p=%s}".formatted(dimension, blockPos);
    }
}
