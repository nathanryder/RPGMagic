package me.bumblebeee.rpgmagic.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ItemBuyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private @Getter Player player;
    private @Getter double price;
    private @Getter String type;
    private @Getter String args;

    public ItemBuyEvent(Player p, double price, String type, String args) {
        this.player = p;
        this.price = price;
        this.type = type;
        this.args = args;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
