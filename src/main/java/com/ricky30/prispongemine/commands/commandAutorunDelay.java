package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.config.ManageMines;

import ninja.leaping.configurate.ConfigurationNode;

public class commandAutorunDelay implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final String Name = args.<String>getOne("name").get();
		final int Delay = args.<Integer>getOne("delay").get();
		final boolean OK = ManageMines.LoadMine(Name);
		this.config = ManageMines.getConfig();
		if (OK)
		{
			if (Delay >= 60)
			{
				this.config.getNode("startupdelay").setValue(Delay);
				return CommandResult.success();
			}
			else
			{
				this.config.getNode("startupdelay").setValue(60);
				src.sendMessage(Text.of("Delay must be at last 60 seconds"));
			}
			return CommandResult.empty();
		}
		return CommandResult.empty();
	}

}