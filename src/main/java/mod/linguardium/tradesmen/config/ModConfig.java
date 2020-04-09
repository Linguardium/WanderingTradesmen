package mod.linguardium.tradesmen.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config.Gui.Background("minecraft:textures/block/orange_shulker_box.png")
@Config(name = "tradesmen")
public class ModConfig implements ConfigData {

    public boolean disableWanderingTrader = false;
    public boolean despawnAnimals = true;
    @ConfigEntry.Gui.Tooltip
    public int spawnDelay = 24000;

    @ConfigEntry.BoundedDiscrete(max=100,min=1)
    public int spawnChance = 25;


}