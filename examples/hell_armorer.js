var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");

TraderTweaker.addTrader("tradesmen:master_armorer", TraderTweaker.makeTrader().tiered()
                            .name("Hell Armorer")
                            .animal("minecraft:wolf", 5)
                            .texture("minecraft:textures/entity/villager/villager.png")
                            .clothes("minecraft:textures/entity/villager/profession/armorer.png")
                            .clothes(0x960000)
                            .hat("tradesmen:textures/entity/hats/eyepatch.png")
                            .addWorld("minecraft:the_nether")
                            .godMode()
                            .setTrades([
        [TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:coal@15").maxUses(16).experience(2),
        TraderTweaker.makeTrade().item("minecraft:iron_boots").price(4).maxUses(12).priceMultiplier(0.2),
        TraderTweaker.makeTrade().item("minecraft:iron_leggings").price(7).maxUses(12).priceMultiplier(0.2),
        TraderTweaker.makeTrade().item("minecraft:iron_helmet").price(5).maxUses(12).priceMultiplier(0.2),
        TraderTweaker.makeTrade().item("minecraft:iron_chestplate").price(9).maxUses(12).priceMultiplier(0.2)],

        [TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:iron_ingot@4").maxUses(12).experience(10),
        TraderTweaker.makeTrade().item("minecraft:bell").price(36).maxUses(12).priceMultiplier(0.2).experience(5),
        TraderTweaker.makeTrade().item("minecraft:chainmail_boots").price(1).maxUses(12).priceMultiplier(0.2).experience(5),
        TraderTweaker.makeTrade().item("minecraft:chainmail_leggings").price(3).maxUses(12).priceMultiplier(0.2).experience(5)],

        [TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:lava_bucket").maxUses(12).experience(20),
        TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:diamond").maxUses(12).experience(20),
        TraderTweaker.makeTrade().item("minecraft:chainmail_helmet").price(1).maxUses(12).priceMultiplier(0.2).experience(10),
        TraderTweaker.makeTrade().item("minecraft:chainmail_chestplate").price(4).maxUses(12).priceMultiplier(0.2).experience(10),
        TraderTweaker.makeTrade().item("minecraft:shield").price(5).maxUses(12).priceMultiplier(0.2).experience(10)],

        [TraderTweaker.makeTrade().randomEnchantmentItemTrade().item("minecraft:diamond_leggings").experience(15).randomPrice(14,64).maxUses(3).priceMultiplier(0.2),
        TraderTweaker.makeTrade().randomEnchantmentItemTrade().item("minecraft:diamond_boots").experience(15).randomPrice(8,64).maxUses(3).priceMultiplier(0.2)],

        [TraderTweaker.makeTrade().randomEnchantmentItemTrade().item("minecraft:diamond_helmet").experience(30).randomPrice(8,64).maxUses(3).priceMultiplier(0.2),
        TraderTweaker.makeTrade().randomEnchantmentItemTrade().item("minecraft:diamond_chestplate").experience(30).randomPrice(16,64).maxUses(3).priceMultiplier(0.2)]
        ],
        [2,2,2,2,2]));

