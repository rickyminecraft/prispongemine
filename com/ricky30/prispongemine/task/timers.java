package com.ricky30.prispongemine.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import com.ricky30.prispongemine.prispongemine;
import ninja.leaping.configurate.ConfigurationNode;

public class timers
{
	static Map<String, Integer> timer = new HashMap<String, Integer>(256);
	static Map<String, Integer> wasted = new HashMap<String, Integer>(256);
	static Map<String, String> theworld = new HashMap<String, String>(256);
	
	private static ConfigurationNode config = null;

	public static void run() 
	{
		if (!timer.isEmpty())
		{
			for (Entry<String, Integer> Prison_name: timer.entrySet()) 
			{
				int duration = wasted.get(Prison_name.getKey());
				int currentduration = Prison_name.getValue();
				duration ++;
				if (duration < currentduration)
				{
					wasted.put(Prison_name.getKey(), duration);
					if (currentduration - duration == 300)
					{
						for (Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("prisonName", Prison_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Prison_name.getKey(), " five minute left"));
							}
						}
					}
					if (currentduration - duration == 60)
					{
						for (Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("prisonName", Prison_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Prison_name.getKey(), " one minute left"));
							}
						}
					}
					if (currentduration - duration == 30)
					{
						for (Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("prisonName", Prison_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Prison_name.getKey(), " 30 seconds left"));
							}
						}
					}
					if (currentduration - duration == 10)
					{
						for (Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("prisonName", Prison_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Prison_name.getKey(), " 10 seconds left"));
							}
						}
					}
					if (currentduration - duration == 5)
					{
						for (Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("prisonName", Prison_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Prison_name.getKey(), " 5 seconds left"));
							}
						}
					}
					if (currentduration - duration == 4)
					{
						for (Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("prisonName", Prison_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Prison_name.getKey(), " 4 seconds left"));
							}
						}
					}
					if (currentduration - duration == 3)
					{
						for (Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("prisonName", Prison_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Prison_name.getKey(), " 3 seconds left"));
							}
						}
					}
					if (currentduration - duration == 2)
					{
						for (Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("prisonName", Prison_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Prison_name.getKey(), " 2 seconds left"));
							}
						}
					}
					if (currentduration - duration == 1)
					{
						for (Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("prisonName", Prison_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Prison_name.getKey(), " 1 second left"));
							}
						}
					}
					
				}
				else
				{
					//here launch event
					Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "prisponge" +" fill " + Prison_name.getKey());
					for (Player player: Sponge.getServer().getOnlinePlayers())
					{
						if (player.getWorld().getName().equals(config.getNode("prisonName", Prison_name.getKey(), "world").getString()))
						{
							player.sendMessage(Text.of("Mine " , Prison_name.getKey(), " refill done"));
						}
					}
					wasted.put(Prison_name.getKey(), -1);
				}
			}
		}
	}
	
	public static void add(String name, int duration, String format, String world)
	{
		config = prispongemine.plugin.getConfig();
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
				theworld.put(name, world);
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
		}
	}
}
