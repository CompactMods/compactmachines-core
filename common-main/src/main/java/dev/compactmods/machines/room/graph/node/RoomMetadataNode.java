package dev.compactmods.machines.room.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.room.IRoomOwnerLookup;
import dev.compactmods.machines.api.room.registration.IRoomRegistration;
import dev.compactmods.machines.api.room.registration.IRoomSpawnLookup;
import dev.compactmods.machines.codec.CodecExtensions;
import dev.compactmods.machines.graph.node.IGraphNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Hosts core information about a machine room, such as how large it is and its code.
 * @param code
 * @param dimensions
 */
public record RoomMetadataNode(String code, int color, Vec3i dimensions, Vec3 center, Vec3 spawnPosition, Vec2 spawnRotation)
        implements IGraphNode<RoomMetadataNode>, IRoomRegistration {

    public static final Codec<RoomMetadataNode> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("code").forGetter(RoomMetadataNode::code),
            Codec.INT.fieldOf("color").forGetter(RoomMetadataNode::color),
            Vec3i.CODEC.fieldOf("dimensions").forGetter(RoomMetadataNode::dimensions),
            Vec3.CODEC.fieldOf("center").forGetter(RoomMetadataNode::center),
            Vec3.CODEC.fieldOf("spawnPos").forGetter(RoomMetadataNode::spawnPosition),
            CodecExtensions.VEC2.fieldOf("spawnRot").forGetter(RoomMetadataNode::spawnRotation)
    ).apply(i, RoomMetadataNode::new));

    @Override
    public String toString() {
        return "Room Meta [id=%s]".formatted(code);
    }

    @Override
    public UUID owner(IRoomOwnerLookup lookup) {
        return lookup.getRoomOwner(code).orElseThrow();
    }

    @Override
    public AABB innerBounds() {
        return AABB.ofSize(center, dimensions.getX() - 2, dimensions.getY() - 2, dimensions.getZ() - 2);
    }

    @Override
    public AABB outerBounds() {
        return AABB.ofSize(center, dimensions.getX(), dimensions.getY(), dimensions.getZ());
    }

    @Override
    public Vec3 spawnPosition(IRoomSpawnLookup spawns) {
        return spawnPosition;
    }

    @Override
    public Vec2 spawnRotation(IRoomSpawnLookup spawns) {
        return spawnRotation;
    }

    @Override
    public Optional<ResourceLocation> getTemplate() {
        return Optional.empty();
    }

    @Override
    public Stream<ChunkPos> chunks() {
        AABB outerBounds = outerBounds();
        BlockPos min = new BlockPos(outerBounds.minX, outerBounds.minY, outerBounds.minZ);
        BlockPos max = new BlockPos(outerBounds.maxX, outerBounds.maxY, outerBounds.maxZ);

        return ChunkPos.rangeClosed(new ChunkPos(min), new ChunkPos(max));
    }

    @Override
    public @NotNull Codec<RoomMetadataNode> codec() {
        return CODEC;
    }
}
