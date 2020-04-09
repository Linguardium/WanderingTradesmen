package mod.linguardium.tradesmen.api;


import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import io.github.cottonmc.libcd.api.tweaker.Tweaker;
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
        return String.valueOf(TradesmenManager.Traders.size()) + " traders";
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
            Identifier ItemId = Identifier.tryParse(trade.get(String.class, "item"));
            if (ItemId == null) {
                throw(new ResourceNotFoundException(null,trade.get(String.class, "item")));
            }
            TradesmenTradeOffers.SellItemFactory sale = new TradesmenTradeOffers.SellItemFactory(Registry.ITEM.get(ItemId), trade.getInt("price", 1), trade.getInt("count", 1), trade.getInt("maxUses", 1), trade.getInt("experience", 1));
            commonTrades[i] = sale;
            i++;
        }
        i=0;
        for (tradeObject trade : rare) {
            Identifier ItemId = Identifier.tryParse(trade.get(String.class, "item"));
            if (ItemId == null) {
                throw(new ResourceNotFoundException(null,trade.get(String.class, "item")));
            }
            TradesmenTradeOffers.SellItemFactory sale = new TradesmenTradeOffers.SellItemFactory(Registry.ITEM.get(ItemId), trade.getInt("price", 1), trade.getInt("count", 1), trade.getInt("maxUses", 1), trade.getInt("experience", 1));
            rareTrades[i] = sale;
            i++;
        }

        TradesmenManager.Traders.put(Id,new Trader(name,texture,animal, commonTrades ,rareTrades));
    }
    public tradeObject makeTrade() {
        return new tradeObject();
    }
    public tradeObject makeTrade(String item, int price, int count, int maxUses, int experience) {
        tradeObject retObj = new tradeObject();
        retObj.put("item",new JsonPrimitive(item));
        retObj.put("price",new JsonPrimitive(price));
        retObj.put("count",new JsonPrimitive(count));
        retObj.put("maxUses",new JsonPrimitive(maxUses));
        retObj.put("experience",new JsonPrimitive(experience));
        return retObj;
    }
    public JsonObject makeTrade(String item, int price, int count, int maxUses) {
        return makeTrade(item,price,count,maxUses,1);
    }
    public JsonObject makeTrade(String item, int price, int count) {
        return makeTrade(item,price,count,1,1);
    }
    public JsonObject makeTrade(String item, int price) {
        return makeTrade(item,price,1,1,1);
    }

    public class tradeObject extends JsonObject {
        public tradeObject item(String item) {
            this.put("item",new JsonPrimitive(item));
            return this;
        }
        public tradeObject price(int price) {
            this.put("price",new JsonPrimitive(price));
            return this;
        }
        public tradeObject count(int count) {
            this.put("count", new JsonPrimitive(count));
            return this;
        }
        public tradeObject maxUses(int maxUses) {
            this.put("maxUses", new JsonPrimitive(maxUses));
            return this;
        }
        public tradeObject experience(int experience) {
            this.put("experience",new JsonPrimitive(experience));
            return this;
        }
    }
}
