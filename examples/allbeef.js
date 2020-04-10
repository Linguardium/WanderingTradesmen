var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");

TraderTweaker.addTrader( TraderTweaker.makeTrader("tradesmen:sexy_butcher")
                            .name("All Beef")
                            .clothes("tradesmen:textures/entity/clothes/apron.png")
                            .colorLong(0x4d2904,null)
                            .hat("tradesmen:textures/entity/hats/strawhat.png")
                            .animal("minecraft:cow"),
                            [
        TraderTweaker.makeTrade("minecraft:beef@4",1,4,1),
        TraderTweaker.makeTrade("minecraft:beef@8",1,4,1),
        TraderTweaker.makeTrade("minecraft:beef@64",1,4,1)
        ],
    [
        TraderTweaker.makeTrade("minecraft:cooked_beef@64",1,4,1)
    ]);