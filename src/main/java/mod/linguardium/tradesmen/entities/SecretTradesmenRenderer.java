package mod.linguardium.tradesmen.entities;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class SecretTradesmenRenderer extends MobEntityRenderer<TradesmenEntity, SecretTradesmenEntityModel<TradesmenEntity>> {

    public SecretTradesmenRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new SecretTradesmenEntityModel<>(0.0F), 0.5F);
        //this.addFeature(new TradesmenClothingFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(TradesmenEntity entity) {
            return new Identifier("tradesmen:textures/entity/black_llaman.png");
    }

}
