package mod.linguardium.tradesmen.api;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.village.TradeOffers;

import java.util.ArrayList;
import java.util.List;

public class Trader {
    private static Int2ObjectMap<TradeOffers.Factory[]> copyToFastUtilMap(ImmutableMap<Integer, TradeOffers.Factory[]> immutableMap) {
        return new Int2ObjectOpenHashMap(immutableMap);
    }
    public static final Vector3f WHITE_COLOR = new Vector3f(1.0F,1.0F,1.0F);
    public Text name;
    public String textureId;
    public String animal;
    public String hatTextureId;
    public Vector3f hatColor;
    public String clothesTextureId;
    public Vector3f clothesColor;
    public List<Integer> tierTradeCount=new ArrayList<>();
    public List<List<TradeOffers.Factory>> TRADES;

    public Trader(String name, String TextureId, String clothesTextureId, Vector3f clothesColor, String hatTextureId, Vector3f hatColor, String animal, List<List<TradeOffers.Factory>> trades) {
        this(name,clothesTextureId,clothesTextureId,clothesColor,hatTextureId,hatColor,animal,trades, Lists.newArrayList(3,1));
    }
    public Trader(String name, String TextureId, String clothesTextureId, Vector3f clothesColor, String hatTextureId, Vector3f hatColor, String animal, List<List<TradeOffers.Factory>> trades, List<Integer> tradeCount) {
        this.name = new TranslatableText(name);
        this.textureId=TextureId;
        this.clothesTextureId=clothesTextureId;
        this.clothesColor=clothesColor;
        this.hatTextureId=hatTextureId;
        this.hatColor=hatColor;
        this.animal=animal;
        this.tierTradeCount = tradeCount;
        /*for (int i=1;i<=trades.size();i++) {
            this.TRADES.putIfAbsent(i,trades[i]);
        }
        this.TRADES=copyToFastUtilMap(ImmutableMap.copyOf(TRADES)); // make it immutable? */
        this.TRADES=trades;
    }
    public Trader() {
        this.name=null;
        this.textureId="tradesmen:textures/entity/skivvies.png";
        this.clothesTextureId="minecraft:textures/entity/wandering_trader.png";
        this.clothesColor=WHITE_COLOR;
        this.hatTextureId="";
        this.hatColor=WHITE_COLOR;
        this.animal="minecraft:trader_llama";
    }

    public void setCounts(List<Integer> tradeCounts) {
        this.tierTradeCount=tradeCounts;
    }
}
