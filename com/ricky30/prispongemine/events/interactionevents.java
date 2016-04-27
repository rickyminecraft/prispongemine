package com.ricky30.prispongemine.events;

import java.util.HashMap;
import java.util.Map;

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
	public static Map<String, Boolean> isActive = new HashMap<String, Boolean>();
	private static Map<String, Boolean> Readytofill = new HashMap<String, Boolean>();
	private static Map<String, Boolean> primaryUsed = new HashMap<String, Boolean>();
	private static Map<String, Boolean> secondaryUsed = new HashMap<String, Boolean>();
	private static Map<String, Vector3i> first = new HashMap<String, Vector3i>();
	private static Map<String, Vector3i> second = new HashMap<String, Vector3i>();
	
	@Listener
	public void oninteractblockPrimary(ChangeBlockEvent.Break Event, @First Player player)
	{
		if (isActive.get(player.getUniqueId()).booleanValue())
		{
			if(secondaryUsed.get(player.getUniqueId()).booleanValue())
			{
				isActive.put(player.getUniqueId().toString(), false);
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
		if (isActive.get(player.getUniqueId()).booleanValue())
		{
			if (player.getItemInHand().isPresent())
			{
				if (player.getItemInHand().get().getItem().getId().equals(prispongemine.plugin.GetTool()))
				{
					if (!primaryUsed.get(player.getUniqueId()).booleanValue())
					{
						first.put(player.getUniqueId().toString(), Event.getTargetBlock().getPosition());
						primaryUsed.put(player.getUniqueId().toString(), true);
						player.getCommandSource().get().sendMessage(Text.of("First point defined"));
						
					}
					else if (!secondaryUsed.get(player.getUniqueId()).booleanValue())
					{
						second.put(player.getUniqueId().toString(), Event.getTargetBlock().getPosition());
						secondaryUsed.put(player.getUniqueId().toString(), true);
						if (primaryUsed.get(player.getUniqueId()).booleanValue() && secondaryUsed.get(player.getUniqueId()).booleanValue())
						{
							Readytofill.put(player.getUniqueId().toString(), true);
						}
						player.getCommandSource().get().sendMessage(Text.of("Second point defined"));
						player.getCommandSource().get().sendMessage(Text.of("Ready to save / update"));
					}
				}
			}
		}
	}
	
	public static boolean IsreadytoFill(Player player)
	{
		return Readytofill.get(player.getUniqueId().toString());
	}
	
	public static Vector3i getFirst(Player player)
	{
		return first.get(player.getUniqueId().toString());
	}
	
	public static Vector3i getSecond(Player player)
	{
		return second.get(player.getUniqueId().toString());
	}
	
	public static void Reset(Player player)
	{
		isActive.remove(player.getUniqueId().toString());
		Readytofill.remove(player.getUniqueId().toString());
		primaryUsed.remove(player.getUniqueId().toString());
		secondaryUsed.remove(player.getUniqueId().toString());
		first.remove(player.getUniqueId().toString());
		second.remove(player.getUniqueId().toString());
		
		isActive.put(player.getUniqueId().toString(), true);
		Readytofill.put(player.getUniqueId().toString(), false);
		primaryUsed.put(player.getUniqueId().toString(), false);
		secondaryUsed.put(player.getUniqueId().toString(), false);
	}
}
