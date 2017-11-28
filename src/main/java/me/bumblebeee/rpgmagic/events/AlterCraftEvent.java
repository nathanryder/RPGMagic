package me.bumblebeee.rpgmagic.events;

import lombok.Getter;
import me.bumblebeee.rpgmagic.utils.Storage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AlterCraftEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private @Getter Player player;
    private @Getter ItemStack[] matrix;
    private @Getter Location location;
    private @Getter Inventory inventory;

    public AlterCraftEvent(Player p, Inventory inv) {
        this.player = p;
        this.matrix = inv.getContents();
        this.inventory = inv;
        this.location = inv.getLocation();
    }

    public void setMatrix(ItemStack[] matrix) {
        Storage.getCraftings().put(location,  matrix);
        this.matrix = matrix;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
