package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;

public class commandRandomOre implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		this.config = prispongemine.plugin.getConfig();
		final String Name = args.<String>getOne("name").get();
		final boolean Random = args.<Boolean>getOne("random").get();
		if (this.config.getNode("mineName").getChildrenMap().get(Name) != null)
		{
			this.config.getNode("mineName", Name, "random").setValue(Random);
			prispongemine.plugin.save();
			src.sendMessage(Text.of("Mine " , Name, " updated random gen"));
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Mine " + Name + " not found"));
		return CommandResult.empty();
	}

}
