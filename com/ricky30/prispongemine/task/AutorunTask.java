package com.ricky30.prispongemine.task;

import java.util.UUID;

import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;

public class AutorunTask
{
	private static ConfigurationNode config = null;
	
	public static void run()
	{
		config = prispongemine.plugin.getConfig();
		for (final Object text: config.getNode("mineName").getChildrenMap().keySet())
		{
			long Tempo = java.lang.System.currentTimeMillis();
			if (config.getNode("mineName", text.toString(), "autorun").getBoolean())
			{
				long tempo2 = Tempo;
				while (tempo2 - Tempo < 60000)
				{
					tempo2 = java.lang.System.currentTimeMillis();
				}
				final int time = config.getNode("mineName", text.toString(), "renewtime").getInt();
				final String format = config.getNode("mineName", text.toString(), "renewformat").getString();
				final String world =  config.getNode("mineName", text.toString(), "world").getString();
				Timers.add(text.toString(), time, format, UUID.fromString(world));
			}
		}
		prispongemine.plugin.task_autorun.cancel();
	}
}
