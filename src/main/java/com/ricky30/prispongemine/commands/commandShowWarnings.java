package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.config.ManageConfig;

import ninja.leaping.configurate.ConfigurationNode;

public class commandShowWarnings implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final Boolean ShowWarning = args.<Boolean>getOne("show").get();
		this.config = ManageConfig.getConfig();
		this.config.getNode("showwarning").setValue(ShowWarning);
		ManageConfig.Save();
		if (ShowWarning)
		{
			src.sendMessage(Text.of("Warning on"));
		}
		else
		{
			src.sendMessage(Text.of("Warning off"));
		}
		return CommandResult.success();
	}

}
