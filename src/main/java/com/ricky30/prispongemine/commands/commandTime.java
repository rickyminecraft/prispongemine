package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.config.ManageMines;

import ninja.leaping.configurate.ConfigurationNode;

public class commandTime implements CommandExecutor
{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final String Name = args.<String>getOne("name").get();
		int Time = args.<Integer>getOne("duration").get();
		final String Format = args.<String>getOne("format").get().toUpperCase();
		if (Time <10 && Format.equals("SECONDS"))
		{
			Time = 10;
		}
		final boolean OK = ManageMines.LoadMine(Name);
		ConfigurationNode config = ManageMines.getConfig();
		if (OK)
		{
			if (Format.equals("SECONDS") || Format.equals("MINUTES") || Format.equals("HOURS") || Format.equals("DAYS"))
			{
				config.getNode("renewtime").setValue(Time);
				config.getNode("renewformat").setValue(Format);
				ManageMines.SaveMine(Name, true);
				src.sendMessage(Text.of("Mine " , Name, " updated time & format"));
				return CommandResult.success();
			}
			src.sendMessage(Text.of("Wrong time format : use SECONDS MINUTES HOURS DAYS"));
			return CommandResult.empty();
		}
		src.sendMessage(Text.of("Mine " , Name, " not found"));
		return CommandResult.empty();
	}

}
