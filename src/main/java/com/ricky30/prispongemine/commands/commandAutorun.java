package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.config.ManageConfig;

import ninja.leaping.configurate.ConfigurationNode;

public class commandAutorun implements CommandExecutor
{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final ConfigurationNode config = ManageConfig.getConfig();
		final String Name = args.<String>getOne("name").get();
		final boolean Autorun = args.<Boolean>getOne("autorun").get();
		if (config.getNode("mineName").getChildrenMap().get(Name) != null)
		{
			config.getNode("mineName", Name, "autorun").setValue(Autorun);
			ManageConfig.Save();
			if (Autorun)
			{
				src.sendMessage(Text.of("Mine " , Name, " autorun on"));
			}
			else
			{
				src.sendMessage(Text.of("Mine " , Name, " autorun off"));
			}
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Mine " + Name + " not found"));
		return CommandResult.empty();
	}

}
