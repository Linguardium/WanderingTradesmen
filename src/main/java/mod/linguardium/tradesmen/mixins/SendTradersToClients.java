package mod.linguardium.tradesmen.mixins;

import mod.linguardium.tradesmen.Tradesmen;
import mod.linguardium.tradesmen.api.TradesmenManager;
import mod.linguardium.tradesmen.network.TradersPacketHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerLoginPacketListener;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class SendTradersToClients implements ServerLoginPacketListener {

    @Inject(method = "onPlayerConnect", at = @At("RETURN"), cancellable = false)
    public void onPlayerLogin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        Tradesmen.log(Level.INFO,"Sending traders to client: "+player.getName().asString());
        TradersPacketHandler.sendClearTraders(player);
        TradesmenManager.Traders.forEach((traderId, t)->{
            if (!traderId.equals("default:default_trader"))
                TradersPacketHandler.sendAddTrader(player,traderId,t);
        });
    }
}