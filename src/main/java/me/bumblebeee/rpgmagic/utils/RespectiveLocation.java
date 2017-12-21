package me.bumblebeee.rpgmagic.utils;

import lombok.Getter;
import org.bukkit.Location;

import java.util.Arrays;

public class RespectiveLocation {

    private @Getter String forward;
    private @Getter String back;
    private @Getter String left;
    private @Getter String right;
    private @Getter Location location;

    public RespectiveLocation(Location l) {
        this.location = l;
        switch (Utils.getCardinalDirection(l)) {
            case "East":
                forward = "x:1";
                back = "x:-1";
                left = "z:-1";
                right = "z:1";
                break;
            case "West":
                forward = "x:-1";
                back = "x:1";
                left = "z:1";
                right = "z:-1";
                break;
            case "North":
                forward = "z:-1";
                back = "z:1";
                left = "x:-1";
                right = "x:1";
                break;
            case "South":
                forward = "z:1";
                back = "z:-1";
                left = "x:1";
                right = "x:-1";
                break;
        }
    }

    public Location forward() {
        String[] par = forward.split(":");
        int dir = Integer.parseInt(par[1]);
        if (par[0].equalsIgnoreCase("x")) {
            location = location.add(dir,0,0);
            return location;
        } else {
            location = location.add(0,0,dir);
            return location;
        }
    }

    public Location back() {
        String[] par = back.split(":");
        int dir = Integer.parseInt(par[1]);
        if (par[0].equalsIgnoreCase("x")) {
            location = location.add(dir,0,0);
            return location;
        } else {
            location = location.add(0,0,dir);
            return location;
        }
    }

    public Location left() {
        String[] par = left.split(":");
        int dir = Integer.parseInt(par[1]);
        if (par[0].equalsIgnoreCase("x")) {
            location = location.add(dir,0,0);
            return location;
        } else {
            location = location.add(0,0,dir);
            return location;
        }
    }
    public Location right() {
        String[] par = right.split(":");
        int dir = Integer.parseInt(par[1]);
        if (par[0].equalsIgnoreCase("x")) {
            location = location.add(dir,0,0);
            return location;
        } else {
            location = location.add(0,0,dir);
            return location;
        }
    }

    public Location forward(double amount) {
        String[] par = forward.split(":");
        int dir = Integer.parseInt(par[1]);
        if (par[0].equalsIgnoreCase("x")) {
            location = location.add(dir*amount,0,0);
            return location;
        } else {
            location = location.add(0,0,dir*amount);
            return location;
        }
    }

    public Location back(double amount) {
        String[] par = back.split(":");
        int dir = Integer.parseInt(par[1]);
        if (par[0].equalsIgnoreCase("x")) {
            location = location.add(dir*amount,0,0);
            return location;
        } else {
            location = location.add(0,0,dir*amount);
            return location;
        }
    }

    public Location left(double amount) {
        String[] par = left.split(":");
        int dir = Integer.parseInt(par[1]);
        if (par[0].equalsIgnoreCase("x")) {
            location = location.add(dir*amount,0,0);
            return location;
        } else {
            location = location.add(0,0,dir*amount);
            return location;
        }
    }
    public Location right(double amount) {
        String[] par = right.split(":");
        int dir = Integer.parseInt(par[1]);
        if (par[0].equalsIgnoreCase("x")) {
            location = location.add(dir*amount,0,0);
            return location;
        } else {
            location = location.add(0,0,dir*amount);
            return location;
        }
    }

}
