package com.ricky30.prispongemine.commands;

import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;
import com.ricky30.prispongemine.task.Timers;

import ninja.leaping.configurate.ConfigurationNode;

public class commandStart implements CommandExecutor {
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		final String Name = args.<String>getOne("name").get();
		this.config = prispongemine.plugin.getConfig();
		if (this.config.getNode("mineName").getChildrenMap().get(Name) != null) {
			final int time = this.config.getNode("mineName", Name, "renewtime").getInt();
			final String format = this.config.getNode("mineName", Name, "renewformat").getString();
			final String world =  this.config.getNode("mineName", Name, "world").getString();
			Timers.add(Name, time, format, UUID.fromString(world));
			src.sendMessage(Text.of("Mine " , Name, " timer start"));
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Mine " , Name, " not found"));
		return CommandResult.empty();
	}
}
