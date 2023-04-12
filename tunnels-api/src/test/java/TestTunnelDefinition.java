import dev.compactmods.machines.api.tunnels.TunnelDefinition;
import dev.compactmods.machines.api.tunnels.lifecycle.IPlayerLifecycleEventReason;
import dev.compactmods.machines.api.tunnels.lifecycle.InstancedTunnel;
import dev.compactmods.machines.api.tunnels.lifecycle.removal.ITunnelRemoveEventListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.Containers.dropItemStack;

public class TestTunnelDefinition implements TunnelDefinition, InstancedTunnel<TestTunnelInstance>,
        ITunnelRemoveEventListener<TestTunnelInstance> {

    @Override
    public int ringColor() {
        // taken from 5.1's Item Tunnel definition
        return FastColor.ARGB32.color(255, 205, 143, 36);
    }

    @Override
    public TestTunnelInstance newInstance(BlockPos position, Direction side) {
        return new TestTunnelInstance();
    }

    @Override
    public @Nullable ITunnelRemoveEventListener.BeforeRemove createBeforeRemoveHandler(TestTunnelInstance instance) {
        return (server, position, reason) -> {
            if (!(reason instanceof IPlayerLifecycleEventReason playerReason)) {
                // Only allow players to remove this tunnel type
                return false;
            }

            final var player = playerReason.player();
            var dropAt = player.position();

            NonNullList<ItemStack> stacks = NonNullList.create();
            // Fill the list with item stacks from the instance passed in above

            // Drop everything into the machine at the player's feet
            final var level = server.getLevel(Level.OVERWORLD);
            if (level != null) {
                stacks.forEach((itemStack) -> dropItemStack(player.level, dropAt.x, dropAt.y + 0.5d, dropAt.z, itemStack));
            }

            return true;
        };
    }
}
