package com.ricky30.prispongemine.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.extent.MutableBlockVolume;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.prispongemine;

public class ClearTask
{
	private static Map<String, MutableBlockVolume> Volume = new HashMap<String, MutableBlockVolume>();
	private static Map<String, Vector3i> Start = new HashMap<String, Vector3i>();
	private static Map<String, Vector3i> End = new HashMap<String, Vector3i>();
	private static Map<String, Boolean> CanStart = new ConcurrentHashMap<String, Boolean>();

	public static void Fill( MutableBlockVolume destination, Vector3i start, Vector3i end, String Name)
	{
		Volume.put(Name, destination);
		Start.put(Name, start);
		End.put(Name, end);
		CanStart.put(Name, true);
	}

	public static void run()
	{
		if (!Volume.isEmpty())
		{
			for (final String Names: Volume.keySet())
			{
				if (CanStart.get(Names))
				{
					CanStart.put(Names, false);
					final MutableBlockVolume volumeOne = Volume.get(Names);

					int StartX, StartY , StartZ, EndX, EndY, EndZ;
					StartX = Start.get(Names).getX();
					StartY = Start.get(Names).getY();
					StartZ = Start.get(Names).getZ();
					EndX = End.get(Names).getX();
					EndY = End.get(Names).getY();
					EndZ = End.get(Names).getZ();
					for (int x = StartX; x<= EndX; x++)
					{
						for (int y = StartY; y<= EndY; y++)
						{
							for (int z = StartZ; z<= EndZ; z++)
							{
								volumeOne.setBlock(x, y, z, BlockTypes.AIR.getDefaultState());
							}
						}
					}
					Volume.remove(Names);
					Start.remove(Names);
					End.remove(Names);
					CanStart.put(Names, true);
				}
			}
		}
		else
		{
			Start.clear();
			End.clear();
			CanStart.clear();
			prispongemine.plugin.StopTaskClear();
		}
	}
}
