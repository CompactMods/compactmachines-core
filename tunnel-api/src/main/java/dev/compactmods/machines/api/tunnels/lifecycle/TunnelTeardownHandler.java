package dev.compactmods.machines.api.tunnels.lifecycle;

import dev.compactmods.machines.api.tunnels.TunnelPosition;
import dev.compactmods.machines.api.tunnels.lifecycle.rotation.ITunnelRotationEventListener;
import dev.compactmods.machines.api.tunnels.lifecycle.removal.ITunnelRemoveEventListener;
import net.minecraft.core.Direction;

/**
 * Indicates that a tunnel has teardown tasks that are performed whenever a tunnel
 * is removed from a machine room's wall, or rotated in-place.
 */
public interface TunnelTeardownHandler<Instance extends TunnelInstance> extends InstancedTunnel<Instance> {

    /**
     * @deprecated Implement {@link ITunnelRotationEventListener}.
     */
    @Deprecated(since = "2.2.0", forRemoval = true)
    default void onRotated(TunnelPosition position, Instance instance, Direction oldSide, Direction newSide) {
    }

    /**
     * @deprecated Implement {@link ITunnelRemoveEventListener}.
     */
    @Deprecated(since = "2.2.0", forRemoval = true)
    default void onRemoved(TunnelPosition position, Instance instance) {
    }
}
