/*
 * Copyright (c) 2021 Ben Siebert. All rights reserved.
 */
package net.craftions.jumpit.events;

import net.craftions.jumpit.Jumpit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventPlayerInteract implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        try {
            if(e.getAction().equals(Action.PHYSICAL)){
                Location l = e.getPlayer().getLocation();
                l.setY(l.getY() - 1);
                Block b = l.getBlock();
                if(b.getType().equals(Material.IRON_BLOCK)){
                    Jumpit.addToJumpAndRun(e.getPlayer());
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
