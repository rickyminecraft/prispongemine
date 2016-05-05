package com.ricky30.prispongemine.utility;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.prispongemine;
import com.ricky30.prispongemine.events.interactionevents;

import ninja.leaping.configurate.ConfigurationNode;

public class altar
{
	private static ConfigurationNode config = null;

	public static void SaveAltar(Vector3i position)
	{
		config = prispongemine.plugin.getConfig();
		config.getNode("altar", "altar_X").setValue(position.getX());
		config.getNode("altar", "altar_Y").setValue(position.getY()+1);
		config.getNode("altar", "altar_Z").setValue(position.getZ());
		prispongemine.plugin.save();
		interactionevents.ResetAltar();
	}
}
