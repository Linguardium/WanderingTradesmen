var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");
var TweakerUtils = libcd.require("libcd.util.TweakerUtils");
var balloonpants = TweakerUtils.createItemStack("minecraft:leather_leggings");
balloonpants = TweakerUtils.enchant(balloonpants, "minecraft:protection", 4);
balloonpants = TweakerUtils.enchant(balloonpants, "minecraft:blast_protection",4);
balloonpants = TweakerUtils.enchant(balloonpants, "minecraft:projectile_protection",4);
balloonpants = TweakerUtils.enchant(balloonpants, "minecraft:fire_protection",4);
balloonpants = TweakerUtils.setName(balloonpants, "Balloon Pants");
TraderTweaker.addTrader("tradesmen:worst",
                            TraderTweaker.makeTrader()
                            .name("Worst")
                            .texture("minecraft:textures/entity/wandering_trader.png")
                            .setTrades([[
        TraderTweaker.makeTrade("minecraft:ice",5,4,1),
        ],[
        TraderTweaker.makeTrade().dyedItemTrade([1.0,0,1.0]).item(balloonpants).price(35).maxUses(1).experience("100")
        ]
        ],[1,1])
    );