package com.ricky30.prispongemine.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
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
import com.ricky30.prispongemine.task.FillTask;
import ninja.leaping.configurate.ConfigurationNode;

public class commandFill implements CommandExecutor
{
	private static Random RANDOM = new Random();

	private ConfigurationNode config = null;

	private final Map<String, org.spongepowered.api.GameDictionary.Entry> TEST_BLOCKS = new HashMap<String, org.spongepowered.api.GameDictionary.Entry>(256);;

	private final Map<String, Float> ore= new HashMap<String, Float>();
	private final Map<String, Integer> orecount= new HashMap<String, Integer>();
	private final Map<String, Integer> totaux= new HashMap<String, Integer>();

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		//get the mine name
		final String Name = args.<String>getOne("name").get();
		//init the config
		this.config = prispongemine.plugin.getConfig();
		//if the mine is defined then fine
		if (this.config.getNode("mineName").getChildrenMap().get(Name) != null)
		{
			//get the size of the mine
			int X1, X2, Y1, Y2, Z1, Z2;
			X1 = this.config.getNode("mineName", Name, "depart_X").getInt();
			Y1 = this.config.getNode("mineName", Name, "depart_Y").getInt();
			Z1 = this.config.getNode("mineName", Name, "depart_Z").getInt();
			X2 = this.config.getNode("mineName", Name, "fin_X").getInt();
			Y2 = this.config.getNode("mineName", Name, "fin_Y").getInt();
			Z2 = this.config.getNode("mineName", Name, "fin_Z").getInt();

			//converted to vector
			final Vector3i first = new Vector3i(X1, Y1, Z1);
			final Vector3i second = new Vector3i(X2, Y2, Z2);

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
			final int total_block = size.getX() * size.getY() * size.getZ();

			float percentage_global = 0.0f;
			int remplissage = 0;
			for (final Object text: this.config.getNode("mineName", Name, "items").getChildrenMap().keySet())
			{
				//get the ore name
				String orename = this.config.getNode("mineName", Name, "items", text.toString()).getString();
				//remove useless things: {minecraft:stone=10 -> minecraft:stone
				orename = orename.substring(1, orename.indexOf("="));
				//get the percentage
				final float percentage = this.config.getNode("mineName", Name, "items", text.toString(), orename).getFloat();
				ore.put(orename, percentage);
				orecount.put(orename, 0);
				//add percentage to total;
				percentage_global += percentage;
			}
			//100% is all block
			//we check for equal to prevent jump to else
			//prevent bad percentages
			if (percentage_global >= 100.0f)
			{
				percentage_global = 100.0f;
				for (final Entry<String, Float> ore_number:ore.entrySet())
				{
					//get number of block for each percentage
					final float total_block_float = total_block;
					final float total_tmp_float = (total_block_float*ore_number.getValue())/100.0f;
					int total_tmp = (int) total_tmp_float;
					//prevent an infinite loop
					if (total_tmp == 0)
					{
						//at last one block
						total_tmp = 1;
					}
					//totaux equal number of block
					totaux.put(ore_number.getKey(), total_tmp);
				}
			}
			else
			{
				//here we reduce percentage_global who is equal to all percentages defined for 'ore' from 100%
				final float percentage = 100.0f - percentage_global;
				//add stone to ore list
				ore.put("stone", percentage);
				orecount.put("stone", 0);
				for (final Entry<String, Float> ore_number:ore.entrySet())
				{
					//get number of block for each percentage
					final float total_block_float = total_block;
					final float total_tmp_float = (total_block_float*ore_number.getValue())/100.0f;
					int total_tmp = (int) total_tmp_float;
					//int total_tmp = (total_block*ore_number.getValue().intValue())/100;
					//prevent an infinite loop
					if (total_tmp == 0)
					{
						//at last one block
						total_tmp = 1;
					}
					//totaux equal number of block
					totaux.put(ore_number.getKey(), total_tmp);
					//remplissage equal all block added
					remplissage += total_tmp;
				}
				//if there is block left then put stone to fill hole
				remplissage = total_block - remplissage;
				totaux.put("stone",totaux.get("stone")+ remplissage);
			}

			//we add all ore to TEST_BLOCKS
			//this way we only include defined ore
			org.spongepowered.api.GameDictionary.Entry entry;
			for (final Entry<String, Float> ore_name:ore.entrySet())
			{
				entry = Sponge.getDictionary().get(ore_name.getKey()).iterator().next();
				TEST_BLOCKS.put(ore_name.getKey(), entry);
			}

			final MutableBlockVolume volume = prispongemine.EXTENT_BUFFER_FACTORY.createBlockBuffer(size);
			//we fill a list with all randomly generated int
			final List<Integer> hazard = new ArrayList<Integer>();
			final Vector3i min = volume.getBlockMin();
			final Vector3i max = volume.getBlockMax();
			for (int x = min.getX(); x <= max.getX(); x++) 
			{
				for (int y = min.getY(); y <= max.getY(); y++) 
				{
					for (int z = min.getZ(); z <= max.getZ(); z++) 
					{
						hazard.add(getRandomBlockint());
					}
				}
			}
			//and we shuffle it - WARNING - absolutely necessary - WARNING -
			java.util.Collections.shuffle(hazard);
			//then we fill a buffer with corresponding block
			int hazard_number = -1;
			for (int x = min.getX(); x <= max.getX(); x++) 
			{
				for (int y = min.getY(); y <= max.getY(); y++) 
				{
					for (int z = min.getZ(); z <= max.getZ(); z++) 
					{
						hazard_number++;
						volume.setBlock(x, y, z, getRandomBlock(hazard.get(hazard_number)));
					}
				}
			}
			//get the world where the mine is located
			final World world = Sponge.getServer().getWorld(this.config.getNode("mineName", Name, "world").getString()).get();
			//next location in the world of our blocks inside a volume
			final MutableBlockVolume Mvolume = world.getBlockView(com.ricky30.prispongemine.utility.size.Min(first, second), com.ricky30.prispongemine.utility.size.Max(first, second));
			//fill the mine with buffer blocks
			FillTask.Fill(volume, Mvolume, com.ricky30.prispongemine.utility.size.Min(first, second), size, Name);
			prispongemine.plugin.StartTaskFill();
			prispongemine.plugin.getLogger().info("Mine " + Name + " start filling");
			//clear all
			totaux.clear();
			orecount.clear();
			ore.clear();
			return CommandResult.success();
		}
		src.sendMessage(Text.of("Mine " , Name, " not found"));
		return CommandResult.empty();
	}


	private BlockState getRandomBlock(int number) 
	{
		BlockState block = null;
		int number_tmp = -1;

		for (final Entry<String, Float> theore:ore.entrySet())
		{
			number_tmp++;
			final String orename = theore.getKey();
			org.spongepowered.api.GameDictionary.Entry entry;
			entry = TEST_BLOCKS.get(orename);
			block = entry.getType().getBlock().get().getDefaultState();
			if (number_tmp == number)
			{
				break;
			}
		}
		return block;
	}

	private int getRandomBlockint() 
	{
		boolean isOk = false;
		BlockState block = null;
		org.spongepowered.api.GameDictionary.Entry entry[];
		int number = 0;
		int numberok = 0;

		while (!isOk)
		{
			RANDOM = new Random();
			//get a random block between all ore
			entry = TEST_BLOCKS.values().toArray(new org.spongepowered.api.GameDictionary.Entry[0]);
			block = entry[RANDOM.nextInt(entry.length)].getType().getBlock().get().getDefaultState();
			number = -1;
			for (final Entry<String, Float> theore:ore.entrySet())
			{
				number++;
				final String orename = theore.getKey();
				final org.spongepowered.api.GameDictionary.Entry theentry = Sponge.getDictionary().get(orename).iterator().next();
				final BlockState blockore = theentry.getType().getBlock().get().getDefaultState();
				if (blockore == block)
				{
					//if equal, check if we already have all block for this ore
					if (totaux.get(orename) > orecount.get(orename))
					{
						//else add one
						orecount.put(orename, orecount.get(orename)+1);
						isOk = true;
						numberok = number;
					}
				}
			}
		}
		return numberok;
	}
}
