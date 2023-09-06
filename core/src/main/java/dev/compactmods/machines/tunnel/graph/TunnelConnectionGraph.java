package dev.compactmods.machines.tunnel.graph;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import dev.compactmods.machines.graph.edge.IGraphEdge;
import dev.compactmods.machines.graph.node.IGraphNode;
import dev.compactmods.machines.machine.graph.node.CompactMachineNode;
import dev.compactmods.machines.graph.edge.GraphEdgeLookupResult;
import dev.compactmods.machines.room.graph.GraphFunctions;
import dev.compactmods.machines.tunnel.graph.edge.TunnelMachineEdge;
import dev.compactmods.machines.tunnel.graph.edge.TunnelTypeEdge;
import dev.compactmods.machines.tunnel.graph.nbt.TunnelGraphNbtSerializer;
import dev.compactmods.machines.tunnel.graph.node.TunnelNode;
import dev.compactmods.machines.tunnel.graph.node.TunnelTypeNode;
import dev.compactmods.machines.graph.GraphTraversalHelper;
import dev.compactmods.machines.tunnel.graph.traversal.ITunnelFilter;
import dev.compactmods.machines.tunnel.graph.traversal.TunnelMachineFilters;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a room's tunnel connections in a graph-style format.
 * This should be accessed through the saved data for specific machine room chunks.
 * <p>
 * Graph Types Used:
 *
 * @see TunnelNode
 * @see TunnelTypeNode
 * @see CompactMachineNode
 * @see TunnelMachineEdge
 * @see TunnelTypeEdge
 */
@SuppressWarnings("UnstableApiUsage")
public class TunnelConnectionGraph extends SavedData {

    /**
     * The full data graph. Contains tunnel nodes, machine ids, and tunnel type information.
     */
    private final MutableValueGraph<IGraphNode<?>, IGraphEdge<?>> graph;

    /**
     * Quick access to tunnel information for specific locations.
     */
    private final Map<BlockPos, TunnelNode> tunnels;

    /**
     * Quick access to machine information nodes.
     */
    private final Map<GlobalPos, CompactMachineNode> machines;

    /**
     * Quick access to tunnel definition nodes.
     */
    private final Map<ResourceLocation, TunnelTypeNode> tunnelTypes;

    private static final Logger LOGS = LogManager.getLogger("tunnel_graph");

    public TunnelConnectionGraph() {
        graph = ValueGraphBuilder
                .directed()
                .build();

        tunnels = new HashMap<>();
        machines = new HashMap<>();
        tunnelTypes = new HashMap<>();
    }

    private TunnelConnectionGraph(CompoundTag nbt) {
        this();
        TunnelGraphNbtSerializer.deserialize(this, nbt);
    }

    public static TunnelConnectionGraph forRoom(ServerLevel compactDim, String room) {
        final var key = getDataFilename(room);
        return compactDim.getDataStorage().computeIfAbsent(
                TunnelConnectionGraph::new,
                TunnelConnectionGraph::new,
                key
        );
    }

    @ApiStatus.Internal
    public MutableValueGraph<IGraphNode<?>, IGraphEdge<?>> graph() {
        return this.graph;
    }

    @NotNull
    @Override
    public CompoundTag save(CompoundTag tag) {
        cleanupOrphans();
        var gData = TunnelGraphNbtSerializer.serialize(this);
        return tag.merge(gData);
    }

    public static String getDataFilename(String room) {
        return "tunnels_" + room;
    }

    private Optional<CompactMachineNode> machineNode(BlockPos tunnel) {
        var tNode = tunnels.get(tunnel);
        return GraphTraversalHelper.successors(this.graph, tNode, CompactMachineNode.class).findFirst();
    }

    private Stream<TunnelNode> tunnelNodes(final ITunnelFilter... filters) {
        return tunnels.values().stream()
                .filter(t -> {
                    for (var filter : filters)
                        if (!filter.filter(this, t))
                            return false;

                    return true;
                });
    }

    /**
     * Finds which machine a tunnel is connected to.
     *
     * @param tunnel The tunnel to find a connection for.
     * @return The id of the connected machine.
     */
    public Optional<GlobalPos> machine(BlockPos tunnel) {
        if (!tunnels.containsKey(tunnel))
            return Optional.empty();

        return machineNode(tunnel).map(CompactMachineNode::dimpos);
    }

