package com.ricky30.prispongemine.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;

public class commandList implements CommandExecutor
{
	private ConfigurationNode config = null;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		this.config = prispongemine.plugin.getConfig();
		for (Object text: this.config.getNode("mineName").getChildrenMap().keySet())
		{
			//get the size of the mine
			int X1, X2, Y1, Y2, Z1, Z2;
			X1 = this.config.getNode("mineName", text.toString(), "depart_X").getInt();
			Y1 = this.config.getNode("mineName", text.toString(), "depart_Y").getInt();
			Z1 = this.config.getNode("mineName", text.toString(), "depart_Z").getInt();
			X2 = this.config.getNode("mineName", text.toString(), "fin_X").getInt();
			Y2 = this.config.getNode("mineName", text.toString(), "fin_Y").getInt();
			Z2 = this.config.getNode("mineName", text.toString(), "fin_Z").getInt();

			//converted to vector
			Vector3i first = new Vector3i(X1, Y1, Z1);
			Vector3i second = new Vector3i(X2, Y2, Z2);

			//here we look for which is greater than
			int x1, y1, z1;
			if (first.getX() < second.getX())
			{
				x1 = second.getX() - first.getX();
			}
			else
			{
				x1 = first.getX() - second.getX();

			}
			if (first.getY() < second.getY())
			{
				y1 = second.getY() - first.getY();
			}
			else
			{
				y1 = first.getY() - second.getY();
			}
			if (first.getZ() < second.getZ())
			{
				z1 = second.getZ() - first.getZ();
			}
			else
			{
				z1 = first.getZ() - second.getZ();
			}
			//set the size of the mine
			Vector3i size = new Vector3i(x1, y1, z1);
			size = size.add(1, 1, 1);
			//number of block inside the mine
			int total_block = size.getX() * size.getY() * size.getZ();
			//get the world of this mine
			String World = this.config.getNode("mineName", text.toString(), "world").getString();
			src.sendMessage(Text.of("Mine: " , text.toString()));
			src.sendMessage(Text.of("Coordinates: X=" , first.getX()," Y=", first.getY()," Z=", first.getZ(), " to X=" ,second.getX()," Y=", second.getY()," Z=", second.getZ()));
			src.sendMessage(Text.of("Size: " , total_block, " blocks"));
			src.sendMessage(Text.of("World: " , World));
		}
		
		return CommandResult.success();
	}

}
