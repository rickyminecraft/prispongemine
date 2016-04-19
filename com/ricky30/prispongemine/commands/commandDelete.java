package com.ricky30.prispongemine.commands;

import java.io.IOException;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;
import com.ricky30.prispongemine.task.timers;

import ninja.leaping.configurate.ConfigurationNode;

public class commandDelete implements CommandExecutor
{
	private ConfigurationNode config = null;

	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		String Name = args.<String>getOne("name").get();
		this.config = prispongemine.plugin.getConfig();
		if (this.config.getNode("prisonName").getChildrenMap().get(Name) != null)
		{
			timers.remove(Name);
			this.config.getNode("prisonName").removeChild(Name);
			try
			{
				prispongemine.plugin.getConfigManager().save(this.config);
			} catch (IOException e) 
	        {
				prispongemine.plugin.getLogger().error("Failed to update config file!", e);
	        }
			src.sendMessage(Text.of("Prison " , Name, " deleted"));
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Prison " , Name, " not found"));
		return CommandResult.empty();
	}

}
