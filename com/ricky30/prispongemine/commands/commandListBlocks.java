package com.ricky30.prispongemine.commands;

import java.util.Collection;
import java.util.Map;
import org.spongepowered.api.GameDictionary.Entry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class commandListBlocks implements CommandExecutor
{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		Map<String, Collection<Entry>> Blocks = Sponge.getDictionary().getAll().asMap();
		src.sendMessage(Text.of("--Start--"));
		for (String a: Blocks.keySet())
		{
			src.sendMessage(Text.of(a));
		}
		src.sendMessage(Text.of("--End--"));
		return CommandResult.success();
	}

}
