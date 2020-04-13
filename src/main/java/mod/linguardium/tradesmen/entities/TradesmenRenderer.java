package mod.linguardium.tradesmen.entities;

import mod.linguardium.tradesmen.api.TradesmenManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class TradesmenRenderer extends MobEntityRenderer<TradesmenEntity, TradesmenEntityModel<TradesmenEntity>> {

    public TradesmenRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new TradesmenEntityModel<>(0.0F), 0.5F);
        this.addFeature(new TradesmenClothingFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(TradesmenEntity entity) {
        if (entity.getTraderType() != null && !TradesmenManager.getTraderById(entity.getTraderType()).textureId.isEmpty())
            return new Identifier(TradesmenManager.getTraderById(entity.getTraderType()).textureId);
        else
            return new Identifier("tradesmen:textures/entity/skivvies.png");


    }

}
