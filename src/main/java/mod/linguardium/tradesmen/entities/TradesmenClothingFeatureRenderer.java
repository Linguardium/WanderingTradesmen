package mod.linguardium.tradesmen.entities;

import mod.linguardium.tradesmen.api.Trader;
import mod.linguardium.tradesmen.api.TradesmenManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TradesmenClothingFeatureRenderer<T extends TradesmenEntity, M extends EntityModel<T> & ModelWithHat> extends FeatureRenderer<T, M> {
    private static final String[] LEVEL_TO_ID = new String[] {
      "stone",
       "iron",
       "gold",
       "emerald",
       "diamond"
    };
    public TradesmenClothingFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (!livingEntity.isInvisible()) {
            Trader traderData = TradesmenManager.getTraderById(((TradesmenEntity)livingEntity).getTraderType());
            M entityModel = this.getContextModel();
            entityModel.setHatVisible(true); // show head texture changes
            if (!traderData.clothesTextureId.isEmpty()) {
                renderModel(entityModel, new Identifier(traderData.clothesTextureId), matrixStack, vertexConsumerProvider, i, livingEntity, traderData.clothesColor[0], traderData.clothesColor[1], traderData.clothesColor[2]);
            }
            if (!traderData.hatTextureId.isEmpty()) {
                // show hat
                renderModel(entityModel, new Identifier(traderData.hatTextureId), matrixStack, vertexConsumerProvider, i, livingEntity, traderData.hatColor[0], traderData.hatColor[1], traderData.hatColor[2]);
            }
            if (traderData.isTiered) {
                renderModel(entityModel,new Identifier("minecraft:textures/entity/villager/profession_level/"+LEVEL_TO_ID[Math.min(livingEntity.getTraderTier(),LEVEL_TO_ID.length-1)]+".png"),matrixStack, vertexConsumerProvider, i, livingEntity, 1.0F,1.0F,1.0F);
            }
        }
    }

}
