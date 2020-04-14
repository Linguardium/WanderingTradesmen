package mod.linguardium.tradesmen.api;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TradesmenTradeOffers {

    public static class SellItemFactory implements TradeOffers.Factory {
        private ItemStack sell;
        private ItemStack[] price;
        private int count;
        private int maxUses;
        private int experience;
        private float multiplier;
        private int randomCostMin=1;
        private int randomCostMax=64;
        private boolean randomCost=false;
        private boolean randomCount=false;
        private boolean randomSaleItem=false;
        private boolean randomPriceItem=false;
        private int randomCountMin=1;
        private int randomCountMax=64;

        public SellItemFactory(SellItemFactory from) {
            this.sell = from.sell;
            this.price = from.price;
            this.count = from.count;
            this.maxUses = from.maxUses;
            this.experience = from.experience;
            this.multiplier=from.multiplier;
        }
        public SellItemFactory(Block block, int price, int count, int maxUses, int experience) {
            this(new ItemStack(block, count), price, maxUses, experience);
        }

        public SellItemFactory(Item item, int price, int count, int experience) {
            this(new ItemStack(item, count), price, 12, experience);
        }

        public SellItemFactory(Item item, int price, int count, int maxUses, int experience) {
            this(new ItemStack(item, count), price, maxUses, experience);
        }

        public SellItemFactory(ItemStack itemStack, int price, int maxUses, int experience) {
            this(itemStack, price, maxUses, experience, 0.05F);
        }

        public SellItemFactory(ItemStack itemStack, int price, int maxUses, int experience, float multiplier) {
            this(itemStack,new ItemStack[]{new ItemStack(Items.EMERALD, price),ItemStack.EMPTY}, maxUses,experience,multiplier);
        }
        public SellItemFactory(ItemStack itemStack, ItemStack price, int maxUses, int experience) {
            this(itemStack,new ItemStack[]{price,ItemStack.EMPTY}, maxUses,experience,0.05F);
        }

        public SellItemFactory(ItemStack itemStack, ItemStack[] price, int randomCostMin, int randomCostMax, boolean randomCost, boolean randomCount, int randomCountMin, int randomCountMax, boolean randomSaleItem, boolean randomPriceItem, int maxUses, int experience, float multiplier) {
            this.sell = itemStack;
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
            this.randomCost=randomCost;
            this.randomCount=randomCount;
            this.randomCostMin=randomCostMin;
            this.randomCostMax=randomCostMax;
            this.randomCountMin=randomCountMin;
            this.randomCountMax=randomCountMax;
            this.randomSaleItem = randomSaleItem;
            this.randomPriceItem = randomPriceItem;
        }
        public SellItemFactory(ItemStack itemStack, ItemStack[] price, int maxUses, int experience, float multiplier) {
            this.sell = itemStack;
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        public TradeOffer create(Entity entity, Random random) {
            ItemStack price0 = this.price[0].copy();
            int priceCount =price0.getCount();
            if (priceCount <1)
                priceCount=1;

            if (randomPriceItem) {

                price0=new ItemStack(Registry.ITEM.get(random.nextInt((int)Registry.ITEM.stream().count())),priceCount);
            }
            if (price0.isEmpty()) {
                price0 = new ItemStack(Items.EMERALD,priceCount);
            }
            if (this.randomCost) {
                price0.setCount(random.nextInt((randomCostMax+1)-randomCostMin)+randomCostMin);
            }
            ItemStack saleItem = this.sell.copy();
            int saleCount =saleItem.getCount();
            if (saleCount <1)
                saleCount=1;

            if (randomSaleItem) {
                saleItem=new ItemStack(Registry.ITEM.get(random.nextInt((int)Registry.ITEM.stream().count())),saleCount);
            }
            if (randomCount) {
                saleItem.setCount(random.nextInt((randomCountMax+1)-randomCountMin)+randomCostMin);
            }
            return new TradeOffer(price0,this.price[1],
                saleItem, this.maxUses, this.experience, this.multiplier);
        }
    }
    public static class SellPotionHoldingItemFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final ItemStack[] price;
        private final int maxUses;
        private final int experience;
        private final float priceMultiplier;

        public SellPotionHoldingItemFactory(ItemStack item, ItemStack[] price, int maxUses, int experience, float priceMultiplier) {
            this.sell = item;
            this.price=price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.priceMultiplier = priceMultiplier;
        }

        public TradeOffer create(Entity entity, Random random) {
            List<Potion> list = Registry.POTION.stream().filter((potionx) -> {
                return !potionx.getEffects().isEmpty() && BrewingRecipeRegistry.isBrewable(potionx);
            }).collect(Collectors.toList());
            Potion potion = (Potion)list.get(random.nextInt(list.size()));
            ItemStack sellItem = PotionUtil.setPotion(this.sell.copy(), potion);
            return new TradeOffer(price[0].copy(), price[1].copy(), sellItem, this.maxUses, this.experience, this.priceMultiplier);
        }
    }
    public static class EnchantBookFactory implements TradeOffers.Factory {
        private final int experience;
        private Integer randomCostMin=2;
        private Integer randomCostMax=64;
        private Boolean randomCost=true;
        private Boolean doubleTreasureCost=true;
        private Integer maxUses = 12;
        private Float priceMultiplier = 0.02F;
        private ItemStack[] price={ItemStack.EMPTY,ItemStack.EMPTY};
        public EnchantBookFactory(ItemStack[] price, int maxUses, int experience, float priceMultiplier) {
            this.price=price;
            this.experience=experience;
            this.maxUses=maxUses;
            this.priceMultiplier=priceMultiplier;
            this.randomCost=false;
        }
        public EnchantBookFactory(ItemStack[] price, int min, int max, boolean doubleTreasureCost, int maxUses, int experience, float priceMultiplier) {
            this.price = price;
            this.experience=experience;
            this.randomCostMin=min;
            this.randomCostMax=max;
            this.doubleTreasureCost=doubleTreasureCost;
            this.maxUses=maxUses;
            this.priceMultiplier=priceMultiplier;
        }

        public TradeOffer create(Entity entity, Random random) {
            Enchantment enchantment = (Enchantment)Registry.ENCHANTMENT.getRandom(random);
            int enchantmentLevel = MathHelper.nextInt(random, enchantment.getMinimumLevel(), enchantment.getMaximumLevel());
            ItemStack itemStack = EnchantedBookItem.forEnchantment(new InfoEnchantment(enchantment, enchantmentLevel));
            int cost = 0;
            ItemStack price0 = price[0].copy();
            if (price0.isEmpty()) {
                price0=new ItemStack(Items.EMERALD);
            }
            if (randomCost) {
                cost = randomCostMin + random.nextInt(5+enchantmentLevel*10)+3*enchantmentLevel;
                cost = Math.min(Math.max(cost,randomCostMin),randomCostMax);
                price0.setCount(cost);
            }
            if (doubleTreasureCost) {
                if (enchantment.isTreasure()) {
                    price0.setCount(price0.getCount()*2);
                }
            }

            return new TradeOffer(price0, price[1].copy(), itemStack, maxUses, this.experience, this.priceMultiplier);
        }
    }

    public static class SellEnchantedItemFactory implements TradeOffers.Factory {
        private final ItemStack item;
        private final int experience;
        private Integer randomCostMin=1;
        private Integer randomCostMax=64;
        private Boolean randomCost=true;
        private final boolean treasure;
        private Integer maxUses = 12;
        private Float priceMultiplier = 0.02F;
        private ItemStack[] price={ItemStack.EMPTY,ItemStack.EMPTY};

        public SellEnchantedItemFactory(ItemStack item, ItemStack[] price, boolean treasure, int maxUses, int experience, float multiplier) {
            this.item = item;
            this.price = price;
            this.randomCost=false;
            this.maxUses = maxUses;
            this.experience = experience;
            this.priceMultiplier = multiplier;
            this.treasure=treasure;
        }
        public SellEnchantedItemFactory(ItemStack item, ItemStack[] price, int min, int max, boolean treasure, int maxUses, int experience, float multiplier) {
            this.item = item;
            this.randomCostMin=min;
            this.randomCostMax=max;
            this.price=price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.priceMultiplier = multiplier;
            this.treasure=treasure;
        }

        public TradeOffer create(Entity entity, Random random) {
            ItemStack enchantedItem = this.item.copy();
            int enchantPower = 5 + random.nextInt(15); // power level
            enchantedItem = EnchantmentHelper.enchant(random, enchantedItem, enchantPower, this.treasure);
            ItemStack price0=price[0];
            if (price0.isEmpty()) {
                price0=new ItemStack(Items.EMERALD);
            }
            if (randomCost) {
                int cost = Math.min(this.randomCostMin + enchantPower, randomCostMax);
                price0.setCount(cost);
            }
            return new TradeOffer(price0, price[1].copy(), enchantedItem, this.maxUses, this.experience, this.priceMultiplier);
        }
    }

    /*
    TODO below
     */
    public static class SellDyedItemFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final ItemStack[] price;
        private final int maxUses;
        private final int experience;
        private final float priceMultiplier;
        private final int color;
        public SellDyedItemFactory(ItemStack item, ItemStack[] price, int maxUses, int experience, float priceMultiplier, int color) {
            this.sell = item;
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.priceMultiplier=priceMultiplier;
            this.color = color;
        }

        public TradeOffer create(Entity entity, Random random) {
            ItemStack saleItem = this.sell.copy();
            if (saleItem.getItem() instanceof DyeableItem) {
                if (this.color < 0) {
                    List<DyeItem> list = Lists.newArrayList();
                    list.add(getDye(random));
                    if (random.nextFloat() > 0.7F) {
                        list.add(getDye(random));
                    }

                    if (random.nextFloat() > 0.8F) {
                        list.add(getDye(random));
                    }
                    saleItem = DyeableItem.blendAndSetColor(saleItem, list);
                }else {
                    ((DyeableItem) saleItem.getItem()).setColor(saleItem,this.color);
                }
            }

            return new TradeOffer(price[0].copy(),price[1].copy(),saleItem, this.maxUses, this.experience, this.priceMultiplier);
        }

        private static DyeItem getDye(Random random) {
            return DyeItem.byColor(DyeColor.byId(random.nextInt(DyeColor.values().length)));
        }
    }
}
