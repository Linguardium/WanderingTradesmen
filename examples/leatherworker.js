var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");

TraderTweaker.addTrader("tradesmen:leatherworker", TraderTweaker.makeTrader()
                            .name("entity.minecraft.villager.leatherworker")
                            .texture("minecraft:textures/entity/villager/villager.png")
                            .clothes("minecraft:textures/entity/villager/type/taiga.png")
                            .hat("minecraft:textures/entity/villager/profession/leatherworker.png")
                            .setTrades([
        [TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:leather@6").maxUses(16).experience(2),
        TraderTweaker.makeTrade().randomDyedItemTrade().item("minecraft:leather_leggings").maxUses(12).price(3).priceMultiplier(0.2),
        TraderTweaker.makeTrade().randomDyedItemTrade().item("minecraft:leather_chestplate").maxUses(12).price(7).priceMultiplier(0.2)],

        [TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:flint@26").maxUses(12).experience(10),
        TraderTweaker.makeTrade().randomDyedItemTrade().item("minecraft:leather_helmet").maxUses(12).price(5).priceMultiplier(0.2).experience(5),
        TraderTweaker.makeTrade().randomDyedItemTrade().item("minecraft:leather_boots").maxUses(12).price(4).priceMultiplier(0.2).experience(5)],

        [TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:rabbit_hide@9").maxUses(12).experience(20),
        TraderTweaker.makeTrade().randomDyedItemTrade().item("minecraft:leather_chestplate").maxUses(12).price(7).priceMultiplier(0.2)],

        [TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:scute@4").maxUses(12).experience(30),
        TraderTweaker.makeTrade().item("minecraft:leather_horse_armor").maxUses(12).price(6).priceMultiplier(0.2).experience(15)],

        [TraderTweaker.makeTrade().item("minecraft:saddle").maxUses(12).price(6).priceMultiplier(0.2).experience(30),
        TraderTweaker.makeTrade().randomDyedItemTrade().item("minecraft:leather_helmet").maxUses(12).price(5).priceMultiplier(0.2).experience(30)]],

        [2,2,2,2,2])
        );

