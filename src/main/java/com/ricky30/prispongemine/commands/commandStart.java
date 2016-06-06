package com.ricky30.prispongemine.commands;

import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.config.ManageMines;
import com.ricky30.prispongemine.task.Timers;

import ninja.leaping.configurate.ConfigurationNode;

public class commandStart implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final String Name = args.<String>getOne("name").get();
		final boolean OK = ManageMines.LoadMine(Name);
		this.config = ManageMines.getConfig();
		if (OK)
		{
			final int time = this.config.getNode("renewtime").getInt();
			final String format = this.config.getNode("renewformat").getString();
			final String world =  this.config.getNode("world").getString();
			Timers.add(Name, time, format, UUID.fromString(world));
			src.sendMessage(Text.of("Mine " , Name, " timer start"));
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Mine " , Name, " not found"));
		return CommandResult.empty();
	}
}
