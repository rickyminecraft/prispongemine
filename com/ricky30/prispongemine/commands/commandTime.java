package com.ricky30.prispongemine.commands;

import java.io.IOException;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;

public class commandTime implements CommandExecutor
{
	private ConfigurationNode config = null;
	
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		String Name = args.<String>getOne("name").get();
		int Time = args.<Integer>getOne("duration").get();
		String Format = args.<String>getOne("format").get();
		this.config = prispongemine.plugin.getConfig();
		if (this.config.getNode("prisonName").getChildrenMap().get(Name) != null)
		{
			if (Format.equals("SECONDS") || Format.equals("MINUTES") || Format.equals("HOURS") || Format.equals("DAYS"))
			{
				int X1, X2, Y1, Y2, Z1, Z2;
				X1 = this.config.getNode("prisonName", Name, "depart_X").getInt();
				Y1 = this.config.getNode("prisonName", Name, "depart_Y").getInt();
				Z1 = this.config.getNode("prisonName", Name, "depart_Z").getInt();
				X2 = this.config.getNode("prisonName", Name, "fin_X").getInt();
				Y2 = this.config.getNode("prisonName", Name, "fin_Y").getInt();
				Z2 = this.config.getNode("prisonName", Name, "fin_Z").getInt();
				this.config.getNode("prisonName", Name).setValue("");
				String world  =  this.config.getNode("prisonName", Name, "world").getString();
		        this.config.getNode("prisonName", Name, "world").setValue(world);
		        this.config.getNode("prisonName", Name, "depart_X").setValue(X1);
		        this.config.getNode("prisonName", Name, "depart_Y").setValue(Y1);
		        this.config.getNode("prisonName", Name, "depart_Z").setValue(Z1);
		        this.config.getNode("prisonName", Name, "fin_X").setValue(X2);
		        this.config.getNode("prisonName", Name, "fin_Y").setValue(Y2);
		        this.config.getNode("prisonName", Name, "fin_Z").setValue(Z2);
		        this.config.getNode("prisonName", Name, "renewtime").setValue(Time);
		        this.config.getNode("prisonName", Name, "renewformat").setValue(Format);
				try
				{
					prispongemine.plugin.getConfigManager().save(this.config);
				} catch (IOException e) 
		        {
					prispongemine.plugin.getLogger().error("Failed to update config file!", e);
		        }
				src.sendMessage(Text.of("Prison " , Name, " updated time & format"));
				return CommandResult.success();
			}
			src.sendMessage(Text.of("Wrong time format : use SECONDS MINUTES HOURS DAYS"));
			return CommandResult.empty();
		}
		src.sendMessage(Text.of("Prison " , Name, " doesn't exist"));
		return CommandResult.empty();
	}

}
