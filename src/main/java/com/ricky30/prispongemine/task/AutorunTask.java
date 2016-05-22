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
			if (config.getNode("mineName", text.toString(), "autorun").getBoolean())
			{
				final int time = config.getNode("mineName", text.toString(), "renewtime").getInt();
				final String format = config.getNode("mineName", text.toString(), "renewformat").getString();
				final String world = config.getNode("mineName", text.toString(), "world").getString();

				if(config.getNode("mineName", text.toString(), "initialdelay").getValue() == null) {
					config.getNode("mineName", text.toString(), "initialdelay").setValue(0);
					prispongemine.plugin.save();
				}

				final int initialDelay = config.getNode("mineName", text.toString(), "initialdelay").getInt();

				Timers.add(text.toString(), time, initialDelay, format, UUID.fromString(world));
			}
		}
		prispongemine.plugin.task_autorun.cancel();
	}
}
