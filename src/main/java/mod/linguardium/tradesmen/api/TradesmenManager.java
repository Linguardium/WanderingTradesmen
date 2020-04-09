package mod.linguardium.tradesmen.api;

import jdk.internal.jline.internal.Nullable;
import mod.linguardium.tradesmen.Tradesmen;
import mod.linguardium.tradesmen.entities.InitEntities;
import mod.linguardium.tradesmen.entities.TradesmenEntity;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import org.apache.logging.log4j.Level;

import java.util.*;

public class TradesmenManager implements WorldTickCallback {
    private HashMap<World, WorldTradesmenManager> WorldManagers = new HashMap<>();
    public static final int SPAWN_DELAY_CONSTANT = 1200; // 24000
    public static final int SPAWN_CHANCE_CONSTANT = 100; // 25
    public static HashMap<String, Trader> Traders = new HashMap<>();
    public TradesmenManager() {
        Tradesmen.log(Level.INFO,"Trader Manager Initialized");
    }
    public static Trader getTraderById(String Id) {
        return Traders.getOrDefault(Id,Traders.get("default:default_trader"));
    }
    private class WorldTradesmenManager {
        private final Random random = new Random();
        private int tickCount;
        private int spawnDelay;
        private int spawnChance;
        private final ServerWorld world;

        public WorldTradesmenManager(ServerWorld world) {
            this.world = world;
            this.tickCount = 1200;
            //LevelProperties levelProperties = world.getLevelProperties();
            this.spawnDelay = TradesmenManager.SPAWN_DELAY_CONSTANT;//levelProperties.getWanderingTraderSpawnDelay();
            this.spawnChance = TradesmenManager.SPAWN_CHANCE_CONSTANT;//levelProperties.getWanderingTraderSpawnChance();
            if (this.spawnDelay == 0 && this.spawnChance == 0) {
                this.spawnDelay = TradesmenManager.SPAWN_DELAY_CONSTANT;
                this.spawnChance = TradesmenManager.SPAWN_CHANCE_CONSTANT;
            }

        }
        public void tick() {

            if (this.world.getGameRules().getBoolean(GameRules.DO_TRADER_SPAWNING) && Traders.size()>1) {
                if (--this.tickCount <= 0) {
                    this.tickCount = 1200;
                    this.spawnDelay -= 1200;
                    if (this.spawnDelay <= 0) {
                        this.spawnDelay = TradesmenManager.SPAWN_DELAY_CONSTANT;
                        if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
                            int i = this.spawnChance;
                            //this.spawnChance = MathHelper.clamp(this.spawnChance + 25, 25, 75);
                            if (this.random.nextInt(100) <= i) {
                                if (this.spawnRoamingTrader()) {
                                    this.spawnChance = TradesmenManager.SPAWN_CHANCE_CONSTANT;
                                }

                            }
                        }
                    }
                }
            }
        }

        private boolean spawnRoamingTrader() {
            PlayerEntity playerEntity = this.world.getRandomAlivePlayer();
            if (playerEntity == null) {
                return true;
            } else {
                BlockPos blockPos = playerEntity.getBlockPos();
                //int i = true;
                PointOfInterestStorage pointOfInterestStorage = this.world.getPointOfInterestStorage();
                Optional<BlockPos> optional = pointOfInterestStorage.getPosition(PointOfInterestType.MEETING.getCompletionCondition(), (blockPosx) -> {
                    return true;
                }, blockPos, 48, PointOfInterestStorage.OccupationStatus.ANY);
                BlockPos blockPos2 = (BlockPos)optional.orElse(blockPos);
                BlockPos blockPos3 = this.getPosDistanceFrom(blockPos2, 48);
                if (blockPos3 != null && this.method_23279(blockPos3)) {
                    if (this.world.getBiome(blockPos3) == Biomes.THE_VOID) {
                        return false;
                    }

                    TradesmenEntity traderEntity = (TradesmenEntity) InitEntities.TRADESMEN_ENTITY_TYPE.spawn(this.world, (CompoundTag)null, (Text)null, (PlayerEntity)null, blockPos3, SpawnType.EVENT, false, false);
                    if (traderEntity != null) {
                        if (Traders.size() > 1) {
                            traderEntity.setTraderType(
                                    Traders.keySet().stream().filter(id -> !("default:default_trader".equalsIgnoreCase(id))).toArray()[world.random.nextInt(Traders.size()-1)].toString()
                            );
                        }
                        this.SpawnAnimal(traderEntity.getTraderAnimal(),traderEntity, 4);

                        //this.world.getLevelProperties().setWanderingTraderId(wanderingTraderEntity.getUuid());
                        traderEntity.setDespawnDelay(SPAWN_DELAY_CONSTANT);
                        traderEntity.setWanderTarget(blockPos2);
                        traderEntity.setPositionTarget(blockPos2, 16);
                        return true;
                    }
                }

                return false;
            }
        }
        private void SpawnAnimal(String AnimalId, LivingEntity ownerTrader, int distance) {
            BlockPos blockPos = this.getPosDistanceFrom(new BlockPos(ownerTrader), distance);
            if (blockPos != null) {
                MobEntity traderAnimalEntity = (MobEntity)(EntityType.get(AnimalId).orElse(EntityType.TRADER_LLAMA)).spawn(this.world, (CompoundTag)null, (Text)null, (PlayerEntity)null, blockPos, SpawnType.EVENT, false, false);
                if (traderAnimalEntity != null) {
                    traderAnimalEntity.attachLeash(ownerTrader, true);
                }
            }
        }
        @Nullable
        private BlockPos getPosDistanceFrom(BlockPos blockPos, int distance) {
            BlockPos blockPos2 = null;

            for(int j = 0; j < 10; ++j) {
                int k = blockPos.getX() + this.random.nextInt(distance * 2) - distance;
                int l = blockPos.getZ() + this.random.nextInt(distance * 2) - distance;
                int m = this.world.getTopY(Heightmap.Type.WORLD_SURFACE, k, l);
                BlockPos blockPos3 = new BlockPos(k, m, l);
                if (SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, this.world, blockPos3, EntityType.WANDERING_TRADER)) {
                    blockPos2 = blockPos3;
                    break;
                }
            }

            return blockPos2;
        }
        private boolean method_23279(BlockPos blockPos) {
            Iterator var2 = BlockPos.iterate(blockPos, blockPos.add(1, 2, 1)).iterator();

            BlockPos blockPos2;
            do {
                if (!var2.hasNext()) {
                    return true;
                }

                blockPos2 = (BlockPos)var2.next();
            } while(this.world.getBlockState(blockPos2).getCollisionShape(this.world, blockPos2).isEmpty());

            return false;
        }

    }

    @Override
    public void tick(World world) {
        if (world.isClient()) {
            return;
        }
        ServerWorld sWorld = (ServerWorld)world;
        if (sWorld.dimension.getType() == DimensionType.OVERWORLD) {
            if (!WorldManagers.containsKey(sWorld)) {
                WorldManagers.put(world, new WorldTradesmenManager(sWorld));
            }
            WorldManagers.get(sWorld).tick();
        }
    }

}
