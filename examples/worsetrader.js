var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");

TraderTweaker.addTrader( TraderTweaker.makeTrader("tradesmen:worst")
                            .name("Worst")
                            .texture("minecraft:textures/entity/wandering_trader.png"),
                            [
        TraderTweaker.makeTrade("minecraft:ice",5,4,1),
        ],
    [
    ]);