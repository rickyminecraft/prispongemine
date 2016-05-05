package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;

public class commandAutorun implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		this.config = prispongemine.plugin.getConfig();
		final String Name = args.<String>getOne("name").get();
		final boolean Autorun = args.<Boolean>getOne("autorun").get();
		if (this.config.getNode("mineName").getChildrenMap().get(Name) != null)
		{
			this.config.getNode("mineName", Name, "autorun").setValue(Autorun);
			src.sendMessage(Text.of("Mine " , Name, " updated autorun"));
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Mine " + Name + " not found"));
		return CommandResult.empty();
	}

}