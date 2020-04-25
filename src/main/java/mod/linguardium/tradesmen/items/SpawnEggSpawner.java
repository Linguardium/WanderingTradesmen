package mod.linguardium.tradesmen.items;

import mod.linguardium.tradesmen.api.TradesmenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SpawnEggSpawner extends Item {
    public SpawnEggSpawner() {
        super(new Item.Settings().maxCount(1).group(ModItems.ITEM_GROUP));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient()) {
            for (String k : TradesmenManager.Traders.keySet()) {
                if (k.equals("default:default_trader"))
                    continue;
                ItemStack i = new ItemStack(ModItems.SPAWN_EGG);
                CompoundTag tag = i.getOrCreateTag();
                tag.putString("traderType", k);
                i.setTag(tag);
                if (!user.giveItemStack(i)) {
                    ItemScatterer.spawn(world, user.getX(), user.getY(), user.getZ(), i);
                }
            }
            stack.decrement(1);
        }

        return TypedActionResult.success(stack);
    }
}
