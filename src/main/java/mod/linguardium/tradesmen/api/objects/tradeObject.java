package mod.linguardium.tradesmen.api.objects;

import io.github.cottonmc.libcd.api.CDSyntaxError;
import io.github.cottonmc.libcd.api.tweaker.recipe.RecipeParser;
import mod.linguardium.tradesmen.api.TradesmenTradeOffers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.village.TradeOffers;

import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.function.Function;

public class tradeObject {
    private static int getIntOrDefault(CompoundTag tag, String key, int defaultValue) {
        return tag.contains(key)?tag.getInt(key):defaultValue;
    }
    private static float getFloatOrDefault(CompoundTag tag, String key, float defaultValue) {
        return tag.contains(key)?tag.getFloat(key):defaultValue;
    }
    private static boolean getBoolOrDefault(CompoundTag tag, String key, boolean defaultValue) {
        return tag.contains(key)?tag.getBoolean(key):defaultValue;
    }
    public static HashMap<String, Function<CompoundTag, TradeOffers.Factory>> factories = new HashMap<>();
    public static void init() {
        registerFactory("tradesmen:simpleTrade",(tag)-> {
            int maxUses = getIntOrDefault(tag,"maxUses",4);
            int experience = getIntOrDefault(tag,"experience",1);
            float priceMultiplier = getFloatOrDefault(tag,"priceMultiplier",0.05F);
            return new TradesmenTradeOffers.SellItemFactory(ItemStack.fromTag(tag.getCompound("saleItem")), new ItemStack[]{ItemStack.fromTag(tag.getCompound("priceItem1")), ItemStack.fromTag(tag.getCompound("priceItem2"))}, maxUses,experience,priceMultiplier);
        });
        registerFactory("tradesmen:randomEnchantedBook",(tag)->{
            int maxUses = getIntOrDefault(tag,"maxUses",12);
            int experience = getIntOrDefault(tag,"experience",1);
            float priceMultiplier = getFloatOrDefault(tag,"priceMultiplier",0.02F);

            if (getBoolOrDefault(tag,"randomPrice",true) && !tag.contains("priceItem1")) {
                int min = getIntOrDefault(tag,"priceMin",2);
                int max = getIntOrDefault(tag,"priceMax",64);
                boolean treasureCost = getBoolOrDefault(tag,"doubleTreasureCost",true);
                return new TradesmenTradeOffers.EnchantBookFactory(ItemStack.fromTag(tag.getCompound("priceItem2")),min , max,treasureCost, maxUses,experience,priceMultiplier);
            }else{
                return new TradesmenTradeOffers.EnchantBookFactory(new ItemStack[]{ItemStack.fromTag(tag.getCompound("priceItem1")),ItemStack.fromTag(tag.getCompound("priceItem2"))},maxUses,experience,priceMultiplier);
            }
        });
        registerFactory("tradesmen:randomEnchantmentItem",(tag)->{
            int maxUses = getIntOrDefault(tag,"maxUses",12);
            int experience = getIntOrDefault(tag,"experience",1);
            float priceMultiplier = getFloatOrDefault(tag,"priceMultiplier",0.05F);
            boolean treasures = getBoolOrDefault(tag,"enableTreasureEnchants",true);
            if (getBoolOrDefault(tag,"randomPrice",true) && !tag.contains("priceItem1")) {
                int min = getIntOrDefault(tag,"priceMin",2);
                int max = getIntOrDefault(tag,"priceMax",64);
                return new TradesmenTradeOffers.SellEnchantedItemFactory(ItemStack.fromTag(tag.getCompound("saleItem")), ItemStack.fromTag(tag.getCompound("priceItem2")),min , max,treasures, maxUses,experience,priceMultiplier);
            }else{
                return new TradesmenTradeOffers.SellEnchantedItemFactory(ItemStack.fromTag(tag.getCompound("saleItem")), new ItemStack[]{ItemStack.fromTag(tag.getCompound("priceItem1")),ItemStack.fromTag(tag.getCompound("priceItem2"))},treasures,maxUses,experience,priceMultiplier);
            }
        });
        registerFactory("tradesmen:randomDyedItem",(tag)->{
            int maxUses = getIntOrDefault(tag,"maxUses",12);
            int experience = getIntOrDefault(tag,"experience",1);
            float priceMultiplier = getFloatOrDefault(tag,"priceMultiplier",0.02F);
            int dyeColor=getIntOrDefault(tag,"dyeColor",-1);
            return new TradesmenTradeOffers.SellDyedItemFactory(ItemStack.fromTag(tag.getCompound("saleItem")), new ItemStack[]{ItemStack.fromTag(tag.getCompound("priceItem1")), ItemStack.fromTag(tag.getCompound("priceItem2"))},maxUses,experience,priceMultiplier, dyeColor);
        });
        registerFactory("tradesmen:randomPotionEffect",(tag)-> {
            int maxUses = getIntOrDefault(tag,"maxUses",12);
            int experience = getIntOrDefault(tag,"experience",1);
            float priceMultiplier = getFloatOrDefault(tag,"priceMultiplier",0.02F);
            return new TradesmenTradeOffers.SellPotionHoldingItemFactory(ItemStack.fromTag(tag.getCompound("saleItem")), new ItemStack[]{ItemStack.fromTag(tag.getCompound("priceItem1")), ItemStack.fromTag(tag.getCompound("priceItem2"))},maxUses,experience,priceMultiplier);
        });
    }
    public static void registerFactory(String id, Function<CompoundTag, TradeOffers.Factory> factory) {
        factories.put(id,factory);
    }