    /**
     * Registers a tunnel as being connected to a machine on a particular side.
     * If the tunnel already is registered, this will report a failure.
     *
     * @param tunnelPos The position of the tunnel inside the room.
     * @param type      The type of tunnel being registered.
     * @param machine   The machine the tunnel is to be connected to.
     * @param side      The side of the machine the tunnel is connecting to.
     * @return True if the connection could be established; false if the tunnel type is already registered for the given side.
     */
    @SuppressWarnings("unused,UnusedReturnValue")
    public boolean register(BlockPos tunnelPos, ResourceKey<TunnelDefinition> type, GlobalPos machine, Direction side) {
        // First we need to get the machine the tunnel is trying to connect to
        var machineNode = getOrCreateMachineNode(machine);
        var tunnelNode = getOrCreateTunnelNode(tunnelPos);
        if (graph.hasEdgeConnecting(tunnelNode, machineNode)) {
            // connection already formed between the tunnel at pos and the machine
            LOGS.info("Tunnel already registered for machine {} at position {}.",
                    machine,
                    tunnelPos);

            return false;
        }

        var tunnelTypeNode = getOrCreateTunnelTypeNode(type);

        // no tunnels registered for side yet - free to make new tunnel node
        graph.putEdgeValue(tunnelNode, machineNode, new TunnelMachineEdge(side));
        graph.putEdgeValue(tunnelNode, tunnelTypeNode, new TunnelTypeEdge());

        setDirty();
        return true;
    }

    @NotNull
    protected TunnelNode getOrCreateTunnelNode(BlockPos tunnelPos) {
        if (tunnels.containsKey(tunnelPos))
            return tunnels.get(tunnelPos);

        var newTunnel = new TunnelNode(tunnelPos);
        tunnels.put(tunnelPos, newTunnel);
        graph.addNode(newTunnel);
        setDirty();

        return newTunnel;
    }

    protected CompactMachineNode getOrCreateMachineNode(GlobalPos machine) {
        var machineRegistered = machines.containsKey(machine);
        CompactMachineNode node;
        if (!machineRegistered) {
            node = new CompactMachineNode(machine.dimension(), machine.pos());
            machines.put(machine, node);
            graph.addNode(node);
            setDirty();
        } else {
            node = machines.get(machine);
        }

        return node;
    }

    protected TunnelTypeNode getOrCreateTunnelTypeNode(ResourceKey<TunnelDefinition> definition) {
        final ResourceLocation id = definition.location();

        if (tunnelTypes.containsKey(id))
            return tunnelTypes.get(id);

        TunnelTypeNode newType = new TunnelTypeNode(definition);
        tunnelTypes.put(id, newType);
        graph.addNode(newType);
        setDirty();
        return newType;
    }

    /**
     * Gets the number of registered nodes in the graph.
     */
    public int size() {
        return graph.nodes().size();
    }

    public Optional<Direction> side(BlockPos pos) {
        if (!tunnels.containsKey(pos))
            return Optional.empty();

        var node = tunnels.get(pos);
        return GraphTraversalHelper.edges(this.graph, node, TunnelMachineEdge.class, CompactMachineNode.class)
                .map(tme -> tme.edgeValue().side())
                .findFirst();
    }

    public Optional<TunnelMachineInfo> info(BlockPos tunnel) {
        if (!tunnels.containsKey(tunnel))
            return Optional.empty();

        var node = tunnels.get(tunnel);
        var typeNode = GraphTraversalHelper.successors(this.graph, node, TunnelTypeNode.class)
                .findFirst()
                .orElseThrow();

        var mach = machine(tunnel).orElseThrow();
        var side = side(tunnel).orElseThrow();
        var type = typeNode.key();

        return Optional.of(new TunnelMachineInfo(tunnel, type, mach, side));
    }

