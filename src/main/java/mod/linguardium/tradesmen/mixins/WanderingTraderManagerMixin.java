package mod.linguardium.tradesmen.mixins;

import mod.linguardium.tradesmen.Tradesmen;
import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WanderingTraderManager.class)
public abstract class WanderingTraderManagerMixin {
    @Inject(at=@At("HEAD"),method="tick", cancellable = true)
    public void tickCanceller(CallbackInfo info){
        if (Tradesmen.getConfig().disableWanderingTrader) {
            info.cancel();
        }
    }
}
