package com.ricky30.prispongemine.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.prispongemine;
import com.ricky30.prispongemine.utility.size;

import ninja.leaping.configurate.ConfigurationNode;

public class timers
{
	static Map<String, Integer> timer = new ConcurrentHashMap<String, Integer>(256);
	static Map<String, Integer> wasted = new HashMap<String, Integer>(256);
	static Map<String, String> theworld = new HashMap<String, String>(256);

	private static ConfigurationNode config = null;

	public static void run() 
	{
		if (!timer.isEmpty())
		{
			for (final Entry<String, Integer> Mine_name: timer.entrySet()) 
			{
				int duration = wasted.get(Mine_name.getKey());
				final int currentduration = Mine_name.getValue();
				duration ++;
				if (duration < currentduration)
				{
					wasted.put(Mine_name.getKey(), duration);
					if (currentduration - duration == 300)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("mineName", Mine_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " five minute left"));
							}
						}
					}
					if (currentduration - duration == 60)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("mineName", Mine_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " one minute left"));
							}
						}
					}
					if (currentduration - duration == 30)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("mineName", Mine_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 30 seconds left"));
							}
						}
					}
					if (currentduration - duration == 10)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("mineName", Mine_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 10 seconds left"));
							}
						}
					}
					if (currentduration - duration == 5)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("mineName", Mine_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 5 seconds left"));
							}
						}
					}
					if (currentduration - duration == 4)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("mineName", Mine_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 4 seconds left"));
							}
						}
					}
					if (currentduration - duration == 3)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("mineName", Mine_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 3 seconds left"));
							}
						}
					}
					if (currentduration - duration == 2)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("mineName", Mine_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 2 seconds left"));
							}
						}
					}
					if (currentduration - duration == 1)
					{
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("mineName", Mine_name.getKey(), "world").getString()))
							{
								player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " 1 second left"));
							}
						}
					}

				}
				else
				{
					//here launch event
					boolean HasSpawn = false;
					if (config.getNode("mineName", Mine_name.getKey()).getChildrenMap().get("Spawn") != null)
					{
						HasSpawn = true;
					}
					if (HasSpawn)
					{
						int X1, X2, Y1, Y2, Z1, Z2;
						double X3 = 0, Y3 = 0, Z3 = 0;
						X1 = config.getNode("mineName", Mine_name.getKey(), "depart_X").getInt();
						Y1 = config.getNode("mineName", Mine_name.getKey(), "depart_Y").getInt();
						Z1 = config.getNode("mineName", Mine_name.getKey(), "depart_Z").getInt();
						X2 = config.getNode("mineName", Mine_name.getKey(), "fin_X").getInt();
						Y2 = config.getNode("mineName", Mine_name.getKey(), "fin_Y").getInt();
						Z2 = config.getNode("mineName", Mine_name.getKey(), "fin_Z").getInt();
						X3 = config.getNode("mineName", Mine_name.getKey(), "Spawn", "Spawn_X").getDouble();
						Y3 = config.getNode("mineName", Mine_name.getKey(), "Spawn", "Spawn_Y").getDouble();
						Z3 = config.getNode("mineName", Mine_name.getKey(), "Spawn", "Spawn_Z").getDouble();

						//converted to vector
						final Vector3i first = new Vector3i(X1, Y1, Z1);
						final Vector3i second = new Vector3i(X2, Y2, Z2);
						final Vector3d spawn = new Vector3d(X3, Y3, Z3);
						for (final Player player: Sponge.getServer().getOnlinePlayers())
						{
							if (player.getWorld().getName().equals(config.getNode("mineName", Mine_name.getKey(), "world").getString()))
							{
								final Location<World> location = player.getLocation();
								if (IsInside(location.getBlockPosition(), first, second))
								{
									final Location<World> SpawnLoc = new Location<World>(player.getWorld(), spawn);
									player.setLocation(SpawnLoc);
								}
							}
						}
					}
					Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "prisponge" +" fill " + Mine_name.getKey());
					for (final Player player: Sponge.getServer().getOnlinePlayers())
					{
						if (player.getWorld().getName().equals(config.getNode("mineName", Mine_name.getKey(), "world").getString()))
						{
							player.sendMessage(Text.of("Mine " , Mine_name.getKey(), " refill done"));
						}
					}
					wasted.put(Mine_name.getKey(), -1);
				}
			}
		}
	}

	public static void add(String name, int duration, String format, String world)
	{
		config = prispongemine.plugin.getConfig();
		if (timer.get(name) == null)
		{
			if (format.equals("SECONDS") || format.equals("MINUTES") || format.equals("HOURS") || format.equals("DAYS"))
			{
				int durationsecond = 0;
				if (format.equals("SECONDS"))
				{
					durationsecond = duration;
				}
				else if (format.equals("MINUTES"))
				{
					durationsecond = (60 * duration);
				}
				else if (format.equals("HOURS"))
				{
					durationsecond = (3600 * duration);
				}
				else if (format.equals("DAYS"))
				{
					durationsecond = (86400 * duration);
				}

				timer.put(name, durationsecond);
				theworld.put(name, world);
				wasted.put(name, -1);
			}
		}
	}

	public static void remove(String name)
	{
		if (timer.get(name) != null)
		{
			timer.remove(name);
			wasted.remove(name);
			theworld.remove(name);
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
