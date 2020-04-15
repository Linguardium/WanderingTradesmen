package mod.linguardium.tradesmen.entities;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.util.math.MathHelper;

public class SecretTradesmenEntityModel<T extends Entity> extends CompositeEntityModel<T> implements ModelWithHead, ModelWithHat {
    protected ModelPart head=null;
    protected  ModelPart man_torso=null;
    protected  ModelPart arms=null;
    protected  ModelPart torso=null;
    private  ModelPart rightBackLeg=null;
    private  ModelPart leftBackLeg=null;
    private  ModelPart rightFrontLeg=null;
    private  ModelPart leftFrontLeg=null;
    protected  ModelPart nose=null;
    private  ModelPart rightChest=null;
    private  ModelPart leftChest=null;

    public SecretTradesmenEntityModel(float scale) {
        this(scale, 128, 64);
    }

    public SecretTradesmenEntityModel(float scale, int textureWidth, int textureHeight) {
        float f = 0.7F;
        this.head = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.head.setPivot(0.0F, -10.0F, -5.0F);
        this.head.setTextureOffset(0, 0).addCuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, scale);
        this.nose = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.nose.setPivot(0.0F, -2.0F, 0.0F);
        this.nose.setTextureOffset(24, 0).addCuboid(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 2.0F, scale);
        this.head.addChild(this.nose);
        this.man_torso = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.man_torso.setPivot(0.0F, -7.1F, 15.0F);
        // Y = front/back
        // Z = Height
        // X = Left/right
        this.man_torso.setTextureOffset(16, 20).addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, scale);
        this.man_torso.pitch=-1.5707964F;
/*        this.robe = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.robe.setPivot(0.0F, 0.0F, 0.0F);
        this.robe.setTextureOffset(0, 38).addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, scale + 0.5F);
        this.man_torso.addChild(this.robe);*/
        this.arms = (new ModelPart(this)).setTextureSize(textureWidth, textureHeight);
        this.arms.setTextureOffset(0, 40).addCuboid(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, scale);
        this.arms.setTextureOffset(0, 40).addCuboid(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, scale, true);
        this.arms.setTextureOffset(18, 40).addCuboid(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, scale);
        this.arms.setPivot(0.0F, -7.0F, -6.0F);
        this.torso = new ModelPart(this, 79, 0).setTextureSize(textureWidth,textureHeight);
        this.torso.addCuboid(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, f);
        this.torso.setPivot(0.0F, 5.0F, 2.0F);
        this.torso.pitch=1.5707964F;
        this.torso.addChild(man_torso);
        this.rightChest = new ModelPart(this, 45, 28).setTextureSize(textureWidth,textureHeight);
        this.rightChest.addCuboid(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, f);
        this.rightChest.setPivot(-8.5F, 3.0F, 3.0F);
        this.rightChest.yaw = 1.5707964F;
        this.leftChest = new ModelPart(this, 45, 41).setTextureSize(textureWidth,textureHeight);
        this.leftChest.addCuboid(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, f);
        this.leftChest.setPivot(5.5F, 3.0F, 3.0F);
        this.leftChest.yaw = 1.5707964F;
        this.rightBackLeg = new ModelPart(this, 79, 29).setTextureSize(textureWidth,textureHeight);
        this.rightBackLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, f);
        this.rightBackLeg.setPivot(-2.5F, 10.0F, 6.0F);
        this.leftBackLeg = new ModelPart(this, 79, 29).setTextureSize(textureWidth,textureHeight);
        this.leftBackLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, f);
        this.leftBackLeg.setPivot(2.5F, 10.0F, 6.0F);
        this.rightFrontLeg = new ModelPart(this, 79, 29).setTextureSize(textureWidth,textureHeight);
        this.rightFrontLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, f);
        this.rightFrontLeg.setPivot(-2.5F, 10.0F, -4.0F);
        this.leftFrontLeg = new ModelPart(this, 79, 29).setTextureSize(textureWidth,textureHeight);
        this.leftFrontLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, f);
        this.leftFrontLeg.setPivot(2.5F, 10.0F, -4.0F);
        --this.rightBackLeg.pivotX;
        ++this.leftBackLeg.pivotX;
        --this.rightFrontLeg.pivotX;
        ++this.leftFrontLeg.pivotX;
        --this.rightFrontLeg.pivotZ;
        --this.leftFrontLeg.pivotZ;
    }

    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(this.head, this.torso, this.arms, this.rightBackLeg, this.leftBackLeg, this.rightFrontLeg, this.leftFrontLeg, this.rightChest, this.leftChest);
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
        this.arms.pitch = -0.75F;
        this.rightBackLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.leftBackLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
        this.rightFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
        this.leftFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.rightChest.visible = true;
        this.leftChest.visible = true;
    }

    public ModelPart getHead() {
        return this.head;
    }

    public void setHatVisible(boolean visible) {
        this.head.visible = visible;
    }
}
