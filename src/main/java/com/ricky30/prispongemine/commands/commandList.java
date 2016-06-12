package com.ricky30.prispongemine.commands;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.config.ManageMines;

import ninja.leaping.configurate.ConfigurationNode;

public class commandList implements CommandExecutor
{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		for (String Name:ManageMines.GetAllMines())
		{
			//remove the ".conf" at end
			Name = Name.substring(0,  Name.length() -5);
			ManageMines.LoadMine(Name);
			final ConfigurationNode config = ManageMines.getConfig();
			//get the size of the mine
			int X1, X2, X4, Y1, Y2, Y4, Z1, Z2, Z4;
			double X3 = 0, Y3 = 0, Z3 = 0;
			X1 = config.getNode("depart_X").getInt();
			Y1 = config.getNode("depart_Y").getInt();
			Z1 = config.getNode("depart_Z").getInt();
			X2 = config.getNode("fin_X").getInt();
			Y2 = config.getNode("fin_Y").getInt();
			Z2 = config.getNode("fin_Z").getInt();

			boolean HasSpawn = false;
			if (!config.getNode("Spawn").isVirtual())
			{
				HasSpawn = true;
				X3 = config.getNode("Spawn", "Spawn_X").getDouble();
				Y3 = config.getNode("Spawn", "Spawn_Y").getDouble();
				Z3 = config.getNode("Spawn", "Spawn_Z").getDouble();
			}

			//converted to vector
			final Vector3i first = new Vector3i(X1, Y1, Z1);
			final Vector3i second = new Vector3i(X2, Y2, Z2);

			//here we look for which is greater than
			if (first.getX() < second.getX())
			{
				X4 = second.getX() - first.getX();
			}
			else
			{
				X4 = first.getX() - second.getX();

			}
			if (first.getY() < second.getY())
			{
				Y4 = second.getY() - first.getY();
			}
			else
			{
				Y4 = first.getY() - second.getY();
			}
			if (first.getZ() < second.getZ())
			{
				Z4 = second.getZ() - first.getZ();
			}
			else
			{
				Z4 = first.getZ() - second.getZ();
			}
			//set the size of the mine
			Vector3i size = new Vector3i(X4, Y4, Z4);
			size = size.add(1, 1, 1);
			//number of block inside the mine
			final int total_block = size.getX() * size.getY() * size.getZ();
			//get the world of this mine
			final UUID World = UUID.fromString(config.getNode("world").getString());
			src.sendMessage(Text.of("Mine: " , Name));
			src.sendMessage(Text.of("Coordinates: X=" , first.getX()," Y=", first.getY()," Z=", first.getZ(), " to X=" ,second.getX()," Y=", second.getY()," Z=", second.getZ()));
			if (HasSpawn)
			{
				src.sendMessage(Text.of("Spawn: X=" , X3," Y=", Y3," Z=", Z3));
			}
			src.sendMessage(Text.of("Size: " , total_block, " blocks"));
			src.sendMessage(Text.of("World: " , Sponge.getServer().getWorld(World).get().getName()));
		}

		return CommandResult.success();
	}

}
