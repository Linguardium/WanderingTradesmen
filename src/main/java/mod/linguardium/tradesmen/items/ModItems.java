package mod.linguardium.tradesmen.items;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static mod.linguardium.tradesmen.Tradesmen.MOD_ID;
import static mod.linguardium.tradesmen.entities.InitEntities.TRADESMEN_ENTITY_TYPE;

public class ModItems {
    public static final TradesmenSpawnEgg SPAWN_EGG = new TradesmenSpawnEgg(TRADESMEN_ENTITY_TYPE,0x0075db, 0xffe203);
    public static final SpawnEggSpawner SPAWN_EGG_SPAWNER = new SpawnEggSpawner();
    public static ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID,"spawneggs")).icon(()->new ItemStack(Items.EMERALD)).appendItems((list)->
    {
        list.add(new ItemStack(SPAWN_EGG_SPAWNER));
    }).build();
    public static void init() {
        Registry.register(Registry.ITEM,new Identifier(MOD_ID,"spawn_egg"),SPAWN_EGG);
        Registry.register(Registry.ITEM,new Identifier(MOD_ID, "spawn_egg_spawner"),SPAWN_EGG_SPAWNER);
    }
}
