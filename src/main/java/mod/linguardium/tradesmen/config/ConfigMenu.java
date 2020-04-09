package mod.linguardium.tradesmen.config;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import mod.linguardium.tradesmen.Tradesmen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import java.util.Optional;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ConfigMenu implements ModMenuApi {
    @Override
    public String getModId() {
        return Tradesmen.MOD_ID;
    }

    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
        return Optional.of(AutoConfig.getConfigScreen(ModConfig.class, screen));
    }
}
