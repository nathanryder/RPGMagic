package me.bumblebeee.rpgmagic.managers;

import lombok.Getter;
import me.bumblebeee.rpgmagic.RPGMagic;
import me.bumblebeee.rpgmagic.Spell;
import me.bumblebeee.rpgmagic.Wand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpellCastController {

    private @Getter Player player;
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

    public void runParticles(String mode) {
        if (mode.equalsIgnoreCase("START")) {
            Bukkit.getServer().getScheduler().runTaskAsynchronously(RPGMagic.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for (String full : wand.getSpell().getParticles()) {
                        String[] data = full.split(":");
                        StringBuilder action = new StringBuilder();
                        for (int i = 1; i < data.length; i++)
                            action.append(data[i]).append(":");

                        if (data[0].equalsIgnoreCase("start")) {
                            if (data[1].equalsIgnoreCase("delay")) {
                                try {
                                    Thread.sleep(Integer.parseInt(data[2])*1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }
                            particleManager.manageParticle(player, action.toString());
                        }
                    }
                }
            });

        } else if (mode.equalsIgnoreCase("END")) {
            Bukkit.getServer().getScheduler().runTaskAsynchronously(RPGMagic.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for (String full : wand.getSpell().getParticles()) {
                        String[] data = full.split(":");
                        StringBuilder action = new StringBuilder();
                        for (int i = 1; i < data.length; i++)
                            action.append(data[i]).append(":");

                        if (data[0].equalsIgnoreCase("end")) {
                            if (data[1].equalsIgnoreCase("delay")) {
                                try {
                                    Thread.sleep(Integer.parseInt(data[2])*1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }
                            particleManager.manageParticle(player, action.toString());
                        }
                    }
                }
            });

        } else if (mode.equalsIgnoreCase("NORMAL")) {
            Bukkit.getServer().getScheduler().runTaskAsynchronously(RPGMagic.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for (String full : wand.getSpell().getParticles()) {
                        String[] data = full.split(":");
                        StringBuilder action = new StringBuilder();
                        for (int i = 1; i < data.length; i++)
                            action.append(data[i]).append(":");

                        if (!data[0].equalsIgnoreCase("START") && !data[0].equalsIgnoreCase("END")) {
                            if (data[0].equalsIgnoreCase("delay")) {
                                try {
                                    Thread.sleep(Integer.parseInt(data[1])*1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }

                            particleManager.manageParticle(player, action.toString());
                        }
                    }
                }
            });
        }
    }

    public void runActions() {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(RPGMagic.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (String action : spell.getActions()) {
                    String ac = replaceVariables(action, wand);
                    String[] data = ac.split(":");
                    if (data[0].equalsIgnoreCase("delay")) {
                        try {
                            Thread.sleep(Integer.parseInt(data[1])*1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    actionManager.manageAction(player, ac);
                }
            }
        });
    }

    public void runCommands() {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(RPGMagic.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (String commandData : spell.getCommands()) {
                    String[] data = commandData.split(":");
                    String cmd = ChatColor.translateAlternateColorCodes('&', data[1]);
                    cmd = replaceVariables(cmd, getWand());

                    if (data[0].equalsIgnoreCase("CONSOLE")) {
                        ConsoleCommandSender sender = RPGMagic.getInstance().getServer().getConsoleSender();
                        RPGMagic.getInstance().getServer().dispatchCommand(sender, cmd);
                    } else if (data[0].equalsIgnoreCase("PLAYER")) {
                        RPGMagic.getInstance().getServer().dispatchCommand(player, cmd);
                    } else if (data[0].equalsIgnoreCase("delay")) {
                        try {
                            Thread.sleep(Integer.parseInt(data[1])*1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        RPGMagic.getInstance().getLogger().severe("INVALID MODE " + data[0] + " in line " + commandData);
                    }
                }
            }
        });
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
            }

            YamlConfiguration c = YamlConfiguration.loadConfiguration(spell.getFile());
            String var = c.getString(variable);
            if (var.contains("%"))
                var = replaceVariables(var, wand);

            int replace = evaluateMath(var);
            action = action.replace("%" + variable + "%", String.valueOf(replace));
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

    public int evaluateMath(String input) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object result;
        try {
            result = engine.eval(input);
        } catch (ScriptException e) {
            e.printStackTrace();
            return 0;
        }

        return (int)result;
    }
}
