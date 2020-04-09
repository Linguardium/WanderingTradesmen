package mod.linguardium.tradesmen.entities;

import mod.linguardium.tradesmen.api.TradesmenManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.util.Identifier;

public class TradesmenRenderer extends MobEntityRenderer<TradesmenEntity, VillagerResemblingModel<TradesmenEntity>> {

    public TradesmenRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new VillagerResemblingModel<>(1.0F), 1);
    }

    @Override
    public Identifier getTexture(TradesmenEntity entity) {
        if (entity.getTraderType() != null && !TradesmenManager.getTraderById(entity.getTraderType()).textureId.isEmpty())
            return new Identifier(TradesmenManager.getTraderById(entity.getTraderType()).textureId);
        else
            return new Identifier("minecraft:textures/entity/wandering_trader.png");

    }
}
