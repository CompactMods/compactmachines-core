package dev.compactmods.machines.api.room.history;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.location.GlobalPosWithRotation;
import net.minecraft.world.entity.player.Player;

public record RoomEntryPoint(GlobalPosWithRotation entryLocation) {

    public static final RoomEntryPoint INVALID = new RoomEntryPoint(GlobalPosWithRotation.INVALID);

    public static final Codec<RoomEntryPoint> CODEC = RecordCodecBuilder.create(i -> i.group(
            GlobalPosWithRotation.CODEC.fieldOf("entry_location").forGetter(RoomEntryPoint::entryLocation)
    ).apply(i, RoomEntryPoint::new));

    public static RoomEntryPoint fromPlayer(Player player) {
        return new RoomEntryPoint(GlobalPosWithRotation.fromPlayer(player));
    }
}