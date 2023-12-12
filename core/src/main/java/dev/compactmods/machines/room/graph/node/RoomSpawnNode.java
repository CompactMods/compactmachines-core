package dev.compactmods.machines.room.graph.node;

import dev.compactmods.feather.node.Node;
import dev.compactmods.machines.room.spawn.RoomSpawn;

import java.util.UUID;

public final class RoomSpawnNode implements Node.Mutable<RoomSpawn> {

    private final UUID id;
    private RoomSpawn data;

    public RoomSpawnNode(UUID id, RoomSpawn initialSpawn) {
        this.id = id;
        this.data = initialSpawn;
    }

    @Override
    public void setData(RoomSpawn spawnData) {
        this.data = spawnData;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public RoomSpawn data() {
        return data;
    }
}
