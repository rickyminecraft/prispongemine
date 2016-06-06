package com.ricky30.prispongemine.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.api.GameDictionary.Entry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import com.ricky30.prispongemine.config.ManageConfig;

import ninja.leaping.configurate.ConfigurationNode;

public class commandUpdateconfig implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		this.config = ManageConfig.getConfig();
		final Player player = (Player) src;
		this.config.getNode("ConfigVersion").setValue(5);
		this.config.getNode("RemindSecondList").setValue("1, 2, 3, 4, 5, 10, 15, 30, 60, 90, 120, 180, 300");
		this.config.getNode("messageDump").setValue("NoMessages");
		//updateMine(player);
		updateAltar(player);
		//updateOre();
		ManageConfig.Save();
		src.sendMessage(Text.of("config file updated"));
		return CommandResult.success();
	}

	private void updateOre()
	{
		final Map<String, String> ore_name = new HashMap<String, String>();
		final Map<String, Integer> ore_percent = new HashMap<String, Integer>();
		for (final Object text: this.config.getNode("mineName").getChildrenMap().keySet())
		{
			if (!this.config.getNode("mineName", text.toString(), "items").isVirtual())
			{
				boolean renew_ore = false;
				for (final Object items: this.config.getNode("mineName", text.toString(), "items").getChildrenMap().keySet())
				{
					if (this.config.getNode("mineName", text.toString(), "items", items).getChildrenMap().get("BlockState") == null)
					{
						String ore = this.config.getNode("mineName", text.toString(), "items", items).getString();
						ore = ore.substring(1, ore.indexOf("="));
						final int percentage = this.config.getNode("mineName", text.toString(), "items", items, ore).getInt();
						ore_name.put(ore.toString(), ore);
						ore_percent.put(ore.toString(), percentage);
						renew_ore = true;
					}
				}
				if (renew_ore)
				{
					this.config.getNode("mineName", text.toString()).removeChild("items");
					for (final String Oname: ore_name.keySet())
					{
						final Entry entry = Sponge.getDictionary().get(Oname).iterator().next();
						final BlockState state = entry.getType().getBlock().get().getDefaultState();
						final Float Percentage = ore_percent.get(Oname).floatValue();
						boolean Isnothere = false;
						int position = -1;
						while (!Isnothere)
						{
							position++;
							if (this.config.getNode("mineName", text.toString(), "items", "item_".concat(String.valueOf(position))).getString() == null)
							{
								Isnothere = true;
							}
						}
						this.config.getNode("mineName", text.toString(), "items", "item_".concat(String.valueOf(position)), "BlockState").setValue(state.toString());
						this.config.getNode("mineName", text.toString(), "items", "item_".concat(String.valueOf(position)), "percentage").setValue(Percentage);
					}
				}
			}
		}
	}

	private void updateMine(Player player)
	{
		for (final Object text: this.config.getNode("mineName").getChildrenMap().keySet())
		{
			if (this.config.getNode("mineName", text.toString()).getChildrenMap().get("autorun") == null)
			{
				this.config.getNode("mineName", text.toString(), "autorun").setValue(false);
			}
			if (this.config.getNode("mineName", text.toString()).getChildrenMap().get("random") == null)
			{
				this.config.getNode("mineName", text.toString(), "random").setValue(false);
			}
			final String testUUID = this.config.getNode("mineName", text.toString(), "world").getString();
			if (testUUID.length() != 36)
			{
				final World world = Sponge.getServer().getWorld(testUUID).get();
				final UUID UUID = world.getUniqueId();
				this.config.getNode("mineName", text.toString(), "world").setValue(UUID.toString());
			}
			if (this.config.getNode("mineName", text.toString()).getChildrenMap().get("Spawn") != null)
			{
				if (this.config.getNode("mineName", text.toString(), "Spawn").getChildrenMap().get("world") == null)
				{
					this.config.getNode("mineName", text.toString(), "Spawn", "world").setValue(player.getWorld().getUniqueId().toString());
				}
				if (this.config.getNode("mineName", text.toString(), "Spawn").getChildrenMap().get("Spawn_Pitch") == null)
				{
					this.config.getNode("mineName", text.toString(), "Spawn", "Spawn_Pitch").setValue(player.getRotation().getX());
					this.config.getNode("mineName", text.toString(), "Spawn", "Spawn_Yaw").setValue(player.getRotation().getY());
					this.config.getNode("mineName", text.toString(), "Spawn", "Spawn_Roll").setValue(player.getRotation().getZ());
				}
				if (this.config.getNode("mineName", text.toString()).getChildrenMap().get("set") == null)
				{
					this.config.getNode("mineName", text.toString(), "set").setValue(0);
				}
				if (this.config.getNode("mineName", text.toString()).getChildrenMap().get("startupdelay") == null)
				{
					this.config.getNode("mineName", text.toString(), "startupdelay").setValue(300);
				}
			}
		}
	}

	private void updateAltar(Player player)
	{
		if (!this.config.getNode("altar").isVirtual())
		{
			if (this.config.getNode("altar").getChildrenMap().get("world") == null)
			{
				this.config.getNode("altar", "world").setValue(player.getWorld().getUniqueId().toString());
			}
		}
	}
}