    public Stream<TunnelMachineInfo> tunnels() {
        return tunnels.keySet().stream()
                .map(this::info)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Stream<TunnelMachineInfo> tunnels(final ITunnelFilter... filters) {
        return tunnels.values().stream()
                .filter(t -> {
                    for (var filter : filters)
                        if (!filter.filter(this, t))
                            return false;

                    return true;
                })
                .map(TunnelNode::position)
                .map(this::info)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public boolean has(BlockPos location) {
        return tunnels.containsKey(location);
    }

    public Stream<ResourceKey<TunnelDefinition>> types(GlobalPos machine, Direction side) {
        final var node = machines.get(machine);
        if (node == null) return Stream.empty();

        return tunnelNodes(TunnelMachineFilters.sided(machine, side))
                .flatMap(tunnelNode -> GraphTraversalHelper.edges(this.graph, tunnelNode, TunnelTypeEdge.class, TunnelTypeNode.class))
                .map(GraphEdgeLookupResult::target)
                .map(TunnelTypeNode::key)
                .distinct();
    }

    public Stream<Direction> sides(final ITunnelFilter... filters) {
        return tunnelNodes(filters)
                .flatMap(tn -> GraphTraversalHelper.edges(this.graph, tn, TunnelMachineEdge.class))
                .map(edge -> edge.edgeValue().side());
    }

    /**
     * Unlinks a tunnel at a specified point inside the machine room.
     *
     * @param pos Tunnel position inside the room.
     */
    public void unregister(BlockPos pos) {
        if (!has(pos))
            return;

        LOGS.debug("Unregistering tunnel at {}", pos);

        final var existing = tunnels.get(pos);
        graph.removeNode(existing);
        tunnels.remove(pos);

        cleanupOrphanedTypes();
        cleanupOrphanedMachines();

        setDirty();
    }

    private void cleanupOrphans() {
        cleanupOrphanedTunnels();
        cleanupOrphanedTypes();
        cleanupOrphanedMachines();
    }

    private void cleanupOrphanedTypes() {
        HashSet<ResourceLocation> removedTypes = new HashSet<>();
        tunnelTypes.forEach((type, node) -> {
            if (graph.degree(node) == 0) {
                graph.removeNode(node);
                removedTypes.add(type);
            }
        });

        if (!removedTypes.isEmpty()) {
            LOGS.debug("Removed {} tunnel type nodes during cleanup.", removedTypes.size());
            removedTypes.forEach(tunnelTypes::remove);
            setDirty();
        }

    }

    private void cleanupOrphanedTunnels() {
        HashSet<BlockPos> removed = new HashSet<>();
        tunnels.forEach((pos, node) -> {
            if (graph.degree(node) == 0) {
                graph.removeNode(node);
                removed.add(pos);
            }
        });

        if (!removed.isEmpty()) {
            LOGS.debug("Removed {} tunnel nodes during cleanup.", removed.size());
            removed.forEach(tunnels::remove);
            setDirty();
        }
    }

    private void cleanupOrphanedMachines() {
        HashSet<GlobalPos> removed = new HashSet<>();
        machines.forEach((machine, node) -> {
            if (graph.degree(node) == 0) {
                graph.removeNode(node);
                removed.add(machine);
            }
        });

        if (!removed.isEmpty()) {
            LOGS.debug("Removed {} machine nodes during cleanup.", removed.size());
            removed.forEach(machines::remove);
            setDirty();
        }
    }

    public void rotate(BlockPos tunnel, Direction newSide) {
        if (!tunnels.containsKey(tunnel))
            return;

        final var connected = machine(tunnel);
        connected.ifPresent(machine -> {
            if (!machines.containsKey(machine))
                return;

            final var t = tunnels.get(tunnel);
            final var m = machines.get(machine);
            graph.removeEdge(t, m);
            graph.putEdgeValue(t, m, new TunnelMachineEdge(newSide));

            setDirty();
        });
    }

    public Stream<GlobalPos> machines() {
        return this.machines.keySet().stream();
    }

    public Stream<BlockPos> positions(final ITunnelFilter... filters) {
        return tunnels.values().stream()
                .filter(t -> {
                    for (var filter : filters)
                        if (!filter.filter(this, t))
                            return false;

                    return true;
                })
                .map(TunnelNode::position);
    }

    public void rebind(BlockPos tunnel, GlobalPos newMachine, Direction side) {
        LOGS.debug("Rebinding tunnel at {} to machine {}", tunnel, newMachine);

        final var tunnelNode = getOrCreateTunnelNode(tunnel);
        final var newMachineNode = getOrCreateMachineNode(newMachine);
        graph.putEdgeValue(tunnelNode, newMachineNode, new TunnelMachineEdge(side));
        setDirty();
    }
}