    /*
     *  Variables
     */

    public String factoryId;
    public CompoundTag tag;

    public tradeObject() {
        this.tag = new CompoundTag();
        this.tag.putFloat("priceMultiplier",0.05F);
        this.tag.putInt("experience",1);
        this.factoryId="tradesmen:simpleTrade";
    }
    public tradeObject item(Object item) throws CDSyntaxError {
         tag.put("saleItem",RecipeParser.processItemStack(item).copy().toTag(new CompoundTag()));
        return this;
    }
    public tradeObject price(Object[] price) throws CDSyntaxError {
        for (int i = 0; i< price.length && i<2 ; i++) {
            this.tag.put("priceItem"+String.valueOf(i), RecipeParser.processItemStack(price[i]).copy().toTag(new CompoundTag()));
        }
        return this;
    }
    public tradeObject price(Object price) throws CDSyntaxError {
        ItemStack it = ItemStack.EMPTY;
        if (price instanceof Integer) {
            it =new ItemStack(Items.EMERALD, (Integer) price);
        }else {
            it = RecipeParser.processItemStack(price).copy();
        }
        this.tag.put("priceItem1",it.toTag(new CompoundTag()));
        return this;
    }
    public tradeObject secondPrice(Object price) throws CDSyntaxError {
        this.tag.put("priceItem2",RecipeParser.processItemStack(price).toTag(new CompoundTag()));
        return this;
    }
    public tradeObject count(int count) {
        ItemStack it = ItemStack.fromTag((CompoundTag) this.tag.get("saleItem"));
        it.setCount(count);
        this.tag.put("saleItem",it.toTag(new CompoundTag()));
        return this;
    }
    public tradeObject maxUses(int maxUses) {
        this.tag.putInt("maxUses",maxUses);
        return this;
    }
    public tradeObject experience(int experience) {
        this.tag.putInt("experience",experience);
        return this;
    }
    public tradeObject priceMultiplier(float multi) {
        this.tag.putFloat("priceMultiplier",multi);
        return this;
    }

    /*
            Random enchanted item methods
     */
    public tradeObject randomEnchantedBookTrade() {
        this.tag = new CompoundTag();
        this.priceMultiplier(0.2F);
        this.experience(1);
        this.factoryId="tradesmen:randomEnchantedBook";
        return this;
    }
    public tradeObject randomEnchantmentItemTrade() {
        this.tag = new CompoundTag();
        this.factoryId="tradesmen:randomEnchantmentItem";
        return this;
    }
    public tradeObject randomDyedItemTrade() {
        this.tag=new CompoundTag();
        this.factoryId="tradesmen:randomDyedItem";
        return this;
    }
    public tradeObject dyedItemTrade(Object color) throws CDSyntaxError, InvalidObjectException {
        this.factoryId="tradesmen:randomDyedItem";
        int iColor = ParseColor.toInt(color);
        this.tag.putInt("dyeColor",iColor);
        return this;
    }
    public tradeObject randomPrice(int min, int max) throws CDSyntaxError {
        if (!factoryId.equals("tradesmen:randomEnchantedBook") && !(factoryId.equals("tradesmen:randomEnchantmentItem")))
            throw(new CDSyntaxError("random price cannot be applied to sale type: "+factoryId));
        if (this.tag.contains("priceItem1")) {
            this.tag.remove("priceItem1");
        }
        this.tag.putBoolean("randomPrice",true);
        this.tag.putInt("priceMin",min);
        this.tag.putInt("priceMax",max);
        return this;
    }
    public tradeObject randomPrice(int max) throws CDSyntaxError {
        if (!factoryId.equals("tradesmen:randomEnchantedBook") && !(factoryId.equals("tradesmen:randomEnchantmentItem")))
            throw(new CDSyntaxError("random price cannot be applied to sale type: "+factoryId));
        if (this.tag.contains("priceItem1")) {
            this.tag.remove("priceItem1");
        }
        this.tag.putInt("priceMax",max);
        this.tag.putBoolean("randomPrice",true);
        return this;
    }
    public tradeObject doubleTreasureCost(boolean b) throws CDSyntaxError {
        if (!factoryId.equals("tradesmen:randomEnchantedBook"))
            throw(new CDSyntaxError("treasure price modification cannot be applied to sale type: "+factoryId));
        this.tag.putBoolean("doubleTreasureCost",b);
        return this;
    }
    public tradeObject doubleTreasureCost() throws CDSyntaxError {
        return doubleTreasureCost(true);
    }
    public tradeObject enableTreasureEnchants(boolean treasure) {
        this.tag.putBoolean("enableTreasureEnchants", treasure);
        return this;
    }
    public tradeObject enableTreasureEnchants() {
        return this.enableTreasureEnchants(true);
    }
    /*
            Random Potion Effect Item methods
     */
    public tradeObject randomEffectItemTrade() {
        this.tag = new CompoundTag();
        this.priceMultiplier(0.05F);
        this.experience(1);
        this.maxUses(12);
        this.factoryId="tradesmen:randomPotionEffect";
        return this;
    }

    /*
            Dyed Item methods
     */
    /*0public tradeObject DyedItemTrade() {
        this.tag = new CompoundTag();
        this.priceMultiplier(0.05F);
        this.experience(1);
        this.maxUses(12);
        this.factoryId="tradesmen:randomPotionEffect";
        return this;
    }*/
}