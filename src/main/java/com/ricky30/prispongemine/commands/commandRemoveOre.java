package com.ricky30.prispongemine.commands;

import java.util.UUID;

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

import com.flowpowered.math.vector.Vector3i;
import com.ricky30.prispongemine.config.ManageConfig;
import com.ricky30.prispongemine.config.ManageMines;

import ninja.leaping.configurate.ConfigurationNode;

public class commandRemoveOre implements CommandExecutor
{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args)
			throws CommandException
	{
		final String Name = args.<String>getOne("name").get();
		final boolean OK = ManageMines.LoadMine(Name);
		final ConfigurationNode Mine = ManageMines.getConfig();
		final ConfigurationNode config = ManageConfig.getConfig();
		if (!config.getNode("altar").isVirtual())
		{
			if (OK)
			{
				for (final Object text: Mine.getNode("items").getChildrenMap().keySet())
				{
					final ConfigurateTranslator  tr = ConfigurateTranslator.instance();
					final ConfigurationNode node = Mine.getNode("items", text.toString());
					final DataContainer cont = tr.translateFrom(node);
					final BlockState state = BlockState.builder().build(cont).get();

					int X, Y, Z;
					X = config.getNode("altar", "altar_X").getInt();
					Y = config.getNode("altar", "altar_Y").getInt();
					Z = config.getNode("altar", "altar_Z").getInt();
					final String world = config.getNode("altar", "world").getString();
					final World Altar_World = Sponge.getServer().getWorld(UUID.fromString(world)).get();
					final Vector3i position_block = new Vector3i(X, Y, Z);

					final BlockState data = Altar_World.getBlock(position_block);

					if (data.equals(state))
					{
						Altar_World.setBlock(position_block, BlockTypes.AIR.getDefaultState());
						Mine.getNode("items").removeChild(text);
						ManageMines.SaveMine(Name, true);
						src.sendMessage(Text.of("Mine " + Name + " ore removed"));
					}
				}
				src.sendMessage(Text.of("Command finished"));
				return CommandResult.success();
			}
			src.sendMessage(Text.of("Mine " + Name + " not found"));
			return CommandResult.empty();
		}
		src.sendMessage(Text.of("you must define an altar"));
		return CommandResult.empty();
	}

}
