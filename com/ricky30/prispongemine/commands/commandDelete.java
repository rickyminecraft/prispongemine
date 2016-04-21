package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;
import com.ricky30.prispongemine.task.timers;

import ninja.leaping.configurate.ConfigurationNode;

public class commandDelete implements CommandExecutor
{
	private ConfigurationNode config = null;

	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		String Name = args.<String>getOne("name").get();
		this.config = prispongemine.plugin.getConfig();
		if (this.config.getNode("mineName").getChildrenMap().get(Name) != null)
		{
			timers.remove(Name);
			this.config.getNode("mineName").removeChild(Name);
			prispongemine.plugin.save();
			src.sendMessage(Text.of("Mine " , Name, " deleted"));
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Mine " , Name, " not found"));
		return CommandResult.empty();
	}

}
