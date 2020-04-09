var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");

TraderTweaker.addTrader("default:default_trader","Dirty Hobo", "", "",[
        TraderTweaker.makeTrade("minecraft:dirt",64,1,1,0)],
    [
        TraderTweaker.makeTrade().item("minecraft:dirt@64").price(["minecraft:cobblestone","minecraft:sand"]).maxUses(4),
        TraderTweaker.makeTrade().item("minecraft:dirt").price("minecraft:emerald").count(4).maxUses(4),
        TraderTweaker.makeTrade().item("minecraft:dirt@64").price(1).maxUses(4)
    ]);
