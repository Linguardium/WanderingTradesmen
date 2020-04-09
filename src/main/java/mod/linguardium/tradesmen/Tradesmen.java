package mod.linguardium.tradesmen;

import io.github.cottonmc.libcd.api.tweaker.TweakerManager;
import mod.linguardium.tradesmen.api.TraderTweaker;
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
        InitEntities.init();
        TweakerManager.INSTANCE.addTweaker("Tradesmen.TraderTweaker", new TraderTweaker());
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}