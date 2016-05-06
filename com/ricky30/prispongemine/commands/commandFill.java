package com.ricky30.prispongemine.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.translator.ConfigurateTranslator;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.prispongemine;
import com.ricky30.prispongemine.task.FillTask;
import com.ricky30.prispongemine.utility.teleport;

import ninja.leaping.configurate.ConfigurationNode;

public class commandFill implements CommandExecutor
{
	private static Random RANDOM = new Random();

	private ConfigurationNode config = null;

	private final Map<String, BlockState> TEST_BLOCKS = new HashMap<String, BlockState>();

	private final Map<BlockState, Float> ore= new HashMap<BlockState, Float>();
	private final Map<BlockState, Integer> orecount= new HashMap<BlockState, Integer>();
	private final Map<BlockState, Integer> totaux= new HashMap<BlockState, Integer>();

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
				//here we convert string to blockstate
				//if the string is not like "BlockState="minecraft:stone[variant=smooth_granite]" it will never work
				final ConfigurateTranslator  tr = ConfigurateTranslator.instance();
				final ConfigurationNode node = this.config.getNode("mineName", Name, "items", text.toString());
				final DataContainer cont = tr.translateFrom(node);
				final BlockState state = BlockState.builder().build(cont).get();

				//get the percentage
				final float percentage = this.config.getNode("mineName", Name, "items", text.toString(), "percentage").getFloat();
				ore.put(state, percentage);
				orecount.put(state, 0);
				//add percentage to total;
				percentage_global += percentage;
			}
			//if random is set
			if (this.config.getNode("mineName", Name, "random").getBoolean())
			{
				//get number of ore
				final int Size = ore.size();
				//random with only one ore is useless
				if (Size > 1)
				{
					RANDOM = new Random();
					//random with half the ore
					for (int loop = 0; loop < (Size/2); loop++)
					{
						//get rand int
						final int Key = RANDOM.nextInt(Size);
						//an array of blockstate who get smaller with each loop
						final BlockState[] All = new BlockState[ore.size()];
						int number = 0;
						//copy all blockstate left
						for (final BlockState Bstate : ore.keySet())
						{
							All[number] = Bstate;
							number++;
						}
						//get the rand blockstate
						final BlockState state = All[Key];
						//remove it
						ore.remove(state);
						orecount.remove(state);
					}
				}
			}

			//100% is all block
			//we check for equal to prevent jump to else
			//prevent bad percentages
			if (percentage_global >= 100.0f)
			{
				percentage_global = 100.0f;
				for (final Entry<BlockState, Float> ore_number:ore.entrySet())
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
				ore.put(BlockTypes.STONE.getDefaultState(), percentage);
				orecount.put(BlockTypes.STONE.getDefaultState(), 0);
				for (final Entry<BlockState, Float> ore_number:ore.entrySet())
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
					//remplissage equal all block added
					remplissage += total_tmp;
				}
				//if there is block left then put stone to fill hole
				remplissage = total_block - remplissage;
				totaux.put(BlockTypes.STONE.getDefaultState(),totaux.get(BlockTypes.STONE.getDefaultState())+ remplissage);
			}

			//we add all ore to TEST_BLOCKS
			//this way we only include defined ore
			for (final Entry<BlockState, Float> ore_name:ore.entrySet())
			{
				TEST_BLOCKS.put(ore_name.getKey().getId(), ore_name.getKey());
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
			final World world = Sponge.getServer().getWorld(UUID.fromString(this.config.getNode("mineName", Name, "world").getString())).get();
			//next location in the world of our blocks inside a volume
			final MutableBlockVolume Mvolume = world.getBlockView(com.ricky30.prispongemine.utility.size.Min(first, second), com.ricky30.prispongemine.utility.size.Max(first, second));
			//teleport all player inside the mine if there is a spawn
			teleport.Doteleport(Name);
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

		for (final Entry<BlockState, Float> theore:ore.entrySet())
		{
			number_tmp++;
			final String orename = theore.getKey().getId();
			BlockState entry;
			entry = TEST_BLOCKS.get(orename);
			block = entry;
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
		BlockState entry[];
		int number = 0;
		int numberok = 0;

		while (!isOk)
		{
			RANDOM = new Random();
			//get a random block between all ore
			entry = TEST_BLOCKS.values().toArray(new BlockState[0]);
			block = entry[RANDOM.nextInt(entry.length)];
			number = -1;
			for (final Entry<BlockState, Float> theore:ore.entrySet())
			{
				number++;
				final BlockState orename = theore.getKey();
				if (orename.equals(block))
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
