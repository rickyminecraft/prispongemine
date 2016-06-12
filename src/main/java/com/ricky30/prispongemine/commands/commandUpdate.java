package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.config.ManageMines;
import com.ricky30.prispongemine.events.interactionevents;

import ninja.leaping.configurate.ConfigurationNode;

public class commandUpdate implements CommandExecutor
{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final String Name = args.<String>getOne("name").get();
		final Player player = (Player) src;
		final boolean OK = ManageMines.LoadMine(Name);
		final ConfigurationNode config = ManageMines.getConfig();
		if (!OK)
		{
			src.sendMessage(Text.of("Mine ", Name, " not found. Use save instead or change name."));
			return CommandResult.empty();
		}
		if (interactionevents.IsreadytoFill(player))
		{
			final Vector3i positiondepart = interactionevents.getFirst(player);
			final Vector3i positionfin = interactionevents.getSecond(player);

			config.getNode("depart_X").setValue(positiondepart.getX());
			config.getNode("depart_Y").setValue(positiondepart.getY());
			config.getNode("depart_Z").setValue(positiondepart.getZ());
			config.getNode("fin_X").setValue(positionfin.getX());
			config.getNode("fin_Y").setValue(positionfin.getY());
			config.getNode("fin_Z").setValue(positionfin.getZ());
			ManageMines.SaveMine(Name, true);
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
