package mod.linguardium.tradesmen.api;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.village.TradeOffers;

public class Trader {
    private static Int2ObjectMap<TradeOffers.Factory[]> copyToFastUtilMap(ImmutableMap<Integer, TradeOffers.Factory[]> immutableMap) {
        return new Int2ObjectOpenHashMap(immutableMap);
    }

    public Text name;
    public String textureId;
    public String animal;
    public Int2ObjectMap<TradeOffers.Factory[]> TRADES;

    public Trader(String name, String textureId, String animal, TradeOffers.Factory[] common_trades, TradeOffers.Factory[] rare_trades) {
        this.name = new TranslatableText(name);
        this.textureId=textureId;
        this.animal=animal;
        this.TRADES = copyToFastUtilMap(ImmutableMap.of(1,common_trades,2,rare_trades));
    }
}
