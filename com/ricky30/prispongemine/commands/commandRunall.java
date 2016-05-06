package com.ricky30.prispongemine.commands;

import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;
import com.ricky30.prispongemine.task.timers;

import ninja.leaping.configurate.ConfigurationNode;

public class commandRunall implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		this.config = prispongemine.plugin.getConfig();
		for (final Object text: this.config.getNode("mineName").getChildrenMap().keySet())
		{
			final int time = this.config.getNode("mineName", text.toString(), "renewtime").getInt();
			final String format = this.config.getNode("mineName", text.toString(), "renewformat").getString();
			final String world =  this.config.getNode("mineName", text.toString(), "world").getString();
			timers.add(text.toString(), time, format, UUID.fromString(world));
			src.sendMessage(Text.of("Mine ", text.toString(), " start"));
		}
		src.sendMessage(Text.of("All mines started now"));
		return CommandResult.success();
	}

}
