package com.ricky30.prispongemine.utility;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.ricky30.prispongemine.config.ManageConfig;

import ninja.leaping.configurate.ConfigurationNode;

/**
 * Created by Jamie on 18-May-16.
 */
public class mineMessages
{
	private static ConfigurationNode config = null;

	public static void buildMessages(String mineName, int time) 
	{
		config = ManageConfig.getConfig();
		String currentMessage = config.getNode("messageDump").getString();
		String spacing = ", ";
		if (currentMessage.equalsIgnoreCase("NoMessages")) 
		{
			currentMessage = "";
			spacing = "";
		}

		String additionalInfo = mineName + " will reset in " + timeToString.secondsToString(time);
		if (time == 0) 
		{
			additionalInfo = mineName + " is resetting now";
		}
		config.getNode("messageDump").setValue(currentMessage + spacing + additionalInfo);
	}

	public static void sendMessages() 
	{
		config = ManageConfig.getConfig();
		if (config.getNode("showwarning").isVirtual())
		{
			config.getNode("showwarning").setValue(true);
			ManageConfig.Save();
		}
		if (config.getNode("showwarning").getBoolean())
		{
			if (config.getNode("messageDump").getString() == null) 
			{
				config.getNode("messageDump").setValue("NoMessages");
				ManageConfig.Save();
			}
			else 
			{
				final String finalMessage = config.getNode("messageDump").getString();
				if (!finalMessage.equalsIgnoreCase("NoMessages")) 
				{
					for (final Player player : Sponge.getServer().getOnlinePlayers()) 
					{
						player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(("&9&l[Mines]&r &e" + finalMessage + ".")));
					}
					config.getNode("messageDump").setValue("NoMessages");
					ManageConfig.Save();
				}
			}
		}
		else
		{
			config.getNode("messageDump").setValue("NoMessages");
		}
	}
}
