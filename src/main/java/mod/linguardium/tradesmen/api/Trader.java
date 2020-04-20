package mod.linguardium.tradesmen.api;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mod.linguardium.tradesmen.api.objects.ParseColor;
import mod.linguardium.tradesmen.api.objects.tradeObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.village.TradeOffers;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

import static mod.linguardium.tradesmen.api.objects.ObjectConversion.toJavaObject;

public class Trader {
    private static Int2ObjectMap<TradeOffers.Factory[]> copyToFastUtilMap(ImmutableMap<Integer, TradeOffers.Factory[]> immutableMap) {
        return new Int2ObjectOpenHashMap(immutableMap);
    }
    public static final float[] WHITE_COLOR = new float[]{1.0F,1.0F,1.0F};
    
    public Text name;
    public String textureId;
    public String animal;
    public int animalCount;
    public String hatTextureId;
    public float[] hatColor;
    public String clothesTextureId;
    public float[] clothesColor;
    public List<Integer> tierTradeCount=new ArrayList<>();
    public List<List<TradeOffers.Factory>> TRADES;
    List<List<tradeObject>> tObjTrades = new ArrayList<>();
    public Boolean isTiered = false;
    public List<String> allowedWorlds = new ArrayList<>();
    public boolean godMode;


    public Trader(String name, String TextureId, String clothesTextureId, float[] clothesColor, String hatTextureId, float[] hatColor, String animal, List<List<TradeOffers.Factory>> trades, List<Integer> tradeCount, Boolean tiered) {
        this.name = new TranslatableText(name);
        this.textureId=TextureId;
        this.clothesTextureId=clothesTextureId;
        this.clothesColor=clothesColor;
        this.hatTextureId=hatTextureId;
        this.hatColor=hatColor;
        this.animal=animal;
        this.tierTradeCount = tradeCount;
        this.isTiered=tiered;
        this.TRADES=trades;
        this.godMode=false;
        this.animalCount=1;
    }
    public Trader() {
        this.name=new LiteralText("Tradesman");;
        this.textureId="tradesmen:textures/entity/skivvies.png";
        this.clothesTextureId="minecraft:textures/entity/wandering_trader.png";
        this.clothesColor=WHITE_COLOR;
        this.hatTextureId="";
        this.hatColor=WHITE_COLOR;
        this.animal="minecraft:trader_llama";
        this.allowedWorlds=new ArrayList<>();
        this.godMode=false;
        this.animalCount=1;
    }
    public Trader name(String trader_name) {
        this.name=new TranslatableText(trader_name);
        return this;
    }
    public Trader texture(String texId) {
        this.textureId=texId;
        return this;
    }
    public Trader clothes(String texId, Object color) throws InvalidObjectException {
        return this.clothes(texId).clothes(color);
    }
    public Trader clothes(Object color) throws InvalidObjectException {
        this.clothesColor=ParseColor.toFloat(color);
        return this;
    }
    public Trader clothes(String texId) {
        this.clothesTextureId=texId;
        return this;
    }
    public Trader hat(String texId, Object color) throws InvalidObjectException {
        return this.hat(texId).hat(color);
    }
    public Trader hat(Object color) throws InvalidObjectException {
        this.hatColor=ParseColor.toFloat(color);
        return this;
    }
    public Trader hat(String texId) {
        this.hatTextureId=texId;
        return this;
    }
    public Trader animal(String animalId) {
        this.animal=animalId;
        return this;
    }
    public Trader animal(String animalId, Integer count) {
        this.animal=animalId;
        this.animalCount=count;
        return this;
    }
    public Trader tiered() {
        return tiered(true);
    }
    public Trader tiered(Boolean isTiered) {
        this.isTiered=isTiered;
        return this;
    }
    public Trader addWorld(String id) {
        this.allowedWorlds.add(id);
        return this;
    }
    public Trader godMode() {
        return godMode(true);
    }
    public Trader godMode(boolean b) {
        this.godMode=b;
        return this;
    }
    public Trader setTrades(List<List<tradeObject>> trades, List<Integer> tradeCounts) {
        List<List<TradeOffers.Factory>> tradeFactories = new ArrayList<List<TradeOffers.Factory>>();
        for (int i=0;i<trades.size();i++){
            // Gotta do this because of the GD ScriptObjectMirror that nashborn keeps forcing on me
            List<tradeObject> tier = (List<tradeObject>)toJavaObject(trades.get(i));
            tObjTrades.add(tier);
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
        this.assignTrades(tradeFactories,tradeCounts);
        return this;
    }

    private void assignTrades(List<List<TradeOffers.Factory>> trades, List<Integer> tradeCount) {
        this.TRADES=trades;
        this.tierTradeCount=tradeCount;
    }
    public static Trader fromClientTag(CompoundTag tag) throws InvalidObjectException {
        Trader t = new Trader()
                .name(tag.getString("name"))
                .texture(tag.getString("textureId"))
                .clothes(tag.getString("clothesId"))
                .clothes(tag.getInt("clothesColor"))
                .hat(tag.getString("hatId"))
                .hat(tag.getInt("hatColor"))
                .tiered(tag.getBoolean("tiered"));
        List<List<tradeObject>> trades = new ArrayList<>();
        ListTag tradeSetsTag = tag.getList("trades",9); // List Tag Type
        for (Tag tradesTag : tradeSetsTag) {
            List<tradeObject> tObjList = new ArrayList<>();
            if (tradesTag instanceof ListTag) {
                ListTag tradesListTag = (ListTag)tradesTag;
                for (Tag tradeObjectTag : tradesListTag) {
                    if (tradeObjectTag instanceof CompoundTag) {
                        tradeObject tObj = new tradeObject();
                        tObj.tag = ((CompoundTag) tradeObjectTag).copy();
                        tObj.factoryId=((CompoundTag) tradeObjectTag).getString("factoryId");
                        tObjList.add(tObj);
                    }
                }
            }
            if (tObjList.size()>0) {
                trades.add(tObjList);
            }
        }
        List<Integer> counts = new ArrayList<>();
        for (int c : tag.getIntArray("tradeCount")) {
            counts.add(c);
        }
        t.setTrades(trades, counts);
        return t;
    }
    public CompoundTag toClientTag(CompoundTag tag) throws InvalidObjectException {
        tag.putString("name",this.name.asString());
        tag.putString("textureId",this.textureId);
        tag.putString("clothesId",this.clothesTextureId);
        tag.putInt("clothesColor",ParseColor.toInt(this.clothesColor));
        tag.putString("hatId",this.hatTextureId);
        tag.putInt("hatColor",ParseColor.toInt(this.hatColor));
        tag.putBoolean("tiered",this.isTiered);
        ListTag trades = new ListTag();
        for (List<tradeObject> tradeObjects : this.tObjTrades) {
            ListTag tradeX = new ListTag();
            for (tradeObject tObject : tradeObjects) {
                CompoundTag tObjectTag = tObject.tag.copy();
                tObjectTag.putString("factoryId",tObject.factoryId);
                tradeX.add(tObjectTag);
            }
            if (tradeX.size()>0) {
                trades.add(tradeX);
            }
        }
        tag.put("trades",trades);
        tag.putIntArray("tradeCount",this.tierTradeCount);
        return tag;
    }

}
