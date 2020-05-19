package mod.linguardium.tradesmen.api;


import com.google.common.collect.Lists;
import io.github.cottonmc.libcd.api.CDSyntaxError;
import mod.linguardium.tradesmen.Tradesmen;
import mod.linguardium.tradesmen.api.objects.tradeObject;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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
    public static void ClearTradesmenList() {
        TradesmenManager.Traders.clear();
        try {
        List<List<tradeObject>> alltrades = Lists.newArrayList(
                Lists.newArrayList(TraderTweaker.INSTANCE.makeTrade("minecraft:dirt",64,1,1,0)),
                Lists.newArrayList(TraderTweaker.INSTANCE.makeTrade().item("minecraft:dirt@64").price(new String[]{"minecraft:cobblestone", "minecraft:sand"}).maxUses(4),
                        TraderTweaker.INSTANCE.makeTrade().item("minecraft:dirt").price("minecraft:emerald").count(4).maxUses(4),
                        TraderTweaker.INSTANCE.makeTrade().item("minecraft:dirt@64").price(1).maxUses(4))
        );
        Trader t = new Trader().name("Def Ault").tiered().setTrades(alltrades,Lists.newArrayList(1,1));
        TradesmenManager.Traders.put("default:default_trader", t);
        } catch (CDSyntaxError cdSyntaxError) {
            cdSyntaxError.printStackTrace();
        }
    }
    private static class WorldTradesmenManager {
        private final Random random = new Random();
        private int tickCount;
        private int spawnDelay;
        private final ServerWorld world;

        public WorldTradesmenManager(ServerWorld world) {
            this.world = world;
            this.tickCount = 600;
            this.spawnDelay = Tradesmen.getConfig().spawnDelay;

        }
        public void tick() {

            if (this.world.getGameRules().getBoolean(GameRules.DO_TRADER_SPAWNING) && Traders.size()>1) {
                if (--this.tickCount <= 0) {
                    this.tickCount = 600;
                    this.spawnDelay -= 600;
                    if (this.spawnDelay <= 0) {
                        this.spawnDelay = Tradesmen.getConfig().spawnDelay;
                        if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
                            if (this.random.nextInt(100) <= Tradesmen.getConfig().spawnChance) {
                                this.spawnRoamingTrader();
                            }
                        }
                    }
                }
            }
        }

        private void spawnRoamingTrader() {
            PlayerEntity playerEntity = this.world.getRandomAlivePlayer();
            if (playerEntity != null) {
                List<String> allowedTraderTypes = new ArrayList<>();
                Identifier dim = Registry.DIMENSION_TYPE.getId(world.dimension.getType());
                if (dim == null) {
                    return;
                }
                Traders.forEach((id,trader)->{
                    if ( !id.equals("default:default_trader") &&
                            (trader.allowedWorlds.contains("*") ||
                            trader.allowedWorlds.contains(dim.toString()))) {
                        allowedTraderTypes.add(id);
                    }

                });
                if (allowedTraderTypes.size() < 1) {
                    return;
                }
                BlockPos blockPos = playerEntity.getBlockPos();

                // spawn a distance from the player or from a meeting POI type
                // currently only village bells
                PointOfInterestStorage pointOfInterestStorage = this.world.getPointOfInterestStorage();
                Optional<BlockPos> optional = pointOfInterestStorage.getPosition(PointOfInterestType.MEETING.getCompletionCondition(), blockPosx -> true, blockPos, 48, PointOfInterestStorage.OccupationStatus.ANY);
                BlockPos blockPos2 = optional.orElse(blockPos);
                BlockPos blockPos3 = this.getNearbySpawnPos(blockPos2, 25);
                if (blockPos3 != null && this.wontSuffocateAt(blockPos3)) {
                    if (this.world.getBiome(blockPos3) == Biomes.THE_VOID) {
                        return;
                    }

                    TradesmenEntity traderEntity;
                    if ((Tradesmen.getConfig().secrets && world.random.nextInt(1000) < 1)) {
                        traderEntity = InitEntities.SECRET_TRADESMEN_ENTITY_TYPE.spawn(this.world, null, null,  null, blockPos3, SpawnType.EVENT, false, false);
                    }else {
                        traderEntity = InitEntities.TRADESMEN_ENTITY_TYPE.spawn(this.world, null, null, null, blockPos3, SpawnType.EVENT, false, false);
                    }
                    if (traderEntity != null) {
                        traderEntity.setTraderType(allowedTraderTypes.get(world.random.nextInt(allowedTraderTypes.size())));
                        for (int i=0;i<getTraderById(traderEntity.getTraderType()).animalCount; i++) {
                            this.SpawnAnimal(traderEntity.getTraderAnimal(),traderEntity);
                        }

                        traderEntity.setDespawnDelay((int)(Tradesmen.getConfig().spawnDelay*0.75F));
                        traderEntity.setWanderTarget(blockPos2);
                        traderEntity.setPositionTarget(blockPos2, 16);
                        traderEntity.setInvulnerable(getTraderById(traderEntity.getTraderType()).godMode);
                    }
                }
            }
        }
        private void SpawnAnimal(String AnimalId, TradesmenEntity ownerTrader) {
            if (AnimalId.isEmpty())
                return;
            BlockPos blockPos = this.getNearbySpawnPos(new BlockPos(ownerTrader), 4);
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

        private BlockPos getNearbySpawnPos(BlockPos blockPos, int distance) {
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

        private boolean wontSuffocateAt(BlockPos blockPos) {
            Iterator<BlockPos> var2 = BlockPos.iterate(blockPos, blockPos.add(1, 2, 1)).iterator();

            BlockPos blockPos2;
            do {
                if (!var2.hasNext()) {
                    return true;
                }

                blockPos2 = var2.next();
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
