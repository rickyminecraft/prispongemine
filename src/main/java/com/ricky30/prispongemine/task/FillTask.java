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

	public static void Fill( MutableBlockVolume origin, MutableBlockVolume destination, Vector3i start, Vector3i end, String Name) {
		one.put(Name, origin);
		two.put(Name, destination);
		Start.put(Name, start);
		End.put(Name, end);
		CanStart.put(Name, true);
	}

	public static void run() {
		if (!one.isEmpty()) {
			for (final String Names: one.keySet()) {
				if (CanStart.get(Names)) {
					CanStart.put(Names, false);
					final MutableBlockVolume volumeOne = one.get(Names);
					final MutableBlockVolume volumeTwo = two.get(Names);

					int StartX, StartY , StartZ, EndX, EndY, EndZ;
					StartX = Start.get(Names).getX();
					StartY = Start.get(Names).getY();
					StartZ = Start.get(Names).getZ();
					EndX = End.get(Names).getX();
					EndY = End.get(Names).getY();
					EndZ = End.get(Names).getZ();
					for (int x = 0;x< EndX;x++) {
						for (int y = 0; y< EndY; y++) {
							for (int z = 0; z< EndZ;z++) {
								volumeTwo.setBlock(StartX+x, StartY+y, StartZ+z, volumeOne.getBlock(x, y, z));
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
		} else {
			prispongemine.plugin.StopTaskFill();
		}
	}
}
