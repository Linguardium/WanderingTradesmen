package mod.linguardium.tradesmen.api;


import mod.linguardium.tradesmen.Tradesmen;
import mod.linguardium.tradesmen.entities.InitEntities;
import mod.linguardium.tradesmen.entities.TradesmenEntity;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import org.apache.logging.log4j.Level;

import java.util.*;

public class TradesmenManager implements WorldTickCallback {
    public static final TradesmenManager INSTANCE = new TradesmenManager();
    private HashMap<World, WorldTradesmenManager> WorldManagers = new HashMap<>();
    public static HashMap<String, Trader> Traders = new HashMap<>();
    private TradesmenManager() {
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
            this.spawnDelay = Tradesmen.getConfig().spawnDelay;
            this.spawnChance = Tradesmen.getConfig().spawnChance;
            if (this.spawnDelay == 0 && this.spawnChance == 0) {
                this.spawnDelay = Tradesmen.getConfig().spawnDelay;
                this.spawnChance = Tradesmen.getConfig().spawnChance;
            }

        }
        public void tick() {

            if (this.world.getGameRules().getBoolean(GameRules.DO_TRADER_SPAWNING) && Traders.size()>1) {
                if (--this.tickCount <= 0) {
                    this.tickCount = 1200;
                    this.spawnDelay -= 1200;
                    if (this.spawnDelay <= 0) {
                        this.spawnDelay = Tradesmen.getConfig().spawnDelay;
                        if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
                            int i = this.spawnChance;
                            this.spawnChance = MathHelper.clamp(this.spawnChance, 0, 100);
                            if (this.random.nextInt(100) <= i) {
                                if (this.spawnRoamingTrader()) {
                                    this.spawnChance = Tradesmen.getConfig().spawnChance;
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
                List<String> allowedTraderTypes = new ArrayList<>();
                Traders.forEach((id,trader)->{
                    if ( !id.equals("default:default_trader") &&
                            (trader.allowedWorlds.contains("*") ||
                            trader.allowedWorlds.contains(Registry.DIMENSION_TYPE.getId(world.dimension.getType()).toString()))) {
                        allowedTraderTypes.add(id);
                    }

                });
                if (allowedTraderTypes.size() < 1) {
                    return true;
                }
                BlockPos blockPos = playerEntity.getBlockPos();

                // spawn a distance from the player or from a meeting POI type
                // currently only village bells
                PointOfInterestStorage pointOfInterestStorage = this.world.getPointOfInterestStorage();
                Optional<BlockPos> optional = pointOfInterestStorage.getPosition(PointOfInterestType.MEETING.getCompletionCondition(), (blockPosx) -> {
                    return true;
                }, blockPos, 48, PointOfInterestStorage.OccupationStatus.ANY);
                BlockPos blockPos2 = (BlockPos)optional.orElse(blockPos);
                BlockPos blockPos3 = this.getPosDistanceFrom(blockPos2, 25);
                if (blockPos3 != null) {
                    if (this.world.getBiome(blockPos3) == Biomes.THE_VOID) {
                        return false;
                    }

                    TradesmenEntity traderEntity=null;
                    if ((Tradesmen.getConfig().secrets && world.random.nextInt(1000) < 1)) {
                        traderEntity = (TradesmenEntity) InitEntities.SECRET_TRADESMEN_ENTITY_TYPE.spawn(this.world, (CompoundTag) null, (Text) null, (PlayerEntity) null, blockPos3, SpawnType.EVENT, false, false);
                    }else {
                        traderEntity = (TradesmenEntity) InitEntities.TRADESMEN_ENTITY_TYPE.spawn(this.world, (CompoundTag) null, (Text) null, (PlayerEntity) null, blockPos3, SpawnType.EVENT, false, false);
                    }
                    if (traderEntity != null) {
                        traderEntity.setTraderType(allowedTraderTypes.get(world.random.nextInt(allowedTraderTypes.size())));
                        for (int i=0;i<getTraderById(traderEntity.getTraderType()).animalCount; i++) {
                            this.SpawnAnimal(traderEntity.getTraderAnimal(),traderEntity, 4);
                        }

                        traderEntity.setDespawnDelay((int)(Tradesmen.getConfig().spawnDelay*0.75F));
                        traderEntity.setWanderTarget(blockPos2);
                        traderEntity.setPositionTarget(blockPos2, 16);
                        traderEntity.setInvulnerable(getTraderById(traderEntity.getTraderType()).godMode);
                        return true;
                    }
                }

                return false;
            }
        }
        private void SpawnAnimal(String AnimalId, TradesmenEntity ownerTrader, int distance) {
            if (AnimalId.isEmpty())
                return;
            BlockPos blockPos = this.getPosDistanceFrom(new BlockPos(ownerTrader), distance);
            if (blockPos != null) {
                MobEntity traderAnimalEntity = (MobEntity)(EntityType.get(AnimalId).orElse(EntityType.TRADER_LLAMA)).spawn(this.world, (CompoundTag)null, (Text)null, (PlayerEntity)null, blockPos, SpawnType.EVENT, false, false);
                if (traderAnimalEntity != null) {
                    traderAnimalEntity.attachLeash(ownerTrader, true);
                    ownerTrader.addAnimalUUID(traderAnimalEntity.getUuidAsString());
                }
            }
        }

        private BlockPos getSpawnableHeight(BlockPos pos, int vDistance) {
            for (int y = 0; y < vDistance; y++) {
                if (SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, this.world, pos.add(0,y,0), InitEntities.TRADESMEN_ENTITY_TYPE)) {
                    return pos.add(0,y,0);
                }
            }
            for (int y = -1; y > -vDistance; y--) {
                if (SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, this.world, pos.add(0,y,0), InitEntities.TRADESMEN_ENTITY_TYPE)) {
                    return pos.add(0,y,0);
                }
            }
            return null;
        }

        private BlockPos getPosDistanceFrom(BlockPos blockPos, int distance) {
            BlockPos blockPos2 = null;

            for(int j = 0; j < 10; ++j) {
                int k = blockPos.getX() + this.random.nextInt(distance * 2) - distance;
                int l = blockPos.getZ() + this.random.nextInt(distance * 2) - distance;
                blockPos2 = getSpawnableHeight(new BlockPos(k,blockPos.getY(),l),distance);
                if (blockPos2 != null) {
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
        if (world.isClient() || Tradesmen.getConfig().disableWanderingTradesmen ) {
            return;
        }
        ServerWorld sWorld = (ServerWorld)world;
        if (!WorldManagers.containsKey(sWorld)) {
            WorldManagers.put(world, new WorldTradesmenManager(sWorld));
        }
        WorldManagers.get(sWorld).tick();
    }

}
