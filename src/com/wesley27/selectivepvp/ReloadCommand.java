package com.wesley27.selectivepvp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor
{
	SelectivePvP plugin;

    ReloadCommand(SelectivePvP plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
    {
        String cmd = command.getName();

        if (cmd.equals("selectivepvp")) 
        {
            if (args.length >= 1)
                if (args[0].equals("reload")) 
                {
                    if (sender instanceof Player) 
                    {
                        Player player = (Player) sender;

                        if (!(player.hasPermission("selectivepvp.reload"))) 
                        {
                            player.sendMessage(ChatColor.RED + "[SelectivePvP] You don't have Permissions to do this.");
                            return true;
                        }
                    }

                    plugin.reloadConfig();
                    sender.sendMessage(ChatColor.RED + "[SelectivePvP] Config reloaded.");

                    return true;
                }
        }
        return false;
    }
}
