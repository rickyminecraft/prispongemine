package com.ricky30.prispongemine.utility;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.config.ManageMines;

import ninja.leaping.configurate.ConfigurationNode;

public class teleport
{

	public static void Doteleport(String Name)
	{
		ManageMines.LoadMine(Name);
		final ConfigurationNode config = ManageMines.getConfig();
		boolean HasSpawn = false;
		if (!config.getNode("Spawn").isVirtual())
		{
			HasSpawn = true;
		}
		if (HasSpawn)
		{
			int X1, X2, Y1, Y2, Z1, Z2;
			double X3 = 0.0, Y3 = 0.0, Z3 = 0.0, X4 = 0.0, Y4 = 0.0, Z4 = 0.0;
			X1 = config.getNode("depart_X").getInt();
			Y1 = config.getNode("depart_Y").getInt();
			Z1 = config.getNode("depart_Z").getInt();
			X2 = config.getNode("fin_X").getInt();
			Y2 = config.getNode("fin_Y").getInt();
			Z2 = config.getNode("fin_Z").getInt();
			X3 = config.getNode("Spawn", "Spawn_X").getDouble();
			Y3 = config.getNode("Spawn", "Spawn_Y").getDouble();
			Z3 = config.getNode("Spawn", "Spawn_Z").getDouble();
			X4 = config.getNode("Spawn", "Spawn_Pitch").getDouble();
			Y4 = config.getNode("Spawn", "Spawn_Yaw").getDouble();
			Z4 = config.getNode("Spawn", "Spawn_Roll").getDouble();
			final String world = config.getNode("Spawn", "world").getString();
			final World Spawn_World = Sponge.getServer().getWorld(UUID.fromString(world)).get();

			//converted to vector
			final Vector3i first = new Vector3i(X1, Y1, Z1);
			final Vector3i second = new Vector3i(X2, Y2, Z2);
			final Vector3d spawn = new Vector3d(X3, Y3, Z3);
			final Vector3d spawnRotation = new Vector3d(X4, Y4, Z4);
			for (final Player player: Sponge.getServer().getOnlinePlayers())
			{
				if (player.getWorld().getUniqueId().equals(UUID.fromString(config.getNode("world").getString())))
				{
					final Location<World> location = player.getLocation();
					if (IsInside(location.getBlockPosition(), first, second))
					{
						player.transferToWorld(Spawn_World.getName(), spawn);
						player.setRotation(spawnRotation);
					}
				}
			}
		}
	}

	private static boolean IsInside(Vector3i location, Vector3i one, Vector3i two)
	{
		final Vector3i Min = size.Min(one, two);
		final Vector3i Max = size.Max(one, two);
		Boolean InsideX = false;
		Boolean InsideY = false;
		Boolean InsideZ = false;
		if ((location.getX() >= Min.getX()) && (location.getX() <= Max.getX()))
		{
			InsideX = true;
		}
		if ((location.getY() >= Min.getY()) && (location.getY() <= Max.getY()))
		{
			InsideY = true;
		}
		if ((location.getZ() >= Min.getZ()) && (location.getZ() <= Max.getZ()))
		{
			InsideZ = true;
		}
		if (InsideX && InsideY && InsideZ)
		{
			return true;
		}
		return false;
	}
}
