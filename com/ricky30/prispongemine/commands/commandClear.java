package com.ricky30.prispongemine.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.MutableBlockVolume;

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;

public class commandClear implements CommandExecutor
{
	private ConfigurationNode config = null;

	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		String Name = args.<String>getOne("name").get();
		this.config = prispongemine.plugin.getConfig();
		if (this.config.getNode("prisonName").getChildrenMap().get(Name) != null)
		{
			//get the size of the prison
			int X1, X2, Y1, Y2, Z1, Z2;
			X1 = this.config.getNode("prisonName", Name, "depart_X").getInt();
			Y1 = this.config.getNode("prisonName", Name, "depart_Y").getInt();
			Z1 = this.config.getNode("prisonName", Name, "depart_Z").getInt();
			X2 = this.config.getNode("prisonName", Name, "fin_X").getInt();
			Y2 = this.config.getNode("prisonName", Name, "fin_Y").getInt();
			Z2 = this.config.getNode("prisonName", Name, "fin_Z").getInt();
			
			//converted to vector
			Vector3i first = new Vector3i(X1, Y1, Z1);
			Vector3i second = new Vector3i(X2, Y2, Z2);

			//here we look for which is greater than
			int x1, y1, z1, x2, y2, z2;
			if (first.getX() < second.getX())
			{
				x1 = second.getX() - first.getX();
				x2 = first.getX();
			}
			else
			{
				x1 = first.getX() - second.getX();
				x2 = second.getX();

			}
			if (first.getY() < second.getY())
			{
				y1 = second.getY() - first.getY();
				y2 = first.getY();
			}
			else
			{
				y1 = first.getY() - second.getY();
				y2 = second.getY();
			}
			if (first.getZ() < second.getZ())
			{
				z1 = second.getZ() - first.getZ();
				z2 = first.getZ();
			}
			else
			{
				z1 = first.getZ() - second.getZ();
				z2 = second.getZ();
			}
			
			//set the size of the prison
			Vector3i taille = new Vector3i(x1, y1, z1);
			taille = taille.add(1, 1, 1);
			
			final MutableBlockVolume volume = prispongemine.EXTENT_BUFFER_FACTORY.createBlockBuffer(taille);
			final Vector3i min = volume.getBlockMin();
			final Vector3i max = volume.getBlockMax();
			for (int x = min.getX(); x <= max.getX(); x++) 
			{
				for (int y = min.getY(); y <= max.getY(); y++) 
				{
					for (int z = min.getZ(); z <= max.getZ(); z++) 
					{
						volume.setBlock(x, y, z, (BlockState) BlockTypes.AIR.getDefaultState());
					}
				}
			}
			//get the world where the prison is located
			World world = Sponge.getServer().getWorld(this.config.getNode("prisonName", Name, "world").getString()).get();
			
			//fill the prison with buffer blocks
			for (int x = min.getX(); x <= max.getX(); x++) 
			{
				for (int y = min.getY(); y <= max.getY(); y++) 
				{
					for (int z = min.getZ(); z <= max.getZ(); z++) 
					{
						world.setBlock(x2 + x, y2 + y, z2 + z, BlockTypes.AIR.getDefaultState());
					}
				}
			}
			prispongemine.plugin.getLogger().info("Prison " + Name + " cleared");
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Prison " + Name + " not found"));
		return CommandResult.empty();
	}

}
