var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");

TraderTweaker.addTrader("tradesmen:master_librarian", TraderTweaker.makeTrader()
                            .name("entity.minecraft.villager.librarian")
                            .texture("minecraft:textures/entity/villager/villager.png")
                            .clothes("minecraft:textures/entity/villager/type/taiga.png")
                            .hat("minecraft:textures/entity/villager/profession/librarian.png",[1.0,0.5,0.5])
                            .tiered()
                            .setTrades([
        [TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:paper@24").maxUses(16).experience(2),
        TraderTweaker.makeTrade().randomEnchantedBookTrade().secondPrice("minecraft:book"),
        TraderTweaker.makeTrade().item("minecraft:bookshelf").price(9).maxUses(12)],

        [TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:book@4").maxUses(12).experience(10),
        TraderTweaker.makeTrade().randomEnchantedBookTrade().experience(5).secondPrice("minecraft:book"),
        TraderTweaker.makeTrade().item("minecraft:lantern").price(1).maxUses(12).experience(5)],

        [TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:ink_sac@5").maxUses(12).experience(20),
        TraderTweaker.makeTrade().randomEnchantedBookTrade().experience(10).secondPrice("minecraft:book"),
        TraderTweaker.makeTrade().item("minecraft:glass@4").price(1).maxUses(12).experience(10)],

        [TraderTweaker.makeTrade().item("minecraft:emerald").price("minecraft:writable_book@2").maxUses(12).experience(30),
        TraderTweaker.makeTrade().randomEnchantedBookTrade().experience(15).secondPrice("minecraft:book"),
        TraderTweaker.makeTrade().item("minecraft:clock").price(5).experience(15),
        TraderTweaker.makeTrade().item("minecraft:compass").price(4).experience(15)],

        [TraderTweaker.makeTrade().item("minecraft:name_tag").price(20).experience(30)]],
        [2,2,2,2,2])
        );

