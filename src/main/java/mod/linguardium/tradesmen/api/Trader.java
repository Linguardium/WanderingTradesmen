package mod.linguardium.tradesmen.api;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.village.TradeOffers;

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
    public Int2ObjectMap<TradeOffers.Factory[]> TRADES;

    public Trader(String name, String TextureId, String clothesTextureId, Vector3f clothesColor, String hatTextureId, Vector3f hatColor, String animal, TradeOffers.Factory[] common_trades, TradeOffers.Factory[] rare_trades) {
        this.name = new TranslatableText(name);
        this.textureId=TextureId;
        this.clothesTextureId=clothesTextureId;
        this.clothesColor=clothesColor;
        this.hatTextureId=hatTextureId;
        this.hatColor=hatColor;
        this.animal=animal;
        this.TRADES = copyToFastUtilMap(ImmutableMap.of(1,common_trades,2,rare_trades));
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
}
