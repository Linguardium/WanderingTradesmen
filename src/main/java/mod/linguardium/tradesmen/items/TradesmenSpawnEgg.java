package mod.linguardium.tradesmen.items;

import mod.linguardium.tradesmen.api.Trader;
import mod.linguardium.tradesmen.api.TradesmenManager;
import mod.linguardium.tradesmen.entities.TradesmenEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import java.util.Objects;

public class TradesmenSpawnEgg extends SpawnEggItem {
    public TradesmenSpawnEgg(EntityType<?> type, int primaryColor, int secondaryColor) {
        super(type, primaryColor, secondaryColor, new Item.Settings().group(ItemGroup.MISC));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient) {
            ItemStack itemStack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            Direction direction = context.getSide();
            BlockState blockState = world.getBlockState(blockPos);

            BlockPos blockPos3;
            if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
                blockPos3 = blockPos;
            } else {
                blockPos3 = blockPos.offset(direction);
            }

            EntityType<?> entityType2 = this.getEntityType(itemStack.getTag());
            Entity e = entityType2.spawnFromItemStack(world, itemStack, context.getPlayer(), blockPos3, SpawnType.SPAWN_EGG, true, !Objects.equals(blockPos, blockPos3) && direction == Direction.UP);
            if (e!=null && e instanceof TradesmenEntity) {
                ((TradesmenEntity) e).setTraderType(this.getTraderType(itemStack));
                if (TradesmenManager.getTraderById(getTraderType(itemStack)).isTiered) {
                    ((TradesmenEntity) e).setTraderTier(world.random.nextInt(TradesmenManager.getTraderById(getTraderType(itemStack)).tierTradeCount.size()));
                }
                ((TradesmenEntity)e).setDespawnDelay((int)(600));
            }

        }
        return ActionResult.SUCCESS;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        HitResult hitResult = rayTrace(world, user, RayTraceContext.FluidHandling.SOURCE_ONLY);
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        } else if (world.isClient) {
            return TypedActionResult.success(itemStack);
        } else {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!(world.getBlockState(blockPos).getBlock() instanceof FluidBlock)) {
                return TypedActionResult.pass(itemStack);
            } else if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos, blockHitResult.getSide(), itemStack)) {
                EntityType<?> entityType = this.getEntityType(itemStack.getTag());
                Entity e = entityType.spawnFromItemStack(world, itemStack, user, blockPos, SpawnType.SPAWN_EGG, false, false);
                if (e!=null && e instanceof TradesmenEntity) {
                    ((TradesmenEntity) e).setTraderType(this.getTraderType(itemStack));
                    if (TradesmenManager.getTraderById(getTraderType(itemStack)).isTiered) {
                        ((TradesmenEntity) e).setTraderTier(world.random.nextInt(TradesmenManager.getTraderById(getTraderType(itemStack)).tierTradeCount.size()));
                    }
                    ((TradesmenEntity)e).setDespawnDelay((int)(600));
                }
                return TypedActionResult.success(itemStack);
            } else {
                return TypedActionResult.fail(itemStack);
            }
        }
    }

    private String getTraderType(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("traderType")) {
            return tag.getString("traderType");
        }else{
            return "default:default_trader";
        }
    }
    @Override
    public Text getName(ItemStack stack) {
        Trader t = TradesmenManager.getTraderById(this.getTraderType(stack));
        Text name;
        if (t!=null) {
            name = t.name.copy();
        }else{
            name = new LiteralText("Tradesmen");
        }
        return new TranslatableText(this.getTranslationKey(),name);
    }

    public boolean isOfSameEntityType(CompoundTag tag, EntityType<?> type) {
        return false;
    }

    //Registry.register(Registry.ITEM, new Identifier("wiki_entity", "cookie_creeper_spawn_egg"), new SpawnEggItem(ModEntities.COOKIE_CREEPER, 0x0DA70B, 0x73420E));
}
