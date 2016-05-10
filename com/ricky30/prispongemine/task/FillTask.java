package com.ricky30.prispongemine.task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.world.extent.MutableBlockVolume;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.prispongemine;

public class FillTask
{
	private static Map<String, MutableBlockVolume> one = new ConcurrentHashMap<String, MutableBlockVolume>();
	private static Map<String, MutableBlockVolume> two = new ConcurrentHashMap<String, MutableBlockVolume>();
	private static Map<String, Vector3i> Start = new ConcurrentHashMap<String, Vector3i>();
	private static Map<String, Vector3i> End = new ConcurrentHashMap<String, Vector3i>();
	private static Map<String, Boolean> CanStart = new ConcurrentHashMap<String, Boolean>();

	public static void Fill( MutableBlockVolume origin, MutableBlockVolume destination, Vector3i start, Vector3i end, String Name)
	{
		one.put(Name, origin);
		two.put(Name, destination);
		Start.put(Name, start);
		End.put(Name, end);
		CanStart.put(Name, true);
	}

	public static void run()
	{
		if (!one.isEmpty())
		{
			for (final String Names: one.keySet())
			{
				if (CanStart.get(Names))
				{
					CanStart.put(Names, false);
					final MutableBlockVolume volumeOne = one.get(Names);
					final MutableBlockVolume volumeTwo = two.get(Names);

					int WorldX, WorldY , WorldZ, MaxX, MaxY, MaxZ;
					WorldX = Start.get(Names).getX();
					WorldY = Start.get(Names).getY();
					WorldZ = Start.get(Names).getZ();
					MaxX = End.get(Names).getX();
					MaxY = End.get(Names).getY();
					MaxZ = End.get(Names).getZ();
					for (int x = 0;x< MaxX;x++)
					{
						for (int y = 0; y< MaxY; y++)
						{
							for (int z = 0; z< MaxZ;z++)
							{
								volumeTwo.setBlock(WorldX+x, WorldY+y, WorldZ+z, volumeOne.getBlock(x, y, z));
							}
						}
					}
					one.remove(Names);
					two.remove(Names);
					Start.remove(Names);
					End.remove(Names);
					CanStart.put(Names, true);
				}
			}
		}
		else
		{
			prispongemine.plugin.StopTaskFill();
		}
	}
}
