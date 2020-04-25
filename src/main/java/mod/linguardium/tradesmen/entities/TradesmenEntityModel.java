package mod.linguardium.tradesmen.entities;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.util.math.MathHelper;

public class TradesmenEntityModel<T extends Entity> extends CompositeEntityModel<T> implements ModelWithHead, ModelWithHat {
    protected ModelPart head;
    protected ModelPart hat;
    protected final ModelPart hatpart;
    protected final ModelPart torso;
    protected final ModelPart robe;
    protected final ModelPart arms;
    protected final ModelPart rightLeg;
    protected final ModelPart leftLeg;
    protected final ModelPart nose;
    protected final ModelPart nose2;

    public TradesmenEntityModel(float scale) {
        this(scale, 64, 64);
    }

    public TradesmenEntityModel(float scale, int textureWidth, int textureHeight) {
        float f = 0.5F;
        this.head = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.head.setPivot(0.0F, 0.0F, 0.0F);
        this.head.setTextureOffset(0, 0).addCuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, scale);
        this.hat = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.hat.setPivot(0.0F, 0.0F, 0.0F);
        this.hat.setTextureOffset(32, 0).addCuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, scale + 0.5F);
        this.head.addChild(this.hat);
        this.hatpart = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.hatpart.setPivot(0.0F, 0.0F, 0.0F);
        this.hatpart.setTextureOffset(30, 47).addCuboid(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F, scale);
        this.hatpart.pitch = -1.5707964F;
        this.hat.addChild(this.hatpart);
        this.nose = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.nose2 = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.nose2.setPivot(0.0F, -2.0F, 0.0F);
        this.nose2.setTextureOffset(24, 0).addCuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, scale);
        this.nose.setPivot(0.0F, -2.0F, 0.0F);
        this.nose.setTextureOffset(24, 0).addCuboid(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 2.0F, scale);
        this.head.addChild(this.nose);
        this.head.addChild(this.nose2);
        this.torso = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.torso.setPivot(0.0F, 0.0F, 0.0F);
        this.torso.setTextureOffset(16, 20).addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, scale);
        this.robe = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.robe.setPivot(0.0F, 0.0F, 0.0F);
        this.robe.setTextureOffset(0, 38).addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, scale + 0.5F);
        this.torso.addChild(this.robe);
        this.arms = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.arms.setPivot(0.0F, 2.0F, 0.0F);
        this.arms.setTextureOffset(44, 22).addCuboid(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, scale);
        this.arms.setTextureOffset(44, 22).addCuboid(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, scale, true);
        this.arms.setTextureOffset(40, 38).addCuboid(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, scale);
        this.rightLeg = (new ModelPart(this, 0, 22)).setTextureSize(textureWidth, textureHeight);
        this.rightLeg.setPivot(-2.0F, 12.0F, 0.0F);
        this.rightLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale);
        this.leftLeg = (new ModelPart(this, 0, 22)).setTextureSize(textureWidth, textureHeight);
        this.leftLeg.mirror = true;
        this.leftLeg.setPivot(2.0F, 12.0F, 0.0F);
        this.leftLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale);
    }

    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(this.head, this.torso, this.rightLeg, this.leftLeg, this.arms);
    }

    public void setAngles(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {
        boolean stillRollin = false;
        if (entity instanceof AbstractTraderEntity) {
            stillRollin = ((AbstractTraderEntity)entity).getHeadRollingTimeLeft() > 0;
        }

        this.head.yaw = headYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;
        if (stillRollin) {
            this.head.roll = 0.3F * MathHelper.sin(0.45F * customAngle);
            this.head.pitch = 0.4F;
        } else {
            this.head.roll = 0.0F;
        }

        this.arms.pivotY = 3.0F;
        this.arms.pivotZ = -1.0F;
        this.arms.pitch = -0.75F;
        this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance * 0.5F;
        this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance * 0.5F;
        this.rightLeg.yaw = 0.0F;
        this.leftLeg.yaw = 0.0F;
    }

    public ModelPart getHead() {
        return this.head;
    }

    public void setHatVisible(boolean visible) {
        this.head.visible = visible;
        this.hat.visible = visible;
        this.hatpart.visible = visible;
    }
    public void setVanillaNose(boolean enabled) {
        if (enabled) {
            this.nose.visible=false;
            this.nose2.visible=true;
        }else{
            this.nose.visible=true;
            this.nose2.visible=false;
        }
    }
}
