package net.craftions.jumpit;

import net.craftions.jumpit.events.EventPlayerInteract;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class Jumpit extends JavaPlugin {

    protected static ArrayList<Player> inJumpAndRun = new ArrayList<Player>();
    protected static HashMap<Player, Integer> minY = new HashMap<Player, Integer>();
    protected static HashMap<Player, ArrayList<Location>> blocks = new HashMap<Player, ArrayList<Location>>();
    public static Jumpit plugin;

    @Override
    public void onEnable() {
        plugin = this;
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new EventPlayerInteract(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static boolean isInJumpAndRun(Player p){
        return inJumpAndRun.contains(p);
    }

    public static boolean addToJumpAndRun(Player p){
        if(!inJumpAndRun.contains(p)){
            inJumpAndRun.add(p);
            Worker.watchPlayer(p);
            return true;
        }
        return false;
    }

    public static boolean removeFromJumpAndRun(Player p){
        if(inJumpAndRun.contains(p)){
            try{
                inJumpAndRun.remove(p);
                minY.remove(p);
                Bukkit.getScheduler().cancelTask(Worker.watchers.get(p));
                for(Location loc : Jumpit.blocks.get(p)) {
                    loc.getBlock().setType(Material.AIR);
                }
                Jumpit.blocks.remove(p);
            }catch (NullPointerException ex){

            }
            return true;
        }
        return false;
    }

    public static boolean isPlayerOutOfRange(Player p){
        if(minY.containsKey(p)){
            if(p.getLocation().getY() > minY.get(p)){
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }
    }

    public static void setMinY(Player p, int y){
        minY.put(p, y);
    }
}
