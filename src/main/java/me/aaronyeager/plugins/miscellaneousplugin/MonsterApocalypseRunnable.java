package me.aaronyeager.plugins.miscellaneousplugin;

import java.util.Collection;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Spider;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * This class handles both the starting and stopping of the periodic monster 
 * apocalypse event.  
 * @author Aaron
 */
public class MonsterApocalypseRunnable implements Runnable {

    Miscellaneousplugin plugin;
    Plugin monsterApocalypsePlugin;
    PluginManager pluginManager;
    List<World> worlds;
    
    MonsterApocalypseRunnable(Miscellaneousplugin plugin)
        {
		this.plugin = plugin;
        }
    
    public void run() {
        
        pluginManager = this.plugin.getServer().getPluginManager();
        monsterApocalypsePlugin = pluginManager.getPlugin("Monster Apocalypse");
        worlds = plugin.getServer().getWorlds();
        
        if(pluginManager.isPluginEnabled("Monster Apocalypse"))
        {
            pluginManager.disablePlugin(monsterApocalypsePlugin);
            plugin.getServer().broadcastMessage("The darkness fades...");
             //make it daytime again, otherwise the night will actually start
            //over when the plugin disables
             for(World world : worlds)
             {
                 world.setTime(6000);
             }
             
        }else
        {
            pluginManager.enablePlugin(monsterApocalypsePlugin);
            plugin.getServer().broadcastMessage("The sky darkens...");
            
            //the night lasts 7 minutes
            this.plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, this, 20*60*7);
            
            //for each world on the server
            for(World world : worlds)
             {
                 //make it nighttime
                 world.setTime(18000);
                 
                 //remove all creepers in the world before the event starts,
                 //because lets not be rediculous.
                 Collection<Creeper> creepers = world.getEntitiesByClass(Creeper.class);
                 for(Creeper creeper: creepers)
                 {
                     creeper.remove();
                 }
                 
                 //we really only want zombies and skeletons in the first wave,
                 //so we will remove the spiders too;  They and the creepers
                 //will respawn as the night progresses.
                 Collection<Spider> spiders = world.getEntitiesByClass(Spider.class);
                 for(Spider spider : spiders)
                 {
                     spider.remove();
                 }
             }
        }        
    }    
}
