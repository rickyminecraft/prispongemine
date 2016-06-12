package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.config.ManageMines;

import ninja.leaping.configurate.ConfigurationNode;

public class commandRandomOre implements CommandExecutor
{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final String Name = args.<String>getOne("name").get();
		final boolean Random = args.<Boolean>getOne("random").get();
		final boolean OK = ManageMines.LoadMine(Name);
		ConfigurationNode config = ManageMines.getConfig();
		if (OK)
		{
			config.getNode("random").setValue(Random);
			ManageMines.SaveMine(Name, true);
			if (Random)
			{
				src.sendMessage(Text.of("Mine " , Name, " random gen on"));
			}
			else
			{
				src.sendMessage(Text.of("Mine " , Name, " random gen off"));
			}
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Mine " + Name + " not found"));
		return CommandResult.empty();
	}

}
