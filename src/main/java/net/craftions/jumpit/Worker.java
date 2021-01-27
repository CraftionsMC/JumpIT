/*
 * Copyright (c) 2021 Ben Siebert. All rights reserved.
 */
package net.craftions.jumpit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Worker {

    public static HashMap<Player, Integer> watchers = new HashMap<Player, Integer>();
    public static HashMap<Integer, Player> pWatchers = new HashMap<Integer, Player>();

    public static void watchPlayer(Player p){
        final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Jumpit.plugin, new Runnable() {
            @Override
            public void run() {
                if(Jumpit.isPlayerOutOfRange(p)){
                    Jumpit.removeFromJumpAndRun(p);
                }else {
                    Location pLoc = p.getLocation();
                    pLoc.setY(pLoc.getY() - 1);
                    if(pLoc.getBlock().getType().equals(Material.GOLD_BLOCK)){
                        for(Location loc : Jumpit.blocks.get(p)) {
                            loc.getBlock().setType(Material.AIR);
                        }
                        pLoc.getBlock().setType(Material.DIAMOND_BLOCK);
                        Jumpit.blocks.get(p).add(pLoc);
                        pLoc.setX(pLoc.getX() + ThreadLocalRandom.current().nextInt(-4, 4));
                        pLoc.setY(pLoc.getY() + ThreadLocalRandom.current().nextInt(2));
                        pLoc.getBlock().setType(Material.GOLD_BLOCK);
                        Jumpit.blocks.get(p).add(pLoc);
                    }
                }
            }
        }, 0, 1L);

        Thread playerWatcher = new Thread("Watcher-" + p.getName()){
            @Override
            public void run() {
                while(pWatchers.containsKey(this)){
                    try {
                        sleep(50);
                        if(Jumpit.isPlayerOutOfRange(p)){
                            Jumpit.removeFromJumpAndRun(p);
                            watchers.remove(pWatchers.get(this));
                            pWatchers.remove(this);
                        }else {
                            Location pLoc = p.getLocation();
                            pLoc.setY(pLoc.getY() - 1);
                            if(pLoc.getBlock().getType().equals(Material.GOLD_BLOCK)){
                                for(Location loc : Jumpit.blocks.get(p)) {
                                    if (loc.getBlock().getType().equals(Material.DIAMOND_BLOCK)) {
                                        loc.getBlock().setType(Material.AIR);
                                        Jumpit.blocks.get(p).clear();
                                    }
                                }
                                pLoc.getBlock().setType(Material.DIAMOND_BLOCK);
                                ArrayList<Location> blocks = new ArrayList<Location>();
                                blocks.add(pLoc);
                                pLoc.setX(pLoc.getX() + ThreadLocalRandom.current().nextInt(3));
                                pLoc.setY((pLoc.getY() - 1) + ThreadLocalRandom.current().nextInt(1));
                                pLoc.getBlock().setType(Material.GOLD_BLOCK);
                                blocks.add(pLoc);
                                Jumpit.blocks.put(p, blocks);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                super.run();
            }
        };

        ArrayList<Location> blocks = new ArrayList<Location>();
        Location base = p.getLocation();
        base.setY(120 + ThreadLocalRandom.current().nextInt(40));
        base.setX(base.getX() + ThreadLocalRandom.current().nextInt(50));
        base.setY(base.getY() - 1);
        blocks.add(base);
        base.getBlock().setType(Material.DIAMOND_BLOCK);
        base.setY(base.getY() + 1);
        p.teleport(base);
        base.setX(base.getX() + ThreadLocalRandom.current().nextInt(3));
        base.setY((base.getY() - 1) + ThreadLocalRandom.current().nextInt(1));
        base.getBlock().setType(Material.GOLD_BLOCK);
        Jumpit.setMinY(p, 100);
        blocks.add(base);
        Jumpit.blocks.put(p, blocks);
        // playerWatcher.start();
        watchers.put(p, id);
        pWatchers.put(id, p);
    }
}
