package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class commandPrisponge implements CommandExecutor
{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		src.sendMessage(Text.of("Prispongemine plugin"));
		src.sendMessage(Text.of("Usage:"));
		src.sendMessage(Text.of("/prisponge define"));
		src.sendMessage(Text.of("/prisponge altar"));
		src.sendMessage(Text.of("/prisponge startall"));
		src.sendMessage(Text.of("/prisponge stopall"));
		src.sendMessage(Text.of("/prisponge random"));
		src.sendMessage(Text.of("/prisponge autorun NAME boolean"));
		src.sendMessage(Text.of("/prisponge warning boolean"));
		src.sendMessage(Text.of("/prisponge reload"));
		src.sendMessage(Text.of("/prisponge list"));
		src.sendMessage(Text.of("/prisponge spawn NAME"));
		src.sendMessage(Text.of("/prisponge clear NAME"));
		src.sendMessage(Text.of("/prisponge fill NAME"));
		src.sendMessage(Text.of("/prisponge save NAME"));
		src.sendMessage(Text.of("/prisponge update NAME"));
		src.sendMessage(Text.of("/prisponge delete NAME"));
		src.sendMessage(Text.of("/prisponge time NAME time format"));
		src.sendMessage(Text.of("/prisponge start NAME"));
		src.sendMessage(Text.of("/prisponge stop NAME"));
		src.sendMessage(Text.of("/prisponge addore NAME percentage"));
		src.sendMessage(Text.of("/prisponge removeore NAME"));
		src.sendMessage(Text.of("/prisponge set NAME set_number"));
		src.sendMessage(Text.of("/prisponge autorundelay NAME delay"));
		return CommandResult.success();
	}

}
