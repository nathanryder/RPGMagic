package me.bumblebeee.rpgmagic.managers;

import lombok.Getter;
import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Spell;
import me.bumblebeee.rpgmagic.Wand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpellCastController {

    private static @Getter List<UUID> frozen = new ArrayList<>();
    private static @Getter Map<UUID, Location> storedLocations = new HashMap<>();

    private @Getter Player player;
    private @Getter Location castLocation = null;
    private @Getter Spell spell;
    private @Getter Wand wand;
    private @Getter SpellActionManager actionManager;
    private @Getter SpellParticleManager particleManager;

    public SpellCastController(Player player, Wand wand) {
        this.player = player;
        this.spell = wand.getSpell();
        this.wand = wand;
        this.actionManager = new SpellActionManager();
        this.particleManager = new SpellParticleManager();
    }

    public void startCasting() {
        frozen.add(player.getUniqueId());
        player.setFlying(true);
        new BukkitRunnable() {
            double i = 0;
            @Override
            public void run() {
                player.setFlying(true);
                player.teleport(player.getLocation().add(0,0.1,0));

                i += 0.1;
                if (i >= 1.5)
                    this.cancel();
            }
        }.runTaskTimer(RPGMagic.getInstance(), 1, 1);
    }

    public void process() {
        castLocation = player.getLocation();
        YamlConfiguration c = YamlConfiguration.loadConfiguration(spell.getFile());

        Bukkit.getScheduler().runTaskAsynchronously(RPGMagic.getInstance(), new Runnable() {
            int i = 0;
            @Override
            public void run() {
                for (String full : c.getStringList("actions")) {
                    String[] data = full.split(":");

                    StringBuilder action = new StringBuilder();
                    for (int i = 1; i < data.length; i++)
                        action.append(data[i]).append(":");

                    if (data[0].equalsIgnoreCase("delay")) {
                        String ac = replaceVariables(data[1], wand);
                        try {
                            Thread.sleep(Integer.parseInt(ac)*1000);
                        } catch (InterruptedException e) {
                            RPGMagic.getInstance().getLogger().severe("FAILED TO DELAY FOR " + data[1] + " SECONDS");
                            e.printStackTrace();
                        }
                    } else if (data[0].equalsIgnoreCase("storeLocation")) {
                        Location l = player.getTargetBlock((Set<Material>)null, 50).getLocation();
                        storedLocations.put(player.getUniqueId(), l);
                    }

                    if (data[0].equalsIgnoreCase("CMD"))
                        runCommand(action.toString());
                    else if (data[0].equalsIgnoreCase("ACT"))
                        runAction(action.toString());
                    else if (data[0].equalsIgnoreCase("PAR"))
                        runParticle(action.toString());

                    i++;
                }
            }
        });
    }

    public void runParticle(String full) {
        Location targetLoc = player.getTargetBlock((Set<Material>) null, 100).getLocation().add(0,1,0);
        particleManager.manageParticle(player, castLocation, targetLoc, wand, full);
    }

    public void runAction(String action) {
        String ac = replaceVariables(action, wand);
        Location targetLoc = player.getTargetBlock((Set<Material>) null, 100).getLocation().add(0,1,0);

        actionManager.manageAction(player, castLocation, targetLoc, wand, ac);
    }

    public void runCommand(String commandData) {
        String[] data = commandData.split(":");
        String cmd = ChatColor.translateAlternateColorCodes('&', data[1]);
        cmd = replaceVariables(cmd, getWand());

        if (data[0].equalsIgnoreCase("CONSOLE")) {
            ConsoleCommandSender sender = RPGMagic.getInstance().getServer().getConsoleSender();
            RPGMagic.getInstance().getServer().dispatchCommand(sender, cmd);
        } else if (data[0].equalsIgnoreCase("PLAYER")) {
            RPGMagic.getInstance().getServer().dispatchCommand(player, cmd);
        } else {
            RPGMagic.getInstance().getLogger().severe("INVALID MODE " + data[0] + " in line " + commandData);
        }
    }

    public String replaceVariables(String action, Wand wand) {
        List<String> variables = getVariablesInString(action);

        for (String variable : variables) {
            //Player/wand variables
            switch (variable) {
                case "potential":
                    action = action.replace("%" + variable + "%", String.valueOf(wand.getPower()));
                    continue;
                case "player":
                    action = action.replace("%" + variable + "%", player.getName());
                    continue;
                case "spell":
                    action = action.replace("%" + variable + "%", spell.getName());
                    continue;
                case "distance":
                    action = action.replace("%" + variable + "%", String.valueOf(wand.getDistance()));
                    continue;
                case "storedLoc":
                    Location l = getStoredLocations().get(player.getUniqueId());
                    String locData = "null";
                    if (l != null) {
                        locData = "{" + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ() + "}";
                    }

                    action = action.replace("%" + variable + "%", locData);
                    continue;
            }

            YamlConfiguration c = YamlConfiguration.loadConfiguration(spell.getFile());
            String var = c.getString(variable);
            if (var.contains("%"))
                var = replaceVariables(var, wand);

            String replace;
            try {
                replace = String.valueOf(evaluateMath(var));
            } catch (ScriptException e) {
                replace = var;
            }

            action = action.replace("%" + variable + "%", replace);
        }

        return action;
    }

    public List<String> getVariablesInString(String text) {
        List<String> vars = new ArrayList<>();
        String pattern1 = "%";
        String pattern2 = "%";

        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(text);
        while (m.find())
            vars.add(m.group(1));
        return vars;
    }

    public int evaluateMath(String input) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object result = engine.eval(input);
//        try {
//            result = engine.eval(input);
//        } catch (ScriptException e) {
//            e.printStackTrace();
//            return 0;
//        }

        return (int)result;
    }
}
