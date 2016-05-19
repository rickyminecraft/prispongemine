package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;

public class commandTime implements CommandExecutor
{
	private ConfigurationNode config = null;

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
		this.config = prispongemine.plugin.getConfig();
		if (this.config.getNode("mineName").getChildrenMap().get(Name) != null)
		{
			if (Format.equals("SECONDS") || Format.equals("MINUTES") || Format.equals("HOURS") || Format.equals("DAYS"))
			{
				this.config.getNode("mineName", Name, "renewtime").setValue(Time);
				this.config.getNode("mineName", Name, "renewformat").setValue(Format);
				prispongemine.plugin.save();
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
