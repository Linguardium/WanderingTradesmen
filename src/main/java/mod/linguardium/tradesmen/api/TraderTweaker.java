package mod.linguardium.tradesmen.api;


import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import io.github.cottonmc.libcd.api.CDSyntaxError;
import io.github.cottonmc.libcd.api.tweaker.Tweaker;
import io.github.cottonmc.libcd.api.tweaker.recipe.RecipeParser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceNotFoundException;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.concurrent.Executor;

public class TraderTweaker implements Tweaker {


    @Override
    public void prepareReload(ResourceManager resourceManager) {
        TradesmenManager.Traders.clear();
    }

    @Override
    public void applyReload(ResourceManager resourceManager, Executor executor) {

    }

    @Override
    public String getApplyMessage() {
        return String.valueOf(TradesmenManager.Traders.size()-1) + " traders";
    }

    @Override
    public JsonObject getDebugInfo() {
        return null;
    }

    public void addTrader(String Id, String name, String texture, String animal, tradeObject[] common, tradeObject[] rare) throws ResourceNotFoundException {
        TradesmenTradeOffers.SellItemFactory[] commonTrades = new TradesmenTradeOffers.SellItemFactory[common.length];
        TradesmenTradeOffers.SellItemFactory[] rareTrades = new TradesmenTradeOffers.SellItemFactory[rare.length];
        int i=0;
        for (tradeObject trade : common) {
            TradesmenTradeOffers.SellItemFactory sale=null;
            if (!trade.saleItem.isEmpty() && !trade.priceItem[0].isEmpty()) {
                sale = new TradesmenTradeOffers.SellItemFactory(trade.saleItem, trade.priceItem, trade.maxUses, trade.experience, trade.multiplier);
            }
            if (sale != null) {
                commonTrades[i] = sale;
            }
            i++;
        }
        i=0;
        for (tradeObject trade : rare) {
            TradesmenTradeOffers.SellItemFactory sale=null;
            if (!trade.saleItem.isEmpty() && !trade.priceItem[0].isEmpty()) {
                sale = new TradesmenTradeOffers.SellItemFactory(trade.saleItem, trade.priceItem, trade.maxUses, trade.experience, trade.multiplier);
            }
            if (sale!=null) {
                rareTrades[i] = sale;
            }
            i++;
        }

        TradesmenManager.Traders.put(Id,new Trader(name,texture,animal, commonTrades ,rareTrades));
    }
    public tradeObject makeTrade() {
        return new tradeObject();
    }
    public tradeObject makeTrade(Object item, Object[] price, int maxUses, int experience) throws CDSyntaxError {
        tradeObject retObj = new tradeObject()
                .item(item)
                .price(price)
                .maxUses(maxUses)
                .experience(experience);
        return retObj;
    }
    public tradeObject makeTrade(Object item, Object price, int maxUses, int experience) throws CDSyntaxError {
        tradeObject retObj = new tradeObject()
                .item(item)
                .price(price)
                .maxUses(maxUses)
                .experience(experience);
        return retObj;
    }
    public tradeObject makeTrade(Object item, Object price, int count, int maxUses, int experience) throws CDSyntaxError {
        tradeObject retObj = new tradeObject()
                .item(item)
                .count(count)
                .price(price)
                .maxUses(maxUses)
                .experience(experience);
        return retObj;
    }

    public class tradeObject {
        ItemStack saleItem=ItemStack.EMPTY;
        ItemStack[] priceItem= new ItemStack[]{new ItemStack((Item)null,0),new ItemStack((Item)null,0)};
        Integer maxUses = 1;
        Integer experience=0;
        Float multiplier=0.05F;
        public tradeObject item(Object item) throws CDSyntaxError {
            saleItem = RecipeParser.processItemStack(item).copy();
            return this;
        }
        public tradeObject price(Object[] price) throws CDSyntaxError {
            for (int i = 0; i< price.length && i<2 ; i++) {
                priceItem[i]=RecipeParser.processItemStack(price[i]).copy();
            }
            return this;
        }
        public tradeObject price(Object price) throws CDSyntaxError {
            if (price instanceof Integer) {
                this.priceItem[0] = new ItemStack(Items.EMERALD, (Integer) price);
            }else {
                this.priceItem[0] = RecipeParser.processItemStack(price).copy();
            }
            return this;
        }
        public tradeObject secondPriceStack(Object price) throws CDSyntaxError {
            priceItem[1] = RecipeParser.processItemStack(price);
            return this;
        }
        public tradeObject count(int count) {
            this.saleItem.setCount(count);
            return this;
        }
        public tradeObject maxUses(int maxUses) {
            this.maxUses=maxUses;
            return this;
        }
        public tradeObject experience(int experience) {
            this.experience=experience;
            return this;
        }
    }
}
