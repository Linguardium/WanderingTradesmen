var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");

TraderTweaker.addTrader( TraderTweaker.makeTrader("tradesmen:ralph")
                            .name("Ralph")
                            .clothes("minecraft:textures/entity/villager/type/jungle.png")
                            .hat("tradesmen:textures/entity/hats/monocle.png",[255,150,150]),
                            [
        TraderTweaker.makeTrade("minecraft:llama_spawn_egg",5,4,1),
        ],
    [
    ]);