package com.wesley27.selectivepvp;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SelectivePvP extends JavaPlugin implements Listener
{
	Logger logger = Logger.getLogger("Minecraft");
	final ArrayList<String> victims = new ArrayList<String>();

	public void onEnable() 
	{
		logger.info("SelectivePvP Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		
		getCommand("selectivepvp").setExecutor(new ReloadCommand(this));
		
		loadConfiguration();
	}

	public void onDisable() 
	{
		logger.info("SelectivePvP Disabled");
	}
	
	public void loadConfiguration()
	{
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
	}
	
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
		{
			Player attacker = (Player) event.getDamager();
			final Player victim = (Player) event.getEntity();
			
			if(!victims.contains(attacker.getName()))
			{				
				String item = attacker.getItemInHand().getType().toString();
				
				if(!getConfig().getStringList("allowed-weapons").contains(item))
				{
					//attacker.sendMessage(ChatColor.RED + "[" + ChatColor.DARK_RED + "SelectivePvP" + ChatColor.RED + "] You cannot attack someone with this item!");
					//victim.sendMessage(ChatColor.RED + "[" + ChatColor.DARK_RED + "SelectivePvP" + ChatColor.RED + "] " + ChatColor.DARK_RED + attacker.getName() + ChatColor.RED + " has tried to attack you, but didn't have a proper weapon!");
					event.setCancelled(true);
				}
				else if(getConfig().getStringList("allowed-weapons").contains(item))
				{
					if(getConfig().getBoolean("self-defence-enabled"))
					{
						victims.add(victim.getName());
						victim.sendMessage(ChatColor.RED + "[" + ChatColor.DARK_RED + "SelectivePvP" + ChatColor.RED + "] You've been attacked! You can use anything to defend yourself for " + getConfig().getInt("self-defence-time") + " seconds!");
						
						Timer t = new Timer();
						t.schedule(new TimerTask()
						{
							@Override
							public void run()
							{
								victim.sendMessage(ChatColor.RED + "[" + ChatColor.DARK_RED + "SelectivePvP" + ChatColor.RED + "] You are no longer under attack.");
								victims.remove(victim.getName());
							}
						}, (getConfig().getInt("self-defence-time") * 1000));
						
						if(victim.isDead())
						{
							t.cancel();
							victims.remove(victim.getName());
						}
					}
				}
			}
		}
	}
}
