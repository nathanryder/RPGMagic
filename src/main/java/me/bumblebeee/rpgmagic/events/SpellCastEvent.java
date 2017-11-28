package me.bumblebeee.rpgmagic.events;

import lombok.Getter;
import me.bumblebeee.rpgmagic.Wand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpellCastEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private @Getter Player player;
    private @Getter Wand wand;
    private @Getter String spell;

    public SpellCastEvent(Player p, Wand wand) {
        this.player = p;
        this.wand = wand;
        this.spell = wand.getSpell();
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
