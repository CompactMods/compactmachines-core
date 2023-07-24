package dev.compactmods.compactmachines.api.room.history;

import dev.compactmods.machines.api.location.IDimensionalPosition;
import net.minecraft.core.GlobalPos;

public interface IRoomHistoryItem {

    IDimensionalPosition getEntryLocation();

    GlobalPos getMachine();
}
