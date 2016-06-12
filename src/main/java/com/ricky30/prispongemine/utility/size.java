package com.ricky30.prispongemine.utility;

import com.flowpowered.math.vector.Vector3i;

public class size
{
	public static Vector3i Min(Vector3i first, Vector3i second)
	{
		int x, y, z;
		if (first.getX() < second.getX())
		{
			x = first.getX();
		}
		else
		{
			x = second.getX();
		}
		if (first.getY() < second.getY())
		{
			y = first.getY();
		}
		else
		{
			y = second.getY();
		}
		if (first.getZ() < second.getZ())
		{
			z = first.getZ();
		}
		else
		{
			z = second.getZ();
		}
		final Vector3i vector3i = new Vector3i(x, y, z);
		return vector3i;
	}

	public static Vector3i Max(Vector3i first, Vector3i second)
	{
		int x, y, z;
		if (first.getX() < second.getX())
		{
			x = second.getX();
		}
		else
		{
			x = first.getX();
		}
		if (first.getY() < second.getY())
		{
			y = second.getY();
		}
		else
		{
			y = first.getY();
		}
		if (first.getZ() < second.getZ())
		{
			z = second.getZ();
		}
		else
		{
			z = first.getZ();
		}
		final Vector3i vector3i = new Vector3i(x, y, z);
		return vector3i;
	}
}
