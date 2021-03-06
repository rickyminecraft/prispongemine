package com.ricky30.prispongemine.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.Sponge;
import com.ricky30.prispongemine.config.ManageConfig;
import com.ricky30.prispongemine.utility.mineMessages;
import com.ricky30.prispongemine.utility.teleport;

import ninja.leaping.configurate.ConfigurationNode;

public class Timers
{
	static Map<String, Integer> timer = new ConcurrentHashMap<String, Integer>();
	static Map<String, Integer> wasted = new ConcurrentHashMap<String, Integer>();
	static Map<String, UUID> theworld = new ConcurrentHashMap<String, UUID>();

	private static ConfigurationNode config = null;

	public static void run() 
	{
		config = ManageConfig.getConfig();
		if (!timer.isEmpty())
		{
			for (final Entry<String, Integer> Mine_name: timer.entrySet()) 
			{
				int duration = wasted.get(Mine_name.getKey());
				final int currentduration = Mine_name.getValue();
				duration ++;

				/**
				 * Modified by Jamie on 18-May-16.
				 */
				if (duration < currentduration) 
				{
					wasted.put(Mine_name.getKey(), duration);

					if (config.getNode("RemindSecondList").isVirtual()) 
					{
						config.getNode("RemindSecondList").setValue("1, 2, 3, 4, 5, 10, 15, 30, 60, 90, 120, 180, 300");
						ManageConfig.Save();
					}

					final String configReminderTimes = config.getNode("RemindSecondList").getString();
					final List<String> remindTimes = new ArrayList<String>(Arrays.asList(configReminderTimes.split(", ")));
					final int remainingTime = currentduration - duration;

					if(remindTimes.contains(Integer.toString(remainingTime))) 
					{
						mineMessages.buildMessages(Mine_name.getKey(), remainingTime);
					}
				}
				else 
				{
					//here launch event
					//teleport all player inside the mine if there is a spawn
					teleport.Doteleport(Mine_name.getKey());
					//and launch fill command
					Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "prisponge" +" fill " + Mine_name.getKey());
					mineMessages.buildMessages(Mine_name.getKey(), (currentduration - duration));
					wasted.put(Mine_name.getKey(), -1);
				}
			}
		}
		//it's better here
		mineMessages.sendMessages();
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

	public static void removeAll()
	{
		timer.clear();
		wasted.clear();
		theworld.clear();
	}
}
