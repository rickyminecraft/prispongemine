package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;

public class commandRunall implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		prispongemine.plugin.StartRunnningAll();
		src.sendMessage(Text.of("All mines starting now"));
		return CommandResult.success();
	}

}
