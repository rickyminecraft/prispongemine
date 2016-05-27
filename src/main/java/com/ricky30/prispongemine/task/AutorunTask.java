package com.ricky30.prispongemine.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ricky30.prispongemine.prispongemine;
import ninja.leaping.configurate.ConfigurationNode;

public class AutorunTask
{
	private static ConfigurationNode config = null;
	private static Map<String, Integer> Mine_startupdelay = new HashMap<String, Integer>();
	private static Map<String, Integer> Mine_actualdelay = new HashMap<String, Integer>();
	private static Map<String, Integer> Mine_set = new ConcurrentHashMap<String, Integer>();
	private static boolean startfillsets;

	public static void Init()
	{
		config = prispongemine.plugin.getConfig();
		for (final Object text: config.getNode("mineName").getChildrenMap().keySet())
		{
			if (config.getNode("mineName", text.toString(), "autorun").getBoolean())
			{
				final int Set = config.getNode("mineName", text.toString(), "set").getInt();
				final int Delay = config.getNode("mineName", text.toString(), "startupdelay").getInt();
				Mine_startupdelay.put(text.toString(), Delay);
				Mine_actualdelay.put(text.toString(), Delay);
				Mine_set.put(text.toString(), Set);
			}
		}
	}

	public static void run()
	{
		startfillsets = true;
		for (final String Name: Mine_set.keySet())
		{
			if (Mine_set.get(Name).intValue() == 0)
			{
				if (Mine_actualdelay.get(Name).equals(0))
				{
					final int time = config.getNode("mineName", Name, "renewtime").getInt();
					final String format = config.getNode("mineName", Name, "renewformat").getString();
					final String world =  config.getNode("mineName", Name, "world").getString();
					Timers.add(Name, time, format, UUID.fromString(world));
					Mine_actualdelay.remove(Name);
					Mine_startupdelay.remove(Name);
					Mine_set.remove(Name);
					for (final String Names: Mine_actualdelay.keySet())
					{
						Mine_actualdelay.put(Names, Mine_startupdelay.get(Names));
					}
				}
				else
				{
					Mine_actualdelay.put(Name, Mine_actualdelay.get(Name) - 1);
				}
			}
		}

		for (final String Name: Mine_set.keySet())
		{
			if (Mine_set.get(Name).intValue() == 0)
			{
				startfillsets = false;
			}
		}
		if (startfillsets)
		{
			//let us know how many set are defined
			final List<Integer> Set_number = new ArrayList<Integer>();
			for (final int Sets: Mine_set.values())
			{
				if (Set_number.isEmpty())
				{
					Set_number.add(Sets);
				}
				if (!Set_number.contains(Sets))
				{
					Set_number.add(Sets);
				}
			}
			//if there is at last one
			if (Set_number.size() != 0)
			{
				//get the first
				final int Set = Set_number.get(0);
				for (final String Name : Mine_set.keySet())
				{
					if (Mine_set.get(Name).equals(Set))
					{
						//if the delay is zero fill the mine in the current set
						//if all mine in the current set get the same delay they will refresh in the same time
						//please put same delay for them
						if (Mine_actualdelay.get(Name).equals(0))
						{
							final int time = config.getNode("mineName", Name, "renewtime").getInt();
							final String format = config.getNode("mineName", Name, "renewformat").getString();
							final String world =  config.getNode("mineName", Name, "world").getString();
							Timers.add(Name, time, format, UUID.fromString(world));
							Mine_actualdelay.remove(Name);
							Mine_startupdelay.remove(Name);
							Mine_set.remove(Name);
						}
						else
						{
							Mine_actualdelay.put(Name, Mine_actualdelay.get(Name) - 1);
						}
					}
				}
			}
		}
		if (Mine_set.isEmpty())
		{
			Mine_startupdelay.clear();
			Mine_actualdelay.clear();
			prispongemine.plugin.task_autorun.cancel();
		}
	}
}
