package com.minecats.cindyk.frostwalkerdefense;

/**
 * Created by cindy on 4/16/2016.
 */

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.milkbowl.vault.permission.Permission;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;


public class FrostWalkerDefense extends JavaPlugin implements Listener {


    static boolean Running = true;
    static boolean AdminOverride = true;

    static Config config;
    Commands commands;

    // Vault --------------------------------
    public static Permission permission = null;

    //WorldGuard ----------------------------
    public WorldGuardPlugin worldGuard = null;

    @Override
    public void onEnable() {
        super.onEnable();

        getLogger().info("Enabling FrostWalkerDefense... ");

        config = new Config(this);
        commands = new Commands();

        // Set up Vault
        if(!setupPermissions()) {
            getLogger().info(String.format("[%s] - *******************************************", getDescription().getName()));
            getLogger().info(String.format("[%s] - Could not find Vault dependency, disabling.", getDescription().getName()));
            getLogger().info(String.format("[%s] - *******************************************", getDescription().getName()));

            this.getPluginLoader().disablePlugin(this);
            return;
        }

        worldGuard = (WorldGuardPlugin)loadPlugin("WorldGuard");

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(this,this);
        getCommand("frostwalkerdefense").setExecutor(commands);
    }

    @Override
    public void onDisable() {
        super.onDisable();


        getLogger().info("Disabling FrostWalkerDefense... ");
    }




    @EventHandler(priority = EventPriority.HIGH)
    public void entityBlockFormEvent( EntityBlockFormEvent event){

        Block block = event.getBlock();

        final Location blockloc;
        final BlockState bs;

        if(event.getEntity() instanceof Player) {

            Player player = (Player)(event.getEntity());


            //Check WorldGuard Restrictions
            if(config.WGRegionRestrict){
                if (config.DebugLogging) {
                    getLogger().info("WorldGuard Check...");
                }
                if(worldGuard != null && !worldGuard.canBuild(player,player.getLocation())){
                    if (config.DebugLogging) {
                        getLogger().info("WorldGuard Check - Cancelling Ice : " + event.getEntity().getName() + " is not in an allowed to build here.");
                    }
                    event.setCancelled(true);
                    return;
                }
            }

            //Check Worlds....
            boolean found = false;
            for(String allowedWorld : config.AffectedWorldsList){
                if(player.getLocation().getWorld().getName().compareToIgnoreCase(allowedWorld)==0){
                    found = true;
                }
            }

            if(!found){
                if (config.DebugLogging) {
                    getLogger().info("Player: " + event.getEntity().getName() + " is not in an allowed world: " + player.getLocation().getWorld().getName());
                }
                return;
            }

            //Ignore OP is AdminOverride is on.
            if(config.AdminOverride && player.isOp() ) {
                if (config.DebugLogging) {
                    getLogger().info("Admin Bypass for Player: " + event.getEntity().getName());
                }
                return;
            };

            //ByPass Permissions
            if(permission.has(player,config.BypassPermission)){
                if (config.DebugLogging) {
                    getLogger().info("Permission Bypass for Player: " + event.getEntity().getName());
                }
                return;
            };

            //LightLevel and Day / Night Checks....
            if ( (block.getLightLevel() >= config.LightLevel && !config.DayRevertIce ) || !config.NightRevertIce) {

                if (config.DebugLogging) {
                    getLogger().info("Ice forming from Player: " + event.getEntity().getName());
                    getLogger().info("Block : " + event.getBlock().getType().name() + " LightLevel: " + block.getLightLevel());
                    getLogger().info("Block Loc : " + event.getBlock().getLocation().toString());
                    getLogger().info("No Action taken... " + block.getLightLevel() + " DayRevertIce="+ config.DayRevertIce +" NightRevertIce="+config.NightRevertIce);
                }

                //OR set block back to STATIONARY_WATER
            }
            else{
                //Player is causing the ice to form.. cancel it.
                if (config.DebugLogging) {
                    getLogger().info("Will fix the ice forming from Player: " + event.getEntity().getName());
                    getLogger().info("Block : " + event.getBlock().getType().name() + " LightLevel: " + block.getLightLevel());
                    getLogger().info("Block Loc : " + event.getBlock().getLocation().toString());
                    getLogger().info("NewBlockState : " + event.getNewState().getBlock().getType().name());
                }

                blockloc = block.getLocation();
                bs = block.getState();


                // Create the task anonymously and schedule to run it once, after 20 ticks
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        // What you want to schedule goes here
                        if (config.DebugLogging) {
                            getLogger().info("Reverting Ice to Water to prevent server overload");
                        }

                        Block fixblock = getServer().getWorld(blockloc.getWorld().getName()).getBlockAt(blockloc);
                        fixblock.setType(Material.STATIONARY_WATER);

                    }

                }.runTaskLater(this, config.WaitTimeForIceRevert);
            }
        }

    }

    private boolean setupPermissions()
    {
        Plugin plug = getServer().getPluginManager().getPlugin("Vault");
        if(plug == null)
            return false;

        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    /*
	 * Check if a plugin is loaded/enabled. Returns the plugin and print message to console if so, returns null otherwise
	 */
    private Plugin loadPlugin(String p) {
        Plugin plugin = this.getServer().getPluginManager().getPlugin(p);
        if (plugin != null && plugin.isEnabled()) {
            getLogger().info(" Using " + plugin.getDescription().getName() + " (v" + plugin.getDescription().getVersion() + ")");
            return plugin;
        }
        return null;
    }



}
