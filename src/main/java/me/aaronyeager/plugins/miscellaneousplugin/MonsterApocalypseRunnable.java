package me.aaronyeager.plugins.miscellaneousplugin;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aaron
 */
public class MonsterApocalypseRunnable implements Runnable {

    Miscellaneousplugin plugin;
    Plugin monsterApocalypsePlugin;
    PluginManager pluginManager;
    
    MonsterApocalypseRunnable(Miscellaneousplugin plugin)
        {
		this.plugin = plugin;
        }
    
    public void run() {
        pluginManager = this.plugin.getServer().getPluginManager();
        monsterApocalypsePlugin = pluginManager.getPlugin("Monster Apocalypse");
        if(pluginManager.isPluginEnabled("Monster Apocalypse"))
        {
            pluginManager.disablePlugin(monsterApocalypsePlugin);          
        }else
        {
            pluginManager.enablePlugin(monsterApocalypsePlugin);
            plugin.getServer().broadcastMessage("The moon is full...");
        }        
    }
    
}
