package mod.linguardium.tradesmen.api;


import blue.endless.jankson.JsonObject;
import io.github.cottonmc.libcd.api.CDSyntaxError;
import io.github.cottonmc.libcd.api.tweaker.Tweaker;
import io.github.cottonmc.libcd.api.tweaker.recipe.RecipeParser;
import jdk.internal.jline.internal.Nullable;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.ListAdapter;
import jdk.nashorn.internal.runtime.ScriptObject;
import mod.linguardium.tradesmen.api.objects.tradeObject;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceNotFoundException;
import net.minecraft.village.TradeOffers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

import static mod.linguardium.tradesmen.api.objects.ObjectConversion.toJavaObject;

public class TraderTweaker implements Tweaker {

    public static final TraderTweaker INSTANCE = new TraderTweaker();
    private TraderTweaker() {}

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


    public traderObject makeTrader(String Id) {
        return new traderObject(Id);
    }
    public void addTrader(traderObject t, tradeObject[][] trades, int[] tradeCounts) {
        List<List<tradeObject>> tradeObjectLists = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        for (tradeObject[] trade : trades) {
            List<tradeObject> tradeObjects = new ArrayList<>();
            Collections.addAll(tradeObjects, trade);
            tradeObjectLists.add(tradeObjects);
        }
        for (int tradeCount : tradeCounts) {
            counts.add(tradeCount);
        }
        addTrader(t,tradeObjectLists,counts);
    }

    public void addTrader(String Id, String name, String texture, String animal, tradeObject[] common, tradeObject[] rare) throws ResourceNotFoundException, CDSyntaxError {
        if (Id.isEmpty() || TradesmenManager.Traders.containsKey(Id))
            throw(new CDSyntaxError("Trader ID already in use: "+Id));
        traderObject t = new traderObject(Id);
        if (!name.isEmpty())
            t=t.name(name);
        if (!texture.isEmpty())
            t=t.texture(texture);
        if (!animal.isEmpty())
            t=t.animal(animal);
        addTrader(t,common,rare);
    }
    public void addTrader(traderObject t, List<List<tradeObject>> trades, List<Integer> tradeCounts) {
        List<List<TradeOffers.Factory>> tradeFactories = new ArrayList<List<TradeOffers.Factory>>();
        for (int i=0;i<trades.size();i++){
            // Gotta do this because of the GD ScriptObjectMirror that nashborn keeps forcing on me
            List<tradeObject> tier = (List<tradeObject>)toJavaObject(trades.get(i));
            List<TradeOffers.Factory> tierFactories = new ArrayList<>();
            for (tradeObject trade: tier) {
                TradeOffers.Factory sale = null;
                if (tradeObject.factories.containsKey(trade.factoryId)) {
                    sale = tradeObject.factories.getOrDefault(trade.factoryId,(tag)->null).apply(trade.tag);
                }
                if (sale != null) {
                    tierFactories.add(sale);
                }
            }
            tradeFactories.add(tierFactories);
        }

        TradesmenManager.Traders.put(t.Id,new Trader(t.name, t.textureId, t.clothesTextureId, t.clothesColor, t.hatTextureId, t.hatColor ,t.animal, tradeFactories, tradeCounts));

    }

    public void addTrader(traderObject t, tradeObject[] common, tradeObject[] rare) {
        addTrader(t,new tradeObject[][]{common,rare},new int[]{3,1});
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


    public class traderObject {
        public String name="Tradesman";
        public String animal="minecraft:trader_llama";
        public String textureId="tradesmen:textures/entity/skivvies.png";
        public String hatTextureId="";
        public Vector3f hatColor=Trader.WHITE_COLOR;
        public String clothesTextureId="";
        public Vector3f clothesColor=Trader.WHITE_COLOR;
        public String Id;
        public traderObject(String id) {
            this.Id = id;
        }
        public traderObject name(String name) {
            this.name=name;
            return this;
        }
        public traderObject texture(String id) {
            this.textureId=id;
            return this;
        }
        public traderObject clothes(String id) {
            this.clothesTextureId=id;
            return this;
        }
        public traderObject animal(String id) {
            this.animal=id;
            return this;
        }
        public traderObject colorLong(@Nullable Long clothes, @Nullable Long hat) {
            if (clothes != null) {
                float b = (clothes & 255)/255.0F;
                float g = (clothes & 65535)/65535.0F;
                float r = (clothes & 16711680)/16711680.0F;
                this.clothesColor= new Vector3f(r,g,b);
            }
            if (hat != null) {
                float b = (hat & 255)/255.0F;
                float g = (hat & 65535)/65535.0F;
                float r = (hat & 16711680)/16711680.0F;
                this.hatColor= new Vector3f(r,g,b);
            }
            return this;
        }
        public traderObject colorInt(@Nullable int[] clothes, @Nullable int[] hat)
        {
            if (clothes != null && clothes.length==3) {
                this.clothesColor=new Vector3f(Math.max(Math.min(clothes[0],255),0)/255.0F,Math.max(Math.min(clothes[1],255),0)/255.0F,Math.max(Math.min(clothes[2],255),0)/255.0F);
            }
            if (hat != null && hat.length==3) {
                this.hatColor=new Vector3f(Math.max(Math.min(hat[0],255),0)/255.0F,Math.max(Math.min(hat[1],255),0)/255.0F,Math.max(Math.min(hat[2],255),0)/255.0F);
            }
            return this;
        }
        public traderObject colorFloat(@Nullable float[] clothes, @Nullable float[] hat)
        {
            if (clothes != null && clothes.length==3) {
                this.clothesColor=new Vector3f(Math.max(Math.min(clothes[0],1.0F),0),Math.max(Math.min(clothes[1],1.0F),0),Math.max(Math.min(clothes[2],1.0F),0));
            }
            if (hat != null && hat.length==3) {
                this.hatColor=new Vector3f(Math.max(Math.min(hat[0],1.0F),0),Math.max(Math.min(hat[1],1.0F),0),Math.max(Math.min(hat[2],1.0F),0));
            }
            return this;
        }
        public traderObject hat(String id) {
            this.hatTextureId=id;
            return this;
        }
        public traderObject hat(String id, int[] color) {
            return this.hat(id).colorInt(null,color);
        }
        public traderObject clothes(String id, int[] color) {
            return this.texture(id).colorInt(color,null);
        }
    }
}
