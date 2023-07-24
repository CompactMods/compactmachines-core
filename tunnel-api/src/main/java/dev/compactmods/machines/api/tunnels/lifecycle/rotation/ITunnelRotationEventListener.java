package dev.compactmods.machines.api.tunnels.lifecycle.rotation;

import dev.compactmods.machines.api.tunnels.TunnelPosition;
import dev.compactmods.machines.api.tunnels.lifecycle.TunnelInstance;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

/**
 * Used to handle events related to tunnels rotating on Compact Machine walls.
 * @param <T> the tunnel definition's instance type.
 */
public interface ITunnelRotationEventListener<T extends TunnelInstance> {

    @Nullable
    default ITunnelRotationEventListener.BeforeRotation createBeforeRotateHandler(T instance) {
        return null;
    }

    @Nullable
    default ITunnelRotationEventListener.AfterRotation createAfterRotateHandler(T instance) {
        return null;
    }

    @FunctionalInterface
    interface BeforeRotation {

        /**
         * Handle tasks before a tunnel is being rotated on a machine room wall.
         *
         * @param server      Current server instance.
         * @param oldPosition The previous state of the machine the tunnel was connected to.
         * @param newPosition The upcoming state of the machine the tunnel will connect to.
         * @param reason      The reason the tunnel is rotating.
         *
         * @return Return false to cancel the rotation.
         *
         * @since 2.2.0
         */
        boolean beforeRotate(MinecraftServer server, TunnelPosition oldPosition, TunnelPosition newPosition, @Nullable ITunnelRotationReason reason);
    }

    @FunctionalInterface
    interface AfterRotation {

        /**
         * Handle tasks when a tunnel is being rotated on a machine room wall.
         *
         * @param server      Current server instance.
         * @param oldPosition The previous state of the machine the tunnel was connected to.
         * @param newPosition The upcoming state of the machine the tunnel will connect to.
         * @param reason      The reason the tunnel rotated.
         *
         * @since 2.2.0
         */
        void afterRotate(MinecraftServer server, TunnelPosition oldPosition, TunnelPosition newPosition, @Nullable ITunnelRotationReason reason);
    }
}
