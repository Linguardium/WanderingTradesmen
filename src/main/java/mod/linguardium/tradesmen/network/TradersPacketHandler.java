package mod.linguardium.tradesmen.network;

import io.netty.buffer.Unpooled;
import mod.linguardium.tradesmen.Tradesmen;
import mod.linguardium.tradesmen.api.Trader;
import mod.linguardium.tradesmen.api.TradesmenManager;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.apache.logging.log4j.Level;

import java.io.InvalidObjectException;

import static mod.linguardium.tradesmen.Tradesmen.MOD_ID;
import static mod.linguardium.tradesmen.Tradesmen.log;

public class TradersPacketHandler {
    public static final Identifier TRADER_LIST_PACKET = new Identifier(MOD_ID, "trader_packet_full_list");
    public static final Identifier ADD_TRADER_PACKET = new Identifier(MOD_ID, "trader_packet_add");
    public static final Identifier CLEAR_TRADER_PACKET = new Identifier(MOD_ID, "trader_packet_clear");

    public static void sendAddTrader(ServerPlayerEntity player, String id, Trader t) {
        Tradesmen.log(Level.INFO,"Sending Trader "+id+" to player "+player.getName().asString());
        PacketByteBuf traderData = new PacketByteBuf(Unpooled.buffer());
        CompoundTag traderTag = null;
        try {
            traderTag = t.toClientTag(new CompoundTag());
            traderTag.putString("traderId",id);
        } catch (InvalidObjectException e) {
            Tradesmen.log(Level.ERROR,"Failed to get trader data to send to client");
        }
        traderData.writeCompoundTag(traderTag);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ADD_TRADER_PACKET, traderData);

    }
    public static void sendClearTraders(PlayerEntity player) {
        PacketByteBuf packetData = new PacketByteBuf(Unpooled.buffer());
        Tradesmen.log(Level.INFO,"Sending Clear Trader to player "+player.getName().asString());
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CLEAR_TRADER_PACKET,packetData);

    }
    public static void receiveClearTraders(PacketContext packetContext, PacketByteBuf attachedData) {
        packetContext.getTaskQueue().execute(() -> {
            TradesmenManager.Traders.clear();
        });
    }
    public static void receiveTrader(PacketContext packetContext, PacketByteBuf attachedData) {
        CompoundTag trader = attachedData.readCompoundTag();
        packetContext.getTaskQueue().execute(() -> {
            try {
                TradesmenManager.Traders.put(trader.getString("traderId"), Trader.fromClientTag(trader));
                log(Level.INFO, "Received trader: " + trader.getString("traderId"));
            } catch (InvalidObjectException e) {
                Tradesmen.log(Level.WARN, "Received invalid trader from server: " + trader.getString("traderId"));
            }
        });
    }
    public static void receiveTraders(PacketContext packetContext, PacketByteBuf attachedData) {
        Tradesmen.log(Level.INFO, "Receiving traders from server");
        CompoundTag TraderTag = attachedData.readCompoundTag();
        packetContext.getTaskQueue().execute(() -> {
            String loaded = "Received Traders from server: ";
            ListTag traders = TraderTag.getList("traders", 10); // CompoundType
            TradesmenManager.Traders.clear();
            for (Tag tag_trader : traders) {
                if (tag_trader instanceof CompoundTag) {
                    CompoundTag trader = (CompoundTag)tag_trader;
                    try {
                        TradesmenManager.Traders.put(trader.getString("traderId"), Trader.fromClientTag(trader));
                        loaded+=trader.getString("traderId")+", ";
                    } catch (InvalidObjectException e) {
                        Tradesmen.log(Level.WARN,"Received invalid trader from server: "+trader.getString("traderId"));
                    }
                }
            }
            if (loaded.contains(", ")) {
                loaded = loaded.substring(0,loaded.lastIndexOf(", "));
            }
            Tradesmen.log(Level.INFO, loaded);
        });
    }
    public static void init() {


        ClientSidePacketRegistry.INSTANCE.register(TRADER_LIST_PACKET, TradersPacketHandler::receiveTraders);
        ClientSidePacketRegistry.INSTANCE.register(ADD_TRADER_PACKET, TradersPacketHandler::receiveTrader);
        ClientSidePacketRegistry.INSTANCE.register(CLEAR_TRADER_PACKET, TradersPacketHandler::receiveClearTraders);


    }
}
