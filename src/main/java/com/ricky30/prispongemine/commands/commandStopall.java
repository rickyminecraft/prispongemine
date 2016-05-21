package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;
import com.ricky30.prispongemine.task.Timers;

import ninja.leaping.configurate.ConfigurationNode;

public class commandStopall implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		this.config = prispongemine.plugin.getConfig();
		for (final Object text: this.config.getNode("mineName").getChildrenMap().keySet())
		{
			Timers.remove(text.toString());
			src.sendMessage(Text.of("Mine ", text.toString(), " stop"));
		}
		src.sendMessage(Text.of("All mines stopped now"));
		return CommandResult.success();
	}
}
