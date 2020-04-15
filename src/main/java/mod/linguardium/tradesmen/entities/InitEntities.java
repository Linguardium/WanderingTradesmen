package mod.linguardium.tradesmen.entities;

import mod.linguardium.tradesmen.Tradesmen;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class InitEntities {
    public static final EntityType<TradesmenEntity> TRADESMEN_ENTITY_TYPE =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier(Tradesmen.MOD_ID, "tradesmen_seller"),
                    FabricEntityTypeBuilder.create(EntityCategory.MISC, TradesmenEntity::new).size(EntityDimensions.fixed(0.6F, 1.95F)).build()
            );
    public static final EntityType<TradesmenEntity> SECRET_TRADESMEN_ENTITY_TYPE =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier(Tradesmen.MOD_ID, "tradesmen_seller_secret"),
                    FabricEntityTypeBuilder.create(EntityCategory.MISC, TradesmenEntity::new).size(EntityDimensions.fixed(0.6F, 1.95F)).build()
            );
    public static void init() {

    }
}
