package mod.linguardium.tradesmen.entities;

import mod.linguardium.tradesmen.Tradesmen;
import mod.linguardium.tradesmen.api.TradesmenManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TraderOfferList;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class TradesmenEntity extends WanderingTraderEntity {
    private static final TrackedData<String> TRADER_TYPE= DataTracker.registerData(TradesmenEntity.class, TrackedDataHandlerRegistry.STRING);;
    private static final TrackedData<String> TRADER_SPAWNED_ANIMAL= DataTracker.registerData(TradesmenEntity.class, TrackedDataHandlerRegistry.STRING);;
    private int despawnDelay;

    public TradesmenEntity(EntityType<? extends WanderingTraderEntity> entityType, World world) {
        super(entityType, world);
    }
    public void setTraderType(String type) {
        this.getDataTracker().set(TRADER_TYPE,type);
    }
    public String getTraderType() {
        return this.getDataTracker().get(TRADER_TYPE);
    }
    public void setAnimalUUID(String id) {
        this.getDataTracker().set(TRADER_SPAWNED_ANIMAL,id);
    }
    public void setAnimalUUID(UUID id) {
        this.getDataTracker().set(TRADER_SPAWNED_ANIMAL,id.toString());
    }
    public String getAnimalIdentifier() {
        return this.getDataTracker().get(TRADER_SPAWNED_ANIMAL);
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
        this.getDataTracker().startTracking(TRADER_SPAWNED_ANIMAL,null);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt("despawnDelay",getDespawnDelay());
        tag.putString("TraderType",this.getTraderType());
        tag.putString("AnimalUUID",this.getAnimalIdentifier());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.setDespawnDelay(tag.getInt("despawnDelay"));
        this.setTraderType(tag.getString("TraderType"));
        this.setAnimalUUID(tag.getString("AnimalUUID"));
    }

    @Override
    public void setDespawnDelay(int delay) {
        this.despawnDelay=delay;
    }

    @Override
    public int getDespawnDelay() {
        return despawnDelay;
    }

    @Override
    public void tickMovement() {
        if (!this.world.isClient) {
            this.tickDespawnDelay();
            super.setDespawnDelay(2000); // effectively disable despawn timer of super. Unsure if its necessary.
        }
        super.tickMovement();
    }

    private void tickDespawnDelay() {
        if (this.despawnDelay > 0 && !this.hasCustomer() && --this.despawnDelay == 0) {
            String animalId = this.getAnimalIdentifier();
            if (Tradesmen.getConfig().despawnAnimals) {
                if (animalId != null && !animalId.isEmpty()) {
                    List<MobEntity> ents = world.getEntities(MobEntity.class,new Box(this.getBlockPos().add(10,10,10),this.getBlockPos().add(-10,-10,-10)),
                            (entity)->entity.getUuidAsString().equals(animalId)
                    );
                    for (MobEntity e : ents) {
                        if (e.getUuidAsString().equals(animalId)) {
                            if (e.getHoldingEntity().equals(this)) {
                                e.detachLeash(true,false);
                               e.remove();
                            }
                        }
                    }
                }
            }
            this.remove();
        }

    }
}
