package dev.compactmods.machines.util;

import dev.compactmods.machines.codec.CodecExtensions;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NbtUtil {

    private static final Logger LOGS = LogManager.getLogger();

    public static ChunkPos readChunkPos(Tag tag) {
        return CodecExtensions.CHUNKPOS.parse(NbtOps.INSTANCE, tag)
                .getOrThrow(false, LOGS::fatal);
    }
}
