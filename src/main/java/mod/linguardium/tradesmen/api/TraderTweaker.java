package mod.linguardium.tradesmen.api;


import blue.endless.jankson.JsonObject;
import io.github.cottonmc.libcd.api.CDSyntaxError;
import io.github.cottonmc.libcd.api.tweaker.Tweaker;
import jdk.internal.jline.internal.Nullable;
import mod.linguardium.tradesmen.api.objects.tradeObject;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceNotFoundException;
import net.minecraft.village.TradeOffers;

import java.util.ArrayList;
import java.util.Collections;
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

    public void addTrader(String id, Trader t) {
        TradesmenManager.Traders.put(id,t);
    }
    public Trader makeTrader() {
        return new Trader();
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
}
