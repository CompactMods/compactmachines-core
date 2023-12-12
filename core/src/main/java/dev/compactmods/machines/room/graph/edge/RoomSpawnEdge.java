package dev.compactmods.machines.room.graph.edge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.feather.edge.GraphValueEdge;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import dev.compactmods.machines.room.graph.node.RoomSpawnNode;
import net.minecraft.core.UUIDUtil;

import java.lang.ref.WeakReference;
import java.util.UUID;

public record RoomSpawnEdge(WeakReference<RoomRegistrationNode> source, WeakReference<RoomSpawnNode> target, EdgeData value)
        implements GraphValueEdge<RoomRegistrationNode, RoomSpawnNode, RoomSpawnEdge.EdgeData> {

    public record EdgeData(UUID player) {

        public static final Codec<EdgeData> CODEC = RecordCodecBuilder.create(i -> i.group(
                UUIDUtil.CODEC.fieldOf("player").forGetter(EdgeData::player)
        ).apply(i, EdgeData::new));
    }
}