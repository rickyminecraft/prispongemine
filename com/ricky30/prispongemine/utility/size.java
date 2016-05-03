package com.ricky30.prispongemine.utility;

import com.flowpowered.math.vector.Vector3i;

public class size
{
	public static Vector3i Min(Vector3i first, Vector3i second)
	{
		int x1, y1, z1;
		if (first.getX() < second.getX())
		{
			x1 = first.getX();
		}
		else
		{
			x1 = second.getX();

		}
		if (first.getY() < second.getY())
		{
			y1 = first.getY();
		}
		else
		{
			y1 = second.getY();
		}
		if (first.getZ() < second.getZ())
		{
			z1 = first.getZ();
		}
		else
		{
			z1 = second.getZ();
		}
		final Vector3i vector3i = new Vector3i(x1, y1, z1);
		return vector3i;
	}

	public static Vector3i Max(Vector3i first, Vector3i second)
	{
		int x1, y1, z1;
		if (first.getX() < second.getX())
		{
			x1 = second.getX();
		}
		else
		{
			x1 = first.getX();
		}
		if (first.getY() < second.getY())
		{
			y1 = second.getY();
		}
		else
		{
			y1 = first.getY();
		}
		if (first.getZ() < second.getZ())
		{
			z1 = second.getZ();
		}
		else
		{
			z1 = first.getZ();
		}
		final Vector3i vector3i = new Vector3i(x1, y1, z1);
		return vector3i;
	}
}
