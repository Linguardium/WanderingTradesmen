package mod.linguardium.tradesmen.entities;

import mod.linguardium.tradesmen.api.TradesmenManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TraderOfferList;
import net.minecraft.world.World;

public class TradesmenEntity extends WanderingTraderEntity {
    private static final TrackedData<String> TRADER_TYPE= DataTracker.registerData(TradesmenEntity.class, TrackedDataHandlerRegistry.STRING);;

    public TradesmenEntity(EntityType<? extends WanderingTraderEntity> entityType, World world) {
        super(entityType, world);
    }
    public void setTraderType(String type) {
        this.getDataTracker().set(TRADER_TYPE,type);
    }
    public String getTraderType() {
        return this.getDataTracker().get(TRADER_TYPE);
    }
    public String getTraderAnimal() {
        String animal = "";
        if (!this.getTraderType().isEmpty()) {
            animal = TradesmenManager.getTraderById(this.getTraderType()).animal;
        }
        if (animal.isEmpty()) {
            return "minecraft:trader_llama";
        }
        return animal;
    }

    protected void fillRecipes() {
        TradeOffers.Factory[] factorys = TradesmenManager.getTraderById(this.getTraderType()).TRADES.get(1);
        TradeOffers.Factory[] factorys2 = TradesmenManager.getTraderById(this.getTraderType()).TRADES.get(2);
        if (factorys != null && factorys2 != null) {
            TraderOfferList traderOfferList = this.getOffers();
            if (factorys.length>0) {
                this.fillRecipesFromPool(traderOfferList, factorys, 3);
            }
            if (factorys2.length > 0) {
                int i = this.random.nextInt(factorys2.length);
                TradeOffers.Factory factory = factorys2[i];
                TradeOffer tradeOffer = factory.create(this, this.random);
                if (tradeOffer != null) {
                    traderOfferList.add(tradeOffer);
                }
            }
        }
    }

    @Override
    protected Text getDefaultName() {
        return TradesmenManager.getTraderById(this.getTraderType()).name;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(TRADER_TYPE,"");
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putString("TraderType",this.getTraderType());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.setTraderType(tag.getString("TraderType"));
    }
}
