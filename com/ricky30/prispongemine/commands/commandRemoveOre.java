package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;

public class commandRemoveOre implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		String Name = args.<String>getOne("name").get();
		String Orename = args.<String>getOne("orename").get();
		this.config = prispongemine.plugin.getConfig();
		if (this.config.getNode("mineName").getChildrenMap().get(Name) != null)
		{
			for (Object text: this.config.getNode("mineName", Name, "items").getChildrenMap().keySet())
			{
				String ore = this.config.getNode("mineName", Name, "items", text.toString()).getString();
				ore = ore.substring(1, ore.indexOf("="));
				if (ore.equals(Orename))
				{
					this.config.getNode("mineName", Name, "items").removeChild(text);
					prispongemine.plugin.save();
					src.sendMessage(Text.of("Mine " + Name + ": " + ore + " removed"));
				}
			}
			src.sendMessage(Text.of("Command finished"));
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Mine " + Name + " not found"));
		return CommandResult.empty();
	}

}
