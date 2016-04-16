package com.minecats.cindyk.frostwalkerdefense;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cindy on 04/16/16.
 */
public class Config {

    FrostWalkerDefense plugin;
    public int LightLevel;
    public boolean WGRegionRestrict;
    public List<String> AffectedWorldsList;

    public boolean DayRevertIce;
    public boolean NightRevertIce;

    public boolean AdminOverride;
    public boolean DebugLogging;
    public int WaitTimeForIceRevert;
    public String BypassPermission;

    public Config(FrostWalkerDefense plugin)
    {
        this.plugin= plugin;
        loadConfig();

    }

    public void loadConfig() {

        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        FillSettings();
        plugin.getLogger().info("Configuration loaded.");

    }

    public void reloadConfig()
    {
        //plugin.getConfig().options().copyDefaults(false);
        plugin.getLogger().info("Get the Updated Settings.");
        plugin.reloadConfig();
        FillSettings();
    }

    public void FillSettings()
    {
        WGRegionRestrict = true;

        //BypassPermission
        if(plugin.getConfig().get("BypassPermission")!=null)
        {
            BypassPermission = plugin.getConfig().getString("BypassPermission");
            plugin.getLogger().info("Config BypassPermission has been set to " + BypassPermission);
        }
        else
        {
            plugin.getLogger().info("Config BypassPermission Not found - setting to FrostWalkerDefense.Bypass");
            BypassPermission = "FrostWalkerDefense.Bypass";
        }

        //WaitTimeForIceRevert
        if(plugin.getConfig().get("WaitTimeForIceRevert")!=null)
        {
            WaitTimeForIceRevert = plugin.getConfig().getInt("WaitTimeForIceRevert");
            plugin.getLogger().info("Config WaitTimeForIceRevert has been set to " + WaitTimeForIceRevert);
        }
        else
        {
            plugin.getLogger().info("Config WaitTimeForIceRevert Not found - setting to 40");
            WaitTimeForIceRevert = 60;
        }


        //LightLevel
        if(plugin.getConfig().get("LightLevel")!=null)
        {
            LightLevel = plugin.getConfig().getInt("LightLevel");
            plugin.getLogger().info("Config LightLevel has been set to " + LightLevel);
        }
        else
        {
            plugin.getLogger().info("Config LightLevel Not found - setting to 12");
            LightLevel = 12;
        }


        //DebugLogging
        if(plugin.getConfig().get("DebugLogging")!=null)
        {
            DebugLogging = plugin.getConfig().getBoolean("DebugLogging");
            plugin.getLogger().info("Config DebugLogging has been set to " + DebugLogging);
        }
        else
        {
            plugin.getLogger().info("Config DebugLogging Not found - setting to false");
            DebugLogging = false;
        }

        //DayRevertIce
        if(plugin.getConfig().get("DayRevertIce")!=null)
        {
            DayRevertIce = plugin.getConfig().getBoolean("DayRevertIce");
            plugin.getLogger().info("Config DayRevertIce has been set to " + DayRevertIce);
        }
        else
        {
            plugin.getLogger().info("Config DayRevertIce Not found - setting to false");
            DayRevertIce = false;
        }

        //NightRevertIce
        if(plugin.getConfig().get("NightRevertIce")!=null)
        {
            NightRevertIce = plugin.getConfig().getBoolean("NightRevertIce");
            plugin.getLogger().info("Config DayRevertIce has been set to " + NightRevertIce);
        }
        else
        {
            plugin.getLogger().info("Config NightRevertIce Not found - setting to false");
            NightRevertIce = false;
        }



        //WGRegionRestrict
        if(plugin.getConfig().get("WGRegionRestrict")!=null)
        {
            Configuration cfg = plugin.getConfig();
            WGRegionRestrict = plugin.getConfig().getBoolean("WGRegionRestrict");
            plugin.getLogger().info("Config WGRegionRestrict is set to " + WGRegionRestrict);
        }

        //AdminOverride
        if(plugin.getConfig().get("AdminOverride")!=null)
        {
            Configuration cfg = plugin.getConfig();
            AdminOverride = plugin.getConfig().getBoolean("AdminOverride");
            plugin.getLogger().info("Config AdminOverride is set to " + AdminOverride + ".");
        }

        //AffectedWorlds
        if(plugin.getConfig().get("AffectedWorlds")!=null)
        {
            Configuration cfg = plugin.getConfig();
            AffectedWorldsList = (ArrayList<String>)plugin.getConfig().getList("AffectedWorlds");
            for(String worlds:AffectedWorldsList) {
                plugin.getLogger().info("Will limit FrostWalked on: " + worlds);
            }
        }


    }



}
