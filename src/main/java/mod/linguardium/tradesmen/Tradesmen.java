package mod.linguardium.tradesmen;

import io.github.cottonmc.libcd.api.tweaker.TweakerManager;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import mod.linguardium.tradesmen.api.TraderTweaker;
import mod.linguardium.tradesmen.config.ModConfig;
import mod.linguardium.tradesmen.entities.InitEntities;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Tradesmen implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "tradesmen";
    public static final String MOD_NAME = "Wandering Tradesmen";


    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        InitEntities.init();
        TweakerManager.INSTANCE.addTweaker("Tradesmen.TraderTweaker", new TraderTweaker());
        log(Level.INFO, "Welcome to The Tradesmen");
    }
    public static ModConfig getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}