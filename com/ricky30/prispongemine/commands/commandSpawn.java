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

import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;

public class commandSpawn implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final String Name = args.<String>getOne("name").get();
		this.config = prispongemine.plugin.getConfig();
		final Player player = (Player) src;
		if (this.config.getNode("mineName").getChildrenMap().get(Name) != null)
		{
			final Location<World> Spawn = player.getLocation();
			this.config.getNode("mineName", Name, "Spawn", "Spawn_X").setValue(Spawn.getX());
			this.config.getNode("mineName", Name, "Spawn", "Spawn_Y").setValue(Spawn.getY());
			this.config.getNode("mineName", Name, "Spawn", "Spawn_Z").setValue(Spawn.getZ());
			this.config.getNode("mineName", Name, "Spawn", "Spawn_Pitch").setValue(player.getRotation().getX());
			this.config.getNode("mineName", Name, "Spawn", "Spawn_Yaw").setValue(player.getRotation().getY());
			this.config.getNode("mineName", Name, "Spawn", "Spawn_Roll").setValue(player.getRotation().getZ());
			this.config.getNode("mineName", Name, "Spawn", "world").setValue(player.getWorld().getUniqueId().toString());
			prispongemine.plugin.save();
			src.sendMessage(Text.of("Mine " , Name, " Spawn saved"));
			return CommandResult.success();
		}
		return CommandResult.empty();
	}

}
