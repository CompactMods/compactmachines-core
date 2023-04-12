package dev.compactmods.machines.machine.graph;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.graph.IGraphEdge;
import org.jetbrains.annotations.NotNull;

public record MachineRoomEdge() implements IGraphEdge<MachineRoomEdge> {

    public static final Codec<MachineRoomEdge> CODEC = Codec.unit(MachineRoomEdge::new);

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "MachineRoomEdge[]";
    }

    @NotNull
    @Override
    public Codec<MachineRoomEdge> codec() {
        return CODEC;
    }
}
