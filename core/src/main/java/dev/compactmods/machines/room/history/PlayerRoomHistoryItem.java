package dev.compactmods.machines.room.history;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.location.PreciseDimensionalPosition;
import net.minecraft.core.GlobalPos;

public record PlayerRoomHistoryItem(PreciseDimensionalPosition entry, GlobalPos machine) {

    public static final Codec<PlayerRoomHistoryItem> CODEC = RecordCodecBuilder.create(i -> i.group(
            PreciseDimensionalPosition.CODEC.fieldOf("position").forGetter(PlayerRoomHistoryItem::entry),
            GlobalPos.CODEC.fieldOf("machine").forGetter(PlayerRoomHistoryItem::machine)
    ).apply(i, PlayerRoomHistoryItem::new));
}
