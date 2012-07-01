package me.aaronyeager.plugins.miscellaneousplugin;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Miscellaneousplugin extends JavaPlugin implements Listener{
    
    //public static JavaPlugin plugin = null;
    private static final Logger log = Logger.getLogger("Minecraft");
    EntityDamageByEntityEvent damageevent;
    MonsterApocalypseRunnable MRRunnable;
    private long MRperiod = 20*60*10;//*150; //every 2 hours and 40 minutes (8 minecraft days)
    Player player = null;
    Random random = new Random();
    private int taskID;
    Plugin monsterApocalypsePlugin;
    //Logger log = Logger.getLogger("Minecraft");//DEBUG CODE
    
    @Override
    public void onDisable() {
        
        System.out.println(this + " is now disabled!");
    }

    @Override
    public void onEnable() {
        
        getServer().getPluginManager().registerEvents(this, this);
        MRRunnable = new MonsterApocalypseRunnable(this);
        monsterApocalypsePlugin = this.getServer().getPluginManager().getPlugin("Monster Apocalypse");
        if(monsterApocalypsePlugin != null)
        {
            this.getServer().getPluginManager().disablePlugin(monsterApocalypsePlugin);
            this.scheduleMR();
        }
        System.out.println(this + " is now enabled!");
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) { 
//       super.onEntityDeath(event);
       Player killer = ((LivingEntity)event.getEntity()).getKiller();
       if((killer != null) && !(event.getEntity() instanceof EnderDragon))
       {
           killer.giveExp(event.getDroppedExp()*2);
           event.setDroppedExp(0);
       }
                                                                                //log.log(Level.INFO, event.getEntity().toString() + " " + event.getDroppedExp());
       if(random.nextInt(100) < 6)
       {
                                                                                //log.log(Level.INFO, "String:" + event.getEntity().toString());
           //Apparently, the .toString() method for wolves returns null.  How retarded.
           //Also retarded, Ghasts aren't considered monsters (not even creatures actually)
           if((event.getEntity() instanceof Monster || 
               event.getEntity() instanceof Animals ||
               event.getEntity() instanceof Ghast) && 
             !(event.getEntity() instanceof Wolf) )            
           {
                                                                                //log.log(Level.INFO,  "Creature: " + event.getEntity().toString() +//.substring(5) + 
                                                                                //                     " Drops: " + event.getDrops().toString() +
                                                                                //                     " CreatureId: "  + CreatureType.fromName(event.getEntity().toString().substring(5)).getTypeId()  );
               event.getDrops().add(
                            new ItemStack(383,1,(short) 0,(byte) CreatureType.fromName(event.getEntity().toString().substring(5)).getTypeId())
                            );
           }
       }
    }
    
	public void scheduleMR()
        {
		BukkitScheduler scheduler = this.getServer().getScheduler();
		
                this.taskID = scheduler.scheduleSyncRepeatingTask(this, MRRunnable, 0, this.MRperiod);
		if(this.taskID == -1){
			this.log(Level.WARNING, "failed to schedule!");
		}
                
                this.taskID = scheduler.scheduleSyncRepeatingTask(this, MRRunnable, 0, this.MRperiod + 20*60*7);
		 if(this.taskID == -1){
			this.log(Level.WARNING, "failed to schedule!");
		}
	}
    
        public void log(Level level, String message)
        {
		PluginDescriptionFile desc = this.getDescription();
		log(level, desc.getName() + " v" + desc.getVersion() + ": " + message);
	}
}
