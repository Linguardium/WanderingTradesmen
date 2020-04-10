package mod.linguardium.tradesmen.entities;

import mod.linguardium.tradesmen.api.TradesmenManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.client.render.entity.feature.VillagerClothingFeatureRenderer;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.util.Identifier;

public class TradesmenRenderer extends MobEntityRenderer<TradesmenEntity, VillagerResemblingModel<TradesmenEntity>> {

    public TradesmenRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new VillagerResemblingModel<>(1.0F), 1);
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
