package dev.compactmods.machines.machine.graph;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.LoggingUtil;
import dev.compactmods.machines.graph.GraphAdjacentNodeStream;
import dev.compactmods.machines.machine.graph.edge.MachineRoomEdge;
import dev.compactmods.machines.machine.graph.node.CompactMachineNode;
import dev.compactmods.machines.room.graph.GraphFunctions;
import dev.compactmods.machines.room.graph.MemoryGraph;
import dev.compactmods.machines.room.graph.node.RoomReferenceNode;
import dev.compactmods.machines.graph.GraphTraversalHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stores information on how external machines connect to the rooms in the compact machine
 * dimension. Per-dimension since 4.3.0.
 */
@SuppressWarnings("UnstableApiUsage")
public class DimensionMachineGraph extends SavedData {

    private static final Logger LOG = LoggingUtil.modLog();

    private final ResourceKey<Level> level;
    private final MemoryGraph graph;
    private final Map<BlockPos, CompactMachineNode> machines;
    private final Map<String, RoomReferenceNode> rooms;

    public static final String DATA_KEY = "machine_connections";
    private final Codec<List<CompactMachineConnectionInfo>> CONN_CODEC = CompactMachineConnectionInfo.CODEC
            .listOf()
            .fieldOf("connections")
            .codec();

    private DimensionMachineGraph(ResourceKey<Level> level) {
        this.level = level;
        graph = new MemoryGraph();
        machines = new HashMap<>();
        rooms = new HashMap<>();
    }

    private DimensionMachineGraph(ResourceKey<Level> level, @NotNull CompoundTag nbt) {
        this(level);

        if (nbt.contains("graph")) {
            CompoundTag graphNbt = nbt.getCompound("graph");

            final var connectionData = CONN_CODEC.parse(NbtOps.INSTANCE, graphNbt)
                    .resultOrPartial(LOG::error)
                    .orElseThrow();

            for (CompactMachineConnectionInfo i : connectionData) {
                registerRoom(i.roomCode);
                for (var connectedMachine : i.machines()) {
                    register(connectedMachine, i.roomCode);
                }
            }
        }
    }

    public static DimensionMachineGraph forDimension(ServerLevel dimension) {
        final var dimStore = dimension.getDataStorage();
        return dimStore.computeIfAbsent(tag -> new DimensionMachineGraph(dimension.dimension(), tag),
                () -> new DimensionMachineGraph(dimension.dimension()), DATA_KEY);
    }

    private List<CompactMachineConnectionInfo> buildConnections() {
        List<CompactMachineConnectionInfo> result = new ArrayList<>();
        this.rooms.forEach((roomCode, node) -> {
            final var machines = this.connectedMachines(roomCode);
            CompactMachineConnectionInfo roomInfo = new CompactMachineConnectionInfo(roomCode, ImmutableList.copyOf(machines));
            result.add(roomInfo);
        });

        return result;
    }

    private void registerMachine(BlockPos machine) {
        if (this.machines.containsKey(machine))
            return;

        CompactMachineNode node = new CompactMachineNode(this.level, machine);
        graph.addNode(node);
        machines.put(machine, node);


        this.setDirty();
    }

    private void registerRoom(String roomCode) {
        if (this.rooms.containsKey(roomCode))
            return;

        var node = new RoomReferenceNode(roomCode);
        graph.addNode(node);
        rooms.put(roomCode, node);

        this.setDirty();
    }

    public void register(BlockPos machine, String room) {
        if (!machines.containsKey(machine))
            registerMachine(machine);

        if (!rooms.containsKey(room))
            registerRoom(room);

        var machineNode = machines.get(machine);
        var roomNode = rooms.get(room);

        graph.putEdgeValue(machineNode, roomNode, new MachineRoomEdge());

        this.setDirty();
    }

    public Stream<BlockPos> machines() {
        return machines.keySet().stream();
    }

    public Stream<String> rooms() {
        return rooms.keySet().stream();
    }

    private static final GraphAdjacentNodeStream<RoomReferenceNode, CompactMachineNode> FIND_MACHINE_NODE =
            (graph, in) -> GraphTraversalHelper.predecessors(graph, in, CompactMachineNode.class);

    public Set<BlockPos> connectedMachines(String room) {
        if (!rooms.containsKey(room))
            return Collections.emptySet();

        var roomNode = this.rooms.get(room);
        return graph.adjacentNodes(FIND_MACHINE_NODE, roomNode)
                .map(CompactMachineNode::position)
                .collect(Collectors.toSet());
    }

    public Optional<String> connectedRoom(BlockPos machinePos) {
        if (!this.machines.containsKey(machinePos))
            return Optional.empty();

        var machineNode = this.machines.get(machinePos);
        return graph.scalar(GraphFunctions.MACHINE_TO_ROOM_CODE, machineNode);
    }

    public void unregisterRoom(String room) {
        if (!this.rooms.containsKey(room))
            return;

        graph.removeNode(rooms.get(room));
        rooms.remove(room);

        setDirty();
    }

    /**
     * Removes a machine from the connection graph.
     * If the machine was connected to a room, returns the previously connected room code.
     *
     * @param machine The machine to unregister.
     *
     * @return An optional, previously-connected room code.
     */
    public Optional<String> unregisterMachine(BlockPos machine) {
        if (!machines.containsKey(machine))
            return Optional.empty();

        final var machineNode = machines.get(machine);
        final var prevConnectedRoom = graph.scalar(GraphFunctions.MACHINE_TO_ROOM_CODE, machineNode);

        graph.removeNode(machineNode);
        machines.remove(machine);

        setDirty();

        return prevConnectedRoom;
    }

    @NotNull
    @Override
    public CompoundTag save(@NotNull CompoundTag nbt) {
        final var connData = buildConnections();

        CONN_CODEC.encodeStart(NbtOps.INSTANCE, connData)
                .resultOrPartial(LOG::error)
                .ifPresent(gNbt -> nbt.put("graph", gNbt));

        return nbt;
    }

    /**
     * Data structure for serialization. Do not use directly.
     */
    private record CompactMachineConnectionInfo(String roomCode, List<BlockPos> machines) {
        public static final Codec<CompactMachineConnectionInfo> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING
                        .fieldOf("room")
                        .forGetter(CompactMachineConnectionInfo::roomCode),

                BlockPos.CODEC.listOf()
                        .fieldOf("machines")
                        .forGetter(CompactMachineConnectionInfo::machines)
        ).apply(i, CompactMachineConnectionInfo::new));

    }
}
