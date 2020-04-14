var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");

TraderTweaker.addTrader("tradesmen:trade_machine",
                            TraderTweaker.makeTrader()
                            .texture("minecraft:textures/entity/wandering_trader.png")
                            .setTrades([[
        TraderTweaker.makeTrade().randomPrice(3,7).randomSaleItem().count(3).maxUses(3),
        TraderTweaker.makeTrade().randomPrice(3,7).randomSaleItem().count(3).maxUses(3),
        TraderTweaker.makeTrade().randomPrice(3,7).randomSaleItem().count(3).maxUses(3),
        TraderTweaker.makeTrade().randomPrice(3,7).randomSaleItem().count(3).maxUses(3),
        TraderTweaker.makeTrade().randomPrice(3,7).randomSaleItem().count(3).maxUses(3)
        ],
        [
        TraderTweaker.makeTrade().item("minecraft:emerald").randomCount(1,10).randomPrice(1,10).randomPriceItem().maxUses(3),
        TraderTweaker.makeTrade().item("minecraft:emerald").randomCount(1,10).randomPrice(1,10).randomPriceItem().maxUses(3),
        TraderTweaker.makeTrade().item("minecraft:emerald").randomCount(1,10).randomPrice(1,10).randomPriceItem().maxUses(3),
        TraderTweaker.makeTrade().item("minecraft:emerald").randomCount(1,10).randomPrice(1,10).randomPriceItem().maxUses(3),
        TraderTweaker.makeTrade().item("minecraft:emerald").randomCount(1,10).randomPrice(1,10).randomPriceItem().maxUses(3)
        ]
        ],[5,5])
    );