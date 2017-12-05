package me.bumblebeee.rpgmagic.managers;

import me.bumblebeee.rpgmagic.RPGMagic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpellParticleManager {

    public SpellParticleManager() {

    }

    public void manageParticle(Player p, String action) {
        String[] data = action.split(":");
        String function = data[0];
        String[] args = new String[5];

        for (int i = 1; i < data.length; i++)
            args[i - 1] = data[i];

        if (function.equalsIgnoreCase("loop")) {
            int time = Integer.parseInt(data[data.length-2]);
            int iterations = Integer.parseInt(data[data.length-1]);

            new BukkitRunnable() {
                int count = 0;
                @Override
                public void run() {
                    StringBuilder ac = new StringBuilder();
                    for (String s : args)
                        ac.append(s).append(":");

                    manageParticle(p, ac.toString());

                    count++;
                    if (count >= iterations)
                        this.cancel();
                }
            }.runTaskTimer(RPGMagic.getInstance(), 0, time);
        } else if (function.equalsIgnoreCase("outlineArea")) {
            p.sendMessage("TODODODO");
        }
    }
}
