package dev.compactmods.machines.location;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import dev.compactmods.machines.api.core.Constants;
import dev.compactmods.machines.codec.CodecExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Compatibility codec for reading LevelBlockPosition instances as GlobalPos
 */
public class DimensionalPosCompat implements Codec<GlobalPos> {

    public static final DimensionalPosCompat CODEC = new DimensionalPosCompat();

    private static final Logger LOG = LogManager.getLogger(Constants.MOD_ID);

    /**
     * As stored in LevelBlockPos codec
     */
    private static final Codec<ResourceKey<Level>> DIM_DIMPOS = ResourceKey.codec(Registry.DIMENSION_REGISTRY)
            .fieldOf("dim")
            .codec();

    /**
     * As stored with GlobalPos codec
     */
    private static final Codec<ResourceKey<Level>> DIM_GLOBALPOS = ResourceKey.codec(Registry.DIMENSION_REGISTRY)
            .fieldOf("dimension")
            .codec();

    private static final Decoder<ResourceKey<Level>> DIM_EITHER = Codec.either(DIM_DIMPOS, DIM_GLOBALPOS)
            .map(either -> either.left().orElse(either.right().orElseThrow()));

    private static final Codec<Vec3> POS = CodecExtensions.VECTOR3D.fieldOf("pos").codec();

    @Override
    public <T> DataResult<Pair<GlobalPos, T>> decode(DynamicOps<T> ops, T input) {
        final var dim = DIM_EITHER.parse(ops, input).getOrThrow(false, LOG::error);
        final var pos = POS.parse(ops, input).getOrThrow(false, LOG::error);
        return DataResult.success(Pair.of(GlobalPos.of(dim, new BlockPos(pos)), input));
    }

    @Override
    public <T> DataResult<T> encode(GlobalPos input, DynamicOps<T> ops, T prefix) {
        final var dim = ResourceKey.codec(Registry.DIMENSION_REGISTRY)
                .encodeStart(ops, input.dimension());

        final var pos = CodecExtensions.VECTOR3D
                .encodeStart(ops, Vec3.atBottomCenterOf(input.pos()));

        // Write rot out for other systems maintaining compat
        final var rot = CodecExtensions.VECTOR3D
                .encodeStart(ops, Vec3.ZERO);

        return ops.mapBuilder()
                .add("dimension", dim)
                .add("pos", pos)
                .add("rot", rot)
                .build(prefix);
    }
}
