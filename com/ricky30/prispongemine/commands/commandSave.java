package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.prispongemine;
import com.ricky30.prispongemine.events.interactionevents;

import ninja.leaping.configurate.ConfigurationNode;

public class commandSave implements CommandExecutor
{
	private ConfigurationNode config = null;
	
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		String Name = args.<String>getOne("name").get();
		Player player = (Player) src;
		String world = player.getWorld().getName();
		this.config = prispongemine.plugin.getConfig();
		if (this.config.getNode("mineName").getChildrenMap().get(Name) != null)
		{
			src.sendMessage(Text.of("Mine ", Name, " already saved. Use update instead or change name."));
			return CommandResult.empty();
		}
		if (interactionevents.IsreadytoFill())
		{
	        Vector3i positiondepart = interactionevents.getFirst();
	        Vector3i positionfin = interactionevents.getSecond();
	        this.config.getNode("mineName", Name).setValue("");
	        this.config.getNode("mineName", Name, "world").setValue(world);
	        this.config.getNode("mineName", Name, "depart_X").setValue(positiondepart.getX());
	        this.config.getNode("mineName", Name, "depart_Y").setValue(positiondepart.getY());
	        this.config.getNode("mineName", Name, "depart_Z").setValue(positiondepart.getZ());
	        this.config.getNode("mineName", Name, "fin_X").setValue(positionfin.getX());
	        this.config.getNode("mineName", Name, "fin_Y").setValue(positionfin.getY());
	        this.config.getNode("mineName", Name, "fin_Z").setValue(positionfin.getZ());
	        this.config.getNode("mineName", Name, "renewtime").setValue(24);
	        this.config.getNode("mineName", Name, "renewformat").setValue("HOURS");
	        prispongemine.plugin.save();
			src.sendMessage(Text.of("Mine " , Name, " saved"));
			return CommandResult.success();
		}
		else
		{
			src.sendMessage(Text.of("Not ready to save yet"));
		}
		return CommandResult.empty();
	}
}