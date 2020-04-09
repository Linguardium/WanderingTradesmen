package mod.linguardium.tradesmen.api;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

import java.util.Random;

public class TradesmenTradeOffers {

    static class SellItemFactory implements TradeOffers.Factory {
        private ItemStack sell;
        private int price;
        private int count;
        private int maxUses;
        private int experience;
        private float multiplier;

        public SellItemFactory(SellItemFactory from) {
            this.sell = from.sell;
            this.price = from.price;
            this.count = from.count;
            this.maxUses = from.maxUses;
            this.experience = from.experience;
            this.multiplier=from.multiplier;
        }
        public SellItemFactory(Block block, int price, int count, int maxUses, int experience) {
            this(new ItemStack(block), price, count, maxUses, experience);
        }

        public SellItemFactory(Item item, int price, int count, int experience) {
            this((ItemStack) (new ItemStack(item)), price, count, 12, experience);
        }

        public SellItemFactory(Item item, int price, int count, int maxUses, int experience) {
            this(new ItemStack(item), price, count, maxUses, experience);
        }

        public SellItemFactory(ItemStack itemStack, int price, int count, int maxUses, int experience) {
            this(itemStack, price, count, maxUses, experience, 0.05F);
        }

        public SellItemFactory(ItemStack itemStack, int price, int count, int maxUses, int experience, float multiplier) {
            this.sell = itemStack;
            this.price = price;
            this.count = count;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }
    

        public TradeOffer create(Entity entity, Random random) {
            return new TradeOffer(new ItemStack(Items.EMERALD, this.price), 
                new ItemStack(this.sell.getItem(), this.count), this.maxUses, this.experience, this.multiplier);
        }
    }

}
