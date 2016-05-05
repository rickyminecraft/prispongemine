package com.ricky30.prispongemine.utility;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.prispongemine;
import ninja.leaping.configurate.ConfigurationNode;

public class altar
{
	private static ConfigurationNode config = null;
	private static boolean saved = false;

	public static void SaveAltar(Vector3i position)
	{
		config = prispongemine.plugin.getConfig();
		config.getNode("altar", "altar_X").setValue(position.getX());
		config.getNode("altar", "altar_Y").setValue(position.getY()+1);
		config.getNode("altar", "altar_Z").setValue(position.getZ());
		prispongemine.plugin.save();
		saved = true;
	}

	public static void SetSaved()
	{
		saved = false;
	}

	public static boolean IsSaved()
	{
		return saved;
	}
}
