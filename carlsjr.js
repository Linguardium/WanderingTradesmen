var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");

TraderTweaker.addTrader("tradesmen:beef_trader","entity.tradesmen.carltwo.name", "tradesmen:textures/entity/taco_man.png", "minecraft:cow",[
        TraderTweaker.makeTrade("minecraft:rotten_flesh",1,4,4,1)
        ],
    [
        TraderTweaker.makeTrade("minecraft:cooked_beef",1,4,4,1),
    ]);
