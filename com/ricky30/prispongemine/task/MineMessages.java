package com.ricky30.prispongemine.task;

import com.ricky30.prispongemine.TimeToString;
import com.ricky30.prispongemine.prispongemine;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

/**
 * Created by Jamie on 18-May-16.
 */
public class MineMessages {

	private static ConfigurationNode config;
	//private ConfigurationNode config = null;

	public static void buildMessages(String mineName, int time) {

		config = prispongemine.plugin.getConfig();

		String currentMessage = config.getNode("messageDump").getString();
		String spacing = ", ";

		if (currentMessage.equalsIgnoreCase("NoMessages")) {
			currentMessage = "";
			spacing = "";
		}

		String additionalInfo = mineName + " will reset in " + TimeToString.secondsToString(time);
		if (time == 0) {
			additionalInfo = mineName + " is resetting now";
		}

		config.getNode("messageDump").setValue(currentMessage + spacing + additionalInfo);
	}

	public static void sendMessages() {

		config = prispongemine.plugin.getConfig();

		String finalMessage = config.getNode("messageDump").getString();
		if (!finalMessage.equalsIgnoreCase("NoMessages")) {

			for (final Player player: Sponge.getServer().getOnlinePlayers()) {
				player.sendMessage(Text.of("[Mines] " + finalMessage));
			}

			config.getNode("messageDump").setValue("NoMessages");
		}
	}
}
