var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");


TraderTweaker.addTrader("tradesmen:sexy_butcher", TraderTweaker.makeTrader()
                            .name("All Beef")
                            .clothes("tradesmen:textures/entity/clothes/apron.png")
                            .clothes(0x4d2904)
                            .hat("tradesmen:textures/entity/hats/strawhat.png")
                            .animal("minecraft:cow")
                            .setTrades([[
        TraderTweaker.makeTrade("minecraft:beef@4",1,4,1),
        TraderTweaker.makeTrade("minecraft:beef@8",1,4,1),
        TraderTweaker.makeTrade("minecraft:beef@64",1,4,1)
        ],
    [
        TraderTweaker.makeTrade("minecraft:cooked_beef@64",1,4,1)
    ]],[1,1]));