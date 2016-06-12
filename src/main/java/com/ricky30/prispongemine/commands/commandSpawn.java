package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.ricky30.prispongemine.config.ManageMines;

import ninja.leaping.configurate.ConfigurationNode;

public class commandSpawn implements CommandExecutor
{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final String Name = args.<String>getOne("name").get();
		final boolean OK = ManageMines.LoadMine(Name);
		final ConfigurationNode config = ManageMines.getConfig();
		final Player player = (Player) src;
		if (OK)
		{
			final Location<World> Spawn = player.getLocation();
			config.getNode("Spawn", "Spawn_X").setValue(Spawn.getX());
			config.getNode("Spawn", "Spawn_Y").setValue(Spawn.getY());
			config.getNode("Spawn", "Spawn_Z").setValue(Spawn.getZ());
			config.getNode("Spawn", "Spawn_Pitch").setValue(player.getRotation().getX());
			config.getNode("Spawn", "Spawn_Yaw").setValue(player.getRotation().getY());
			config.getNode("Spawn", "Spawn_Roll").setValue(player.getRotation().getZ());
			config.getNode("Spawn", "world").setValue(player.getWorld().getUniqueId().toString());
			ManageMines.SaveMine(Name, true);
			src.sendMessage(Text.of("Mine " , Name, " Spawn saved"));
			return CommandResult.success();
		}
		return CommandResult.empty();
	}

}
