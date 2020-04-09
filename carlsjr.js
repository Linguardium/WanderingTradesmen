var TraderTweaker = libcd.require("Tradesmen.TraderTweaker");
var TweakerUtils = libcd.require("libcd.util.TweakerUtils");
var newPickStack = TweakerUtils.createItemStack("minecraft:diamond_pickaxe");
newPickStack = TweakerUtils.setDamage(newPickStack, 1430);
newPickStack = TweakerUtils.setName(newPickStack, "Patched Pickaxe");
newPickStack = TweakerUtils.enchant(newPickStack, "minecraft:fortune", 1);

TraderTweaker.addTrader("tradesmen:beef_trader","entity.tradesmen.carltwo.name", "tradesmen:textures/entity/taco_man.png", "minecraft:cow",[
        TraderTweaker.makeTrade("minecraft:rotten_flesh@4",1,4,1)
        ],
    [
        TraderTweaker.makeTrade("minecraft:cooked_beef@2",[ "minecraft:emerald","minecraft:rotten_flesh@4"],4,1),
    ]);
TraderTweaker.addTrader("tradesmen:fish_trader","Sad Fishman", "", "minecraft:cod",[
            TraderTweaker.makeTrade(newPickStack,1,4,4,1)
        ],
    [
        TraderTweaker.makeTrade("minecraft:cooked_cod@1",newPickStack,4,1),
    ]);
TraderTweaker.addTrader("tradesmen:pig_trader","Wigby", "", "minecraft:pig",[
        TraderTweaker.makeTrade("minecraft:porkchop",1,4,4,1)
        ],
    [
        TraderTweaker.makeTrade("minecraft:cooked_porkchop",1,4,4,1),
    ]);