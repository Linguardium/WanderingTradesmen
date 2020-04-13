var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");

TraderTweaker.addTrader("tradesmen:ralph", TraderTweaker.makeTrader()
                            .name("Ralph")
                            .clothes("minecraft:textures/entity/villager/type/jungle.png")
                            .hat("tradesmen:textures/entity/hats/monocle.png")
                            .hat(0xFF9696)
                            .setTrades([[
        TraderTweaker.makeTrade("minecraft:llama_spawn_egg",5,4,1),
        ]],[1])
        );