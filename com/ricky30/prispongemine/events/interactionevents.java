package com.ricky30.prispongemine.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.prispongemine;

public class interactionevents
{
	public static boolean isActive = false;
	private static boolean Readytofill = false;
	private static boolean primaryUsed = false;
	private static boolean secondaryUsed = false;
	private static Vector3i first;
	private static Vector3i second;
	
	@Listener
	public void oninteractblockPrimary(ChangeBlockEvent.Break Event, @First Player player)
	{
		if (isActive)
		{
			if(secondaryUsed)
			{
				isActive = false;
			}
			if (player.getItemInHand().isPresent())
			{
				if (player.getItemInHand().get().getItem().getId().equals(prispongemine.plugin.GetTool()))
				{
					Event.setCancelled(true);
				}
			}
		}
	}
	
	@Listener
	public void oninteractblockPrimary(InteractBlockEvent.Primary Event, @First Player player)
	{
		if (isActive)
		{
			if (player.getItemInHand().isPresent())
			{
				if (player.getItemInHand().get().getItem().getId().equals(prispongemine.plugin.GetTool()))
				{
					if (!primaryUsed)
					{
						first = Event.getTargetBlock().getPosition();
						primaryUsed = true;
						player.getCommandSource().get().sendMessage(Text.of("First point defined"));
						
					}
					else if (!secondaryUsed)
					{
						second = Event.getTargetBlock().getPosition();
						secondaryUsed = true;
						if (primaryUsed && secondaryUsed)
						{
							Readytofill = true;
						}
						player.getCommandSource().get().sendMessage(Text.of("Second point defined"));
						player.getCommandSource().get().sendMessage(Text.of("Ready to save / update"));
					}
				}
			}
		}
	}
	
	public static boolean IsreadytoFill()
	{
		return Readytofill;
	}
	
	public static Vector3i getFirst()
	{
		return first;
	}
	
	public static Vector3i getSecond()
	{
		return second;
	}
	
	public static void Reset()
	{
		isActive = true;
		Readytofill = false;
		primaryUsed = false;
		secondaryUsed = false;
	}
}
