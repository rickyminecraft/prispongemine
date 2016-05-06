package com.ricky30.prispongemine.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import com.ricky30.prispongemine.utility.teleport;

public class timers
{
	static Map<String, Integer> timer = new ConcurrentHashMap<String, Integer>();
	static Map<String, Integer> wasted = new HashMap<String, Integer>();
	static Map<String, UUID> theworld = new HashMap<String, UUID>();

	public static void run() 
	{
		if (!timer.isEmpty())
		{
			for (final Entry<String, Integer> Mine_name: timer.entrySet()) 
			{
				int duration = wasted.get(Mine_name.getKey());
				final int currentduration = Mine_name.getValue();
				duration ++;
				if (duration < currentduration)
				{
					wasted.put(Mine_name.getKey(), duration);
					if (currentduration - duration == 300)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getUniqueId().equals(theworld.get(Mine_name.getKey())))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " five minute left"));
							}
						}
					}
					if (currentduration - duration == 60)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getUniqueId().equals(theworld.get(Mine_name.getKey())))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " one minute left"));
							}
						}
					}
					if (currentduration - duration == 30)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getUniqueId().equals(theworld.get(Mine_name.getKey())))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 30 seconds left"));
							}
						}
					}
					if (currentduration - duration == 10)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getUniqueId().equals(theworld.get(Mine_name.getKey())))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 10 seconds left"));
							}
						}
					}
					if (currentduration - duration == 5)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getUniqueId().equals(theworld.get(Mine_name.getKey())))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 5 seconds left"));
							}
						}
					}
					if (currentduration - duration == 4)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getUniqueId().equals(theworld.get(Mine_name.getKey())))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 4 seconds left"));
							}
						}
					}
					if (currentduration - duration == 3)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getUniqueId().equals(theworld.get(Mine_name.getKey())))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 3 seconds left"));
							}
						}
					}
					if (currentduration - duration == 2)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getUniqueId().equals(theworld.get(Mine_name.getKey())))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 2 seconds left"));
							}
						}
					}
					if (currentduration - duration == 1)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getUniqueId().equals(theworld.get(Mine_name.getKey())))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 1 second left"));
							}
						}
					}

				}
				else
				{
					//here launch event
					//teleport all player inside the mine if there is a spawn
					teleport.Doteleport(Mine_name.getKey());
					//and launch fill command
					Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "prisponge" +" fill " + Mine_name.getKey());
					for (final Player player: Sponge.getServer().getOnlinePlayers())
					{
						if (player.getWorld().getUniqueId().equals(theworld.get(Mine_name.getKey())))
						{
							player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " refill done"));
						}
					}
					wasted.put(Mine_name.getKey(), -1);
				}
			}
		}
	}

	public static void add(String name, int duration, String format, UUID uuid)
	{
		if (timer.get(name) == null)
		{
			if (format.equals("SECONDS") || format.equals("MINUTES") || format.equals("HOURS") || format.equals("DAYS"))
			{
				int durationsecond = 0;
				if (format.equals("SECONDS"))
				{
					durationsecond = duration;
				}
				else if (format.equals("MINUTES"))
				{
					durationsecond = (60 * duration);
				}
				else if (format.equals("HOURS"))
				{
					durationsecond = (3600 * duration);
				}
				else if (format.equals("DAYS"))
				{
					durationsecond = (86400 * duration);
				}

				timer.put(name, durationsecond);
				theworld.put(name, uuid);
				wasted.put(name, -1);
			}
		}
	}

	public static void remove(String name)
	{
		if (timer.get(name) != null)
		{
			timer.remove(name);
			wasted.remove(name);
			theworld.remove(name);
		}
	}
}
