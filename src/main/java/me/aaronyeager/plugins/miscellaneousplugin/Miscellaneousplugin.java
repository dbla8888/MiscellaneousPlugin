package me.aaronyeager.plugins.miscellaneousplugin;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Miscellaneousplugin extends JavaPlugin implements Listener{
    
    private static final Logger logger = Logger.getLogger("Minecraft");
    private MonsterApocalypseRunnable MRRunnable;
    //every 2 hours and 40 minutes (8 minecraft days)
    private long MARperiod = 20*60*160; 
    Random random = new Random();
    private int taskID;
    Plugin monsterApocalypsePlugin;
    List<World> worlds;

    /**
     * onEnable is called on server startup at a stage specified in the
     * plugin.yml file under the 'startup' tag, or when it is explicitly 
     * called by the plugin manager.
     */
    @Override
    public void onEnable() {
        //register all events declared in this plugin.  Arguments 
        //for registerEvents are (plugin, class which contains the event 
        //handler).  In this case the plugin contains the event handler
        getServer().getPluginManager().registerEvents(this, this);
        
        MRRunnable = new MonsterApocalypseRunnable(this);
        monsterApocalypsePlugin = 
                this.getServer().getPluginManager().getPlugin(
                "Monster Apocalypse");
        
        if(monsterApocalypsePlugin != null)
        {
            //we want to disable the MA plugin on startup
            this.getServer().getPluginManager().disablePlugin(monsterApocalypsePlugin);
            this.scheduleMAR();
        }
        
        //set the time in the game world to morning on startup, so things
        //transition nicely
        worlds = this.getServer().getWorlds();
        for(World world : worlds)
             {
                 world.setTime(0000);
             }
        
        System.out.println(this + " is now enabled!");
    }
    
     /**
     * onDisable is called on server shutdown and when explicitly called by
     * the plugin manager.  
     */
    @Override
    public void onDisable() {
        
        System.out.println(this + " is now disabled!");
    }
    
    
    /**
     * onEntityDeath event handler runs whenever a LivingEntity class dies on
     * the server.  This does not override the standard EntityDeathEvent actions
     * such as removal from the game world, and item drops, or any custom effects
     * from other plugins.  This handler doubles the about of exp dropped for a 
     * mob kill by a player, and awards that exp directly to the player without
     * dropping the exp orbs on the ground.  It also gives all mobs a small
     * chance to drop an egg on death.
     * @param event 
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) { 

       Player killer = ((LivingEntity)event.getEntity()).getKiller();
       if((killer != null) && !(event.getEntity() instanceof EnderDragon))
       {
           killer.giveExp(event.getDroppedExp()*2);
           event.setDroppedExp(0);
       }
                                                                                //logger.log(Level.INFO, event.getEntity().toString() + " " + event.getDroppedExp());
       if(random.nextInt(100) < 6)
       {
                                                                                //logger.log(Level.INFO, "String:" + event.getEntity().toString());
           //Apparently, the .toString() method for wolves returns null.  How retarded.
           //Also retarded, Ghasts aren't considered monsters (not even creatures actually)
           if((event.getEntity() instanceof Monster || 
               event.getEntity() instanceof Animals ||
               event.getEntity() instanceof Ghast) && 
             !(event.getEntity() instanceof Wolf) )            
           {
                                                                                //logger.log(Level.INFO,  "Creature: " + event.getEntity().toString() +//.substring(5) + 
                                                                                //                     " Drops: " + event.getDrops().toString() +
                                                                                //                     " CreatureId: "  + CreatureType.fromName(event.getEntity().toString().substring(5)).getTypeId()  );
               event.getDrops().add(
                            new ItemStack(383,1,(short) 0,(byte) CreatureType.fromName(event.getEntity().toString().substring(5)).getTypeId())
                            );
           }
       }
    }
    
    /**
     * Schedules the Monster Apocalypse runnable to execute every MAR period
     */
    public void scheduleMAR()
    {
            BukkitScheduler scheduler = this.getServer().getScheduler();

            this.taskID = scheduler.scheduleSyncRepeatingTask(
                    this, MRRunnable, this.MARperiod, this.MARperiod);
            if(this.taskID == -1){
                    this.log(Level.WARNING, "failed to schedule!");
            }

    }
    
    /**
     * sends a log message to the server console
     * @param level
     * @param message 
     */
    public void log(Level level, String message)
    {
            PluginDescriptionFile desc = this.getDescription();
            logger.log(level, desc.getName() + " v" + desc.getVersion() + ": " + message);
    }
}
