package mod.linguardium.tradesmen;

import io.github.cottonmc.libcd.api.tweaker.TweakerManager;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import mod.linguardium.tradesmen.api.TraderTweaker;
import mod.linguardium.tradesmen.api.TradesmenManager;
import mod.linguardium.tradesmen.api.objects.tradeObject;
import mod.linguardium.tradesmen.config.ModConfig;
import mod.linguardium.tradesmen.entities.InitEntities;
import mod.linguardium.tradesmen.items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Tradesmen implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "tradesmen";
    public static final String MOD_NAME = "Wandering Tradesmen";

    @Override
    public void onInitialize() {
        log(Level.INFO, "Blindly accepting all terms and conditions");
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        InitEntities.init();
        tradeObject.init();
        ModItems.init();
        TradesmenManager.ClearTradesmenList();
        WorldTickCallback.EVENT.register(TradesmenManager.INSTANCE);

        TweakerManager.INSTANCE.addTweaker("Tradesmen.TraderTweaker", TraderTweaker.INSTANCE);
        log(Level.INFO, "Welcome to The Tradesmen");
    }
    public static ModConfig getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}