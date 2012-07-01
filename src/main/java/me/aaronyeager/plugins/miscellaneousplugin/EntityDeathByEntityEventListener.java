/*
 * 
 */
package me.aaronyeager.plugins.miscellaneousplugin;

import java.util.Random;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Aaron
 */
public class EntityDeathByEntityEventListener implements Listener {
    public static JavaPlugin plugin = null;
    EntityDamageByEntityEvent damageevent;
    Player player = null;
    //Logger log = Logger.getLogger("Minecraft");//DEBUG CODE
    Random random = new Random();
    
    EntityDeathByEntityEventListener(JavaPlugin plug) {
       plugin = plug;
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) { 

       Player killer = ((LivingEntity)event.getEntity()).getKiller();
       if((killer != null) && !(event.getEntity() instanceof EnderDragon))
       {
           killer.giveExp(event.getDroppedExp()*2);
           event.setDroppedExp(0);
       }
                                                                                //log.log(Level.INFO, event.getEntity().toString() + " " + event.getDroppedExp());
       if(random.nextInt(100) < 5)
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
}
