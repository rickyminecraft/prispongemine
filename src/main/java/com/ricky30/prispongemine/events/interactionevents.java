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
import com.ricky30.prispongemine.utility.altar;

public class interactionevents
{
	private static Map<String, Boolean> Readytofill = new HashMap<String, Boolean>();
	private static Map<String, Boolean> primaryUsed = new HashMap<String, Boolean>();
	private static Map<String, Boolean> secondaryUsed = new HashMap<String, Boolean>();
	private static Map<String, Vector3i> first = new HashMap<String, Vector3i>();
	private static Map<String, Vector3i> second = new HashMap<String, Vector3i>();
	private static boolean setAltar = false;
	private static Vector3i Altar;

	@Listener
	public void oninteractblockBreak(ChangeBlockEvent.Break Event, @First Player player)
	{
		if (altar.IsSaved() && setAltar)
		{
			setAltar = false;
			Event.setCancelled(true);
		}
	}

	@Listener
	public void oninteractblockPrimary(InteractBlockEvent.Secondary Event, @First Player player)
	{
		if (setAltar)
		{
			Altar = Event.getTargetBlock().getPosition();
			altar.SaveAltar(Altar, player.getWorld().getUniqueId());
			player.getCommandSource().get().sendMessage(Text.of("Altar defined"));
		}
		else if (player.getItemInHand().isPresent())
		{
			if (player.getItemInHand().get().getItem().getId().equals(prispongemine.plugin.GetTool()))
			{
				if (!primaryUsed.get(player.getUniqueId().toString()).booleanValue())
				{
					first.put(player.getUniqueId().toString(), Event.getTargetBlock().getPosition());
					primaryUsed.put(player.getUniqueId().toString(), true);
					player.getCommandSource().get().sendMessage(Text.of("First point defined"));

				}
				else if (!secondaryUsed.get(player.getUniqueId().toString()).booleanValue())
				{
					second.put(player.getUniqueId().toString(), Event.getTargetBlock().getPosition());
					secondaryUsed.put(player.getUniqueId().toString(), true);
					Readytofill.put(player.getUniqueId().toString(), true);
					player.getCommandSource().get().sendMessage(Text.of("Second point defined"));
					player.getCommandSource().get().sendMessage(Text.of("Ready to save / update"));
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
		Readytofill.remove(player.getUniqueId().toString());
		primaryUsed.remove(player.getUniqueId().toString());
		secondaryUsed.remove(player.getUniqueId().toString());
		first.remove(player.getUniqueId().toString());
		second.remove(player.getUniqueId().toString());

		Readytofill.put(player.getUniqueId().toString(), false);
		primaryUsed.put(player.getUniqueId().toString(), false);
		secondaryUsed.put(player.getUniqueId().toString(), false);
	}

	public static void SetAltar()
	{
		setAltar = true;
	}

	public static void ResetAltar()
	{
		setAltar = false;
	}
}
