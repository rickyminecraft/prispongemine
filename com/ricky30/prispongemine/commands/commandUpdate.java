package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.prispongemine;
import com.ricky30.prispongemine.events.interactionevents;

import ninja.leaping.configurate.ConfigurationNode;

public class commandUpdate implements CommandExecutor
{
	private ConfigurationNode config = null;
	
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		String Name = args.<String>getOne("name").get();
		this.config = prispongemine.plugin.getConfig();
		if (this.config.getNode("prisonName").getChildrenMap().get(Name) == null)
		{
			src.sendMessage(Text.of("Mine ", Name, " not found. Use save instead or change name."));
			return CommandResult.empty();
		}
		if (interactionevents.IsreadytoFill())
		{
	        Vector3i positiondepart = interactionevents.getFirst();
	        Vector3i positionfin = interactionevents.getSecond();

	        this.config.getNode("prisonName", Name, "depart_X").setValue(positiondepart.getX());
	        this.config.getNode("prisonName", Name, "depart_Y").setValue(positiondepart.getY());
	        this.config.getNode("prisonName", Name, "depart_Z").setValue(positiondepart.getZ());
	        this.config.getNode("prisonName", Name, "fin_X").setValue(positionfin.getX());
	        this.config.getNode("prisonName", Name, "fin_Y").setValue(positionfin.getY());
	        this.config.getNode("prisonName", Name, "fin_Z").setValue(positionfin.getZ());
	        prispongemine.plugin.save();
			src.sendMessage(Text.of("Mine " , Name, " updated"));
			return CommandResult.success();
		}
		else
		{
			src.sendMessage(Text.of("Not ready to update yet"));
		}
		return CommandResult.empty();
	}

}
