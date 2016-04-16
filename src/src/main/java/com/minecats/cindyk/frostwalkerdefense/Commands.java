package com.minecats.cindyk.frostwalkerdefense;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by cindy on 4/16/2016.
 */
public class Commands implements Listener, CommandExecutor {



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if(cmd.getName().compareToIgnoreCase("FrostWalkerDefense")==0)
        {
            if(sender instanceof Player)
            {
                Player p = (Player)sender;
                if( p.hasPermission("FrostWalkerDefense.Admin") || p.isOp() )
                {

                    if(args.length == 1)
                    {
                        if(args[0].compareToIgnoreCase("reload")==0)
                        {

                            sender.sendMessage("Reloading FrostWalkerDefense config.yml...");
                            FrostWalkerDefense.config.reloadConfig();
                            return true;
                        }

                    }
                    sender.sendMessage("FrostWalkerDefense : Keeping Mojang idiocy at bay since 2014");
                    sender.sendMessage("/FrostWalkerDefense reload");
                }
            }
        }



        sender.sendMessage("Done");
        return true;

    }
}
