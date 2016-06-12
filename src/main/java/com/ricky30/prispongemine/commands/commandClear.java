package com.ricky30.prispongemine.commands;

import java.util.UUID;

import org.spongepowered.api.Sponge;
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
import com.ricky30.prispongemine.config.ManageMines;
import com.ricky30.prispongemine.task.ClearTask;
import com.ricky30.prispongemine.utility.size;

import ninja.leaping.configurate.ConfigurationNode;

public class commandClear implements CommandExecutor
{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final String Name = args.<String>getOne("name").get();
		final boolean OK = ManageMines.LoadMine(Name);
		final ConfigurationNode config = ManageMines.getConfig();
		if (OK)
		{
			//get the size of the prison
			int X1, X2, X3, Y1, Y2, Y3, Z1, Z2, Z3;
			X1 = config.getNode("depart_X").getInt();
			Y1 = config.getNode("depart_Y").getInt();
			Z1 = config.getNode("depart_Z").getInt();
			X2 = config.getNode("fin_X").getInt();
			Y2 = config.getNode("fin_Y").getInt();
			Z2 = config.getNode("fin_Z").getInt();

			//converted to vector
			final Vector3i first = new Vector3i(X1, Y1, Z1);
			final Vector3i second = new Vector3i(X2, Y2, Z2);

			//here we look for which is greater than
			if (first.getX() < second.getX())
			{
				X3 = second.getX() - first.getX();
			}
			else
			{
				X3 = first.getX() - second.getX();

			}
			if (first.getY() < second.getY())
			{
				Y3 = second.getY() - first.getY();
			}
			else
			{
				Y3 = first.getY() - second.getY();
			}
			if (first.getZ() < second.getZ())
			{
				Z3 = second.getZ() - first.getZ();
			}
			else
			{
				Z3 = first.getZ() - second.getZ();
			}

			//set the size of the prison
			Vector3i taille = new Vector3i(X3, Y3, Z3);
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
						volume.setBlock(x, y, z, BlockTypes.AIR.getDefaultState());
					}
				}
			}
			//get the world where the prison is located
			final World world = Sponge.getServer().getWorld(UUID.fromString(config.getNode("world").getString())).get();
			final MutableBlockVolume Mvolume = world.getBlockView(size.Min(first, second), size.Max(first, second));
			//fill the prison with buffer blocks
			ClearTask.Fill(Mvolume, size.Min(first, second), size.Max(first, second), Name);
			prispongemine.plugin.StartTaskClear();
			prispongemine.plugin.getLogger().info("Mine " + Name + " start clear");
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Mine " + Name + " not found"));
		return CommandResult.empty();
	}

}
