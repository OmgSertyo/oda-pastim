package sertyo.events.module.impl.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.util.MovementInputFromOptions;
import sertyo.events.utility.Utility;

import java.util.UUID;

public class FreeCamera extends ClientPlayerEntity implements Utility {

    private static final ClientPlayNetHandler NETWORK_HANDLER = new ClientPlayNetHandler(Utility.mc, Utility.mc.currentScreen, Utility.mc.getConnection().getNetworkManager(), new GameProfile(UUID.randomUUID(), "<j cby>")) {
        @Override
        public void sendPacket(IPacket<?> packetIn) {
            super.sendPacket(packetIn);
        }
    };

    public FreeCamera(int i) {
        super(Utility.mc, Utility.mc.world, NETWORK_HANDLER, Utility.mc.player.getStats(), Utility.mc.player.getRecipeBook(), false, false);
        
        setEntityId(i);
        movementInput = new MovementInputFromOptions(Utility.mc.gameSettings);
    }

    public void spawn() {
        if (world != null) {
            world.addEntity(this);
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();
    }

    @Override
    public void rotateTowards(double yaw, double pitch) {
        super.rotateTowards(yaw, pitch);
    }
}