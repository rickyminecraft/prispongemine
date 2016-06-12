package com.ricky30.prispongemine.utility;

import java.util.UUID;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.config.ManageConfig;

import ninja.leaping.configurate.ConfigurationNode;

public class altar
{
	private static boolean saved = false;

	public static void SaveAltar(Vector3i position, UUID uuid)
	{
		final ConfigurationNode config = ManageConfig.getConfig();
		config.getNode("altar", "altar_X").setValue(position.getX());
		config.getNode("altar", "altar_Y").setValue(position.getY()+1);
		config.getNode("altar", "altar_Z").setValue(position.getZ());
		config.getNode("altar", "world").setValue(uuid.toString());
		ManageConfig.Save();
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
