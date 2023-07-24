package dev.compactmods.machines.api.tunnels.lifecycle.removal;

import dev.compactmods.machines.api.tunnels.TunnelPosition;
import dev.compactmods.machines.api.tunnels.lifecycle.TunnelInstance;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

/**
 * Used to handle events related to tunnels being removed from Compact Machine walls.
 * @param <T> the tunnel definition's instance type.
 */
public interface ITunnelRemoveEventListener<T extends TunnelInstance> {

    @Nullable
    default ITunnelRemoveEventListener.BeforeRemove createBeforeRemoveHandler(T instance) {
        return null;
    }

    @Nullable
    default ITunnelRemoveEventListener.AfterRemove createAfterRemoveHandler(T instance) {
        return null;
    }

    @FunctionalInterface
    interface BeforeRemove {
        /**
         * Handle tasks when a tunnel is about to be removed from a machine room wall.
         *
         * @param server    Current server instance.
         * @param position  The position of the tunnel in-world.
         * @param reason    The reason the tunnel is being removed.
         *
         * @return Return false to cancel the tunnel being removed.
         */
        boolean beforeRemove(MinecraftServer server, TunnelPosition position, @Nullable ITunnelRemoveReason reason);
    }

    @FunctionalInterface
    interface AfterRemove {
        /**
         * Handle tasks when a tunnel is fully removed from a machine room wall.
         *
         * @param server    Current server instance.
         * @param position  The position of the tunnel in-world.
         * @param reason    The reason the tunnel was removed.
         */
        void afterRemove(MinecraftServer server, TunnelPosition position, @Nullable ITunnelRemoveReason reason);
    }
}
