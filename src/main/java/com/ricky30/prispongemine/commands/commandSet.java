package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import com.ricky30.prispongemine.config.ManageMines;

import ninja.leaping.configurate.ConfigurationNode;

public class commandSet implements CommandExecutor
{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final String Name = args.<String>getOne("name").get();
		final int SetNumber = args.<Integer>getOne("setnumber").get();
		final boolean OK = ManageMines.LoadMine(Name);
		final ConfigurationNode config = ManageMines.getConfig();
		if (OK)
		{
			config.getNode("set").setValue(SetNumber);
			ManageMines.SaveMine(Name, true);
			return CommandResult.success();
		}
		return CommandResult.empty();
	}
}
