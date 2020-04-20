package mod.linguardium.tradesmen;

import mod.linguardium.tradesmen.entities.InitEntities;
import mod.linguardium.tradesmen.entities.SecretTradesmenRenderer;
import mod.linguardium.tradesmen.entities.TradesmenRenderer;
import mod.linguardium.tradesmen.network.TradersPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class TradesmenClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(InitEntities.TRADESMEN_ENTITY_TYPE, (entityRenderDispatcher, context) -> new TradesmenRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(InitEntities.SECRET_TRADESMEN_ENTITY_TYPE, (entityRenderDispatcher, context) -> new SecretTradesmenRenderer(entityRenderDispatcher));
        TradersPacketHandler.init();
    }
}