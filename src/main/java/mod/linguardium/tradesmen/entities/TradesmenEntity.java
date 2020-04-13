package mod.linguardium.tradesmen.entities;

import jdk.internal.jline.internal.Nullable;
import mod.linguardium.tradesmen.Tradesmen;
import mod.linguardium.tradesmen.api.TradesmenManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TraderOfferList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TradesmenEntity extends WanderingTraderEntity {
    private static final TrackedData<String> TRADER_TYPE= DataTracker.registerData(TradesmenEntity.class, TrackedDataHandlerRegistry.STRING);;
    private static final TrackedData<Integer> TRADER_TIER = DataTracker.registerData(TradesmenEntity.class, TrackedDataHandlerRegistry.INTEGER);;
    private static final TrackedData<CompoundTag> TRADER_SPAWNED_ANIMAL= DataTracker.registerData(TradesmenEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);;
    private int despawnDelay;
    @Nullable
    private BlockPos wanderTarget;

    public TradesmenEntity(EntityType<? extends WanderingTraderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0,new HoldInHandsGoal(this, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.INVISIBILITY), SoundEvents.ENTITY_WANDERING_TRADER_DISAPPEARED, (wanderingTraderEntity) -> {
            return !this.isInvulnerable() && (this.world.dimension.hasVisibleSky() && !this.world.isDay()) && !((Entity)wanderingTraderEntity).isInvisible();
        }));
        this.goalSelector.add(0, new HoldInHandsGoal(this, new ItemStack(Items.MILK_BUCKET), SoundEvents.ENTITY_WANDERING_TRADER_REAPPEARED, (wanderingTraderEntity) -> {
            return (this.world.dimension.hasVisibleSky() && this.world.isDay()) && ((Entity)wanderingTraderEntity).isInvisible();
        }));
        this.goalSelector.add(1, new StopFollowingCustomerGoal(this));
        // I really hate identifying something programatically. Maybe i should add a config for neutralizing mob entities...
        //TODO: figure this hostile/neutral mob shit out
        this.goalSelector.add(1, new TradesmenEntity.FleeFromMob(this, HostileEntity.class, 8.0F, (entity)->{return !(entity instanceof ZombiePigmanEntity);}));
        this.goalSelector.add(1, new TradesmenEntity.FleeFromMob(this, HostileEntity.class, 15.0F, (entity)-> {return (entity instanceof RangedAttackMob);}));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 0.5D));
        this.goalSelector.add(1, new LookAtCustomerGoal(this));
        this.goalSelector.add(2, new TradesmenEntity.WanderToTargetGoal(this, 2.0D, 0.35D));
        this.goalSelector.add(4, new GoToWalkTargetGoal(this, 0.35D));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 0.35D));
        this.goalSelector.add(9, new GoToEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));

    }

    public void setTraderType(String type) {
        this.getDataTracker().set(TRADER_TYPE,type);
    }
    public Integer getTraderTier() {
        return this.getDataTracker().get(TRADER_TIER);
    }
    public void setTraderTier(Integer tier) {
        this.getDataTracker().set(TRADER_TIER,tier);
    }
    public String getTraderType() {
        return this.getDataTracker().get(TRADER_TYPE);
    }
    public String getNextAvailable(CompoundTag tag, String id) {
        int i=0;
        while (tag.contains(id+String.valueOf(i))) {
            i++;
        }
        return (id+String.valueOf(i));
    }
    public String getLastOf(CompoundTag tag, String id) {
        if (!tag.contains(id+String.valueOf(0))) {
            return "";
        }
        int i=0;
        while (tag.contains(id+String.valueOf(i))) {
            i++;
        }
        return (id+String.valueOf(i-1));
    }
    public void addAnimalUUID(String id) {
        CompoundTag tag = this.getDataTracker().get(TRADER_SPAWNED_ANIMAL).copy();
        ListTag animalTag = tag.getList("traderAnimals",8); // StringTag
        animalTag.add(StringTag.of(id));
        tag.put("traderAnimals",animalTag);
        this.getDataTracker().set(TRADER_SPAWNED_ANIMAL,tag);
    }
    public List<String> getAnimalIdentifiers() {
        List<String> list = new ArrayList<>();
        CompoundTag tag = this.getDataTracker().get(TRADER_SPAWNED_ANIMAL).copy();
        ListTag listTag = tag.getList("traderAnimals", 8); // StringTag
        listTag.forEach((sUUID)->{
            list.add(((StringTag)sUUID).asString());
        });
        return list;
    }
    public String getTraderAnimal() {
        String animal = "";
        if (!this.getTraderType().isEmpty()) {
            animal = TradesmenManager.getTraderById(this.getTraderType()).animal;
        }
        return animal;
    }

    protected void fillRecipes() {
        List<List<TradeOffers.Factory>> trades = TradesmenManager.getTraderById(this.getTraderType()).TRADES;
        List<Integer> tradeCount = TradesmenManager.getTraderById(this.getTraderType()).tierTradeCount;
        if (trades.size() > 0) {
            TraderOfferList traderOfferList = this.getOffers();
            for (int tier=0;tier<trades.size() && tier < tradeCount.size() && (tier<=this.getTraderTier() || !TradesmenManager.getTraderById(this.getTraderType()).isTiered) ;tier++)
            {
                // get tradeCount[tier] unique random numbers into an int[] array
                int[] tradeNums = random.ints(100,0, trades.get(tier).size() ).distinct().limit(tradeCount.get(tier)).toArray();
                for (int c = 0; c<tradeCount.get(tier) && c<tradeNums.length; c++) {
                    TradeOffer tradeOffer = trades.get(tier).get(tradeNums[c]).create(this, this.random);
                    if (tradeOffer!=null) {
                        traderOfferList.add(tradeOffer);
                    }
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
        this.getDataTracker().startTracking(TRADER_SPAWNED_ANIMAL,new CompoundTag());
        this.getDataTracker().startTracking(TRADER_TIER,this.world.random.nextInt(5));
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt("despawnDelay",getDespawnDelay());
        tag.putString("TraderType",this.getTraderType());
        tag.put("AnimalUUID",this.getDataTracker().get(TRADER_SPAWNED_ANIMAL));
        tag.putInt("TraderTier",this.getTraderTier());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.setDespawnDelay(tag.getInt("despawnDelay"));
        this.setTraderType(tag.getString("TraderType"));
        this.setTraderTier(tag.getInt("TraderTier"));
        this.getDataTracker().set(TRADER_SPAWNED_ANIMAL,tag.getCompound("AnimalUUID"));
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
                List<String> animalIDs = this.getAnimalIdentifiers();
                if (!animalIDs.isEmpty()) {
                    List<MobEntity> ents = world.getEntities(MobEntity.class,new Box(this.getBlockPos().add(10,10,10),this.getBlockPos().add(-10,-10,-10)),
                            (entity)->animalIDs.contains(entity.getUuidAsString())
                    );
                    for (MobEntity e : ents) {
                        if (e != null && e.getHoldingEntity() != null && e.getHoldingEntity().equals(this)) {
                            e.detachLeash(true,false);
                            if (Tradesmen.getConfig().despawnAnimals) {
                                e.remove();
                            }
                        }
                    }
                }
            this.remove();
        }

    }

    @Nullable
    private BlockPos getWanderTarget() {
        return this.wanderTarget;
    }

    class WanderToTargetGoal extends Goal {
        final TradesmenEntity trader;
        final double proximityDistance;
        final double speed;

        WanderToTargetGoal(TradesmenEntity trader, double proximityDistance, double speed) {
            this.trader = trader;
            this.proximityDistance = proximityDistance;
            this.speed = speed;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public void stop() {
            this.trader.setWanderTarget((BlockPos)null);
            TradesmenEntity.this.navigation.stop();
        }

        public boolean canStart() {
            BlockPos blockPos = this.trader.getWanderTarget();
            return blockPos != null && this.isTooFarFrom(blockPos, this.proximityDistance);
        }

        public void tick() {
            BlockPos blockPos = this.trader.getWanderTarget();
            if (blockPos != null && TradesmenEntity.this.navigation.isIdle()) {
                if (this.isTooFarFrom(blockPos, 10.0D)) {
                    Vec3d vec3d = (new Vec3d((double)blockPos.getX() - this.trader.getX(), (double)blockPos.getY() - this.trader.getY(), (double)blockPos.getZ() - this.trader.getZ())).normalize();
                    Vec3d vec3d2 = vec3d.multiply(10.0D).add(this.trader.getX(), this.trader.getY(), this.trader.getZ());
                    TradesmenEntity.this.navigation.startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, this.speed);
                } else {
                    TradesmenEntity.this.navigation.startMovingTo((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), this.speed);
                }
            }

        }

        private boolean isTooFarFrom(BlockPos pos, double proximityDistance) {
            return !pos.isWithinDistance(this.trader.getPos(), proximityDistance);
        }
    }
    private class FleeFromMob extends FleeEntityGoal {

        public FleeFromMob(MobEntityWithAi fleeingEntity, Class classToFleeFrom, float fleeDistance, Predicate inclusionSelector) {
            super(fleeingEntity, classToFleeFrom, fleeDistance, 0.5F, 0.5F, inclusionSelector);
        }

        @Override
        public boolean canStart() {
            return !mob.isInvulnerable() && !mob.isInvisible() && super.canStart();
        }
    }
}
