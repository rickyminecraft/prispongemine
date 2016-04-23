package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.events.interactionevents;

public class commandDefine implements CommandExecutor
{

	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		interactionevents.Reset();
		src.sendMessage(Text.of("Ready to define a mine"));
		return CommandResult.success();
	}
}
