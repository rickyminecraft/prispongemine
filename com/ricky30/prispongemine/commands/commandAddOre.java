package com.ricky30.prispongemine.commands;

import org.spongepowered.api.GameDictionary.Entry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.google.common.collect.SetMultimap;
import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;

public class commandAddOre implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{

		final SetMultimap<String, Entry> blocks = Sponge.getDictionary().getAll();
		final String Name = args.<String>getOne("name").get();
		final String Orename = args.<String>getOne("orename").get();
		final int Percentage = args.<Integer>getOne("percentage").get();
		if (Percentage <0 || Percentage >100)
		{
			src.sendMessage(Text.of("Wrong percentage"));
			return CommandResult.empty();
		}
		if (blocks.containsKey(Orename))
		{
			this.config = prispongemine.plugin.getConfig();
			if (this.config.getNode("mineName").getChildrenMap().get(Name) != null)
			{
				boolean Isnothere = false;
				int position = -1;
				while (!Isnothere)
				{
					position++;
					if (this.config.getNode("mineName", Name, "items", "item_".concat(String.valueOf(position))).getString() == null)
					{
						Isnothere = true;
					}
				}
				this.config.getNode("mineName", Name, "items", "item_".concat(String.valueOf(position))).setValue(Orename);
				this.config.getNode("mineName", Name, "items", "item_".concat(String.valueOf(position)), Orename).setValue(Percentage);
				prispongemine.plugin.save();
				src.sendMessage(Text.of("Mine " , Name, " add ore ", Orename));
				return CommandResult.success();
			}
			src.sendMessage(Text.of("Mine " + Name + " not found"));
			return CommandResult.empty();
		}
		src.sendMessage(Text.of("Ore not found"));
		return CommandResult.empty();
	}

}
