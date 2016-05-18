package com.ricky30.prispongemine.utility;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;

/**
 * Created by Jamie on 18-May-16.
 */
public class MineMessages
{
	private static ConfigurationNode config = null;

	public static void buildMessages(String mineName, int time) 
	{
		config = prispongemine.plugin.getConfig();

		String currentMessage = config.getNode("messageDump").getString();
		String spacing = ", ";
		if (currentMessage.equals("NoMessages")) 
		{
			currentMessage = "";
			spacing = "";
		}

		String additionalInfo = mineName + " will reset in " + TimeToString.secondsToString(time);
		if (time == 0) 
		{
			additionalInfo = mineName + " is resetting now";
		}
		config.getNode("messageDump").setValue(currentMessage + spacing + additionalInfo);
	}

	public static void sendMessages() 
	{
		config = prispongemine.plugin.getConfig();
		String finalMessage = "";
		if (!config.getNode("messageDump").isVirtual())
		{
			finalMessage = config.getNode("messageDump").getString();
		}
		if (!finalMessage.equals("NoMessages")) 
		{
			for (final Player player: Sponge.getServer().getOnlinePlayers()) 
			{
				player.sendMessage(Text.of("[Mines] " + finalMessage));
			}
			config.getNode("messageDump").setValue("NoMessages");
		}
	}
}
