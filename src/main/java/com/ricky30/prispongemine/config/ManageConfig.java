package com.ricky30.prispongemine.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.spongepowered.api.item.ItemTypes;

import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ManageConfig
{
	private static ConfigurationNode ConfigNodes = null;
	private static ConfigurationLoader<CommentedConfigurationNode> configManager;
	private static Path config; //config directory: /config/Prisponge
	private static Path DefaultFile; //config file : /config/Prisponge/Prisponge.conf

	public static void Init(Path path)
	{
		if(path.toFile().exists())
		{
			update(path);
		}
		File file = new File(path.getParent().toString()+"/Prisponge");
		config = file.toPath();
		ManageMines.SetPath(config);
		file = new File(file.getPath()+"/Prisponge.conf");
		DefaultFile = file.toPath();

		try
		{
			//if the file does not exist
			if (file.createNewFile())
			{
				//then add everything
				configManager = HoconConfigurationLoader.builder().setPath(DefaultFile).build();
				ConfigNodes = configManager.load();
				if (ConfigNodes.getNode("ConfigVersion").isVirtual())
				{
					ConfigNodes.getNode("ConfigVersion").setValue(5);
				}
				final boolean OK = ConfigNodes.getNode("ConfigVersion").getValue().equals(5);
				if (!OK)
				{
					ConfigNodes.getNode("ConfigVersion").setValue(5);
				}
				if (ConfigNodes.getNode("tool").isVirtual())
				{
					ConfigNodes.getNode("tool").setValue(ItemTypes.STICK.getId());
				}
				if (ConfigNodes.getNode("RemindSecondList").isVirtual())
				{
					ConfigNodes.getNode("RemindSecondList").setValue("1, 2, 3, 4, 5, 10, 15, 30, 60, 90, 120, 180, 300");
				}
				if (ConfigNodes.getNode("messageDump").isVirtual())
				{
					ConfigNodes.getNode("messageDump").setValue("NoMessages");
				}
				if (ConfigNodes.getNode("showwarning").isVirtual())
				{
					ConfigNodes.getNode("showwarning").setValue(true);
				}
				configManager.save(ConfigNodes);
			}
			else
			{
				configManager = HoconConfigurationLoader.builder().setPath(DefaultFile).build();
				ConfigNodes = configManager.load();
			}
		} catch (final IOException e)
		{
			prispongemine.plugin.getLogger().error("Failed to access config file!", e);
		}
	}

	public static Path GetDefaultFilePath()
	{
		return DefaultFile;
	}

	public static Path GetDefaultPath()
	{
		return config;
	}

	public static ConfigurationLoader<CommentedConfigurationNode> getConfigManager()
	{
		return configManager;
	}

	public static ConfigurationNode getConfig()
	{
		return ConfigNodes;
	}

	public static void Save()
	{
		try
		{
			configManager.save(ConfigNodes);
		} catch (final IOException e)
		{
			prispongemine.plugin.getLogger().error("Failed to save config file!", e);
		}
	}

	public static void Load()
	{
		try
		{
			ConfigNodes = configManager.load();
		} catch (final IOException e)
		{
			prispongemine.plugin.getLogger().error("Failed to load config file!", e);
		}
	}

	private static void update(Path path)
	{
		final File original = new File(path.toString());
		final ConfigurationLoader<CommentedConfigurationNode> configManagerOriginal = HoconConfigurationLoader.builder().setPath(original.toPath()).build();
		ConfigurationNode ConfigNodesOriginal = null;
		try
		{
			ConfigNodesOriginal = configManagerOriginal.load();
		} catch (final IOException e1)
		{
			prispongemine.plugin.getLogger().error("Failed to access config file!", e1);
		}
		File file = new File(path.getParent().toString()+"/Prisponge");
		//make the directory
		file.mkdirs();
		file = new File(file.getPath()+"/Prisponge.conf");
		try
		{
			//config file
			configManager = HoconConfigurationLoader.builder().setPath(file.toPath()).build();
			ConfigNodes = configManager.load();
			ConfigNodes.getNode("ConfigVersion").setValue(5);
			if (ConfigNodesOriginal.getNode("tool").isVirtual())
			{
				ConfigNodes.getNode("tool").setValue(ItemTypes.STICK.getId());
			}
			else
			{
				ConfigNodes.getNode("tool").setValue(ConfigNodesOriginal.getNode("tool").getValue());
			}
			ConfigNodes.getNode("RemindSecondList").setValue("1, 2, 3, 4, 5, 10, 15, 30, 60, 90, 120, 180, 300");
			ConfigNodes.getNode("messageDump").setValue("NoMessages");
			if (ConfigNodesOriginal.getNode("showwarning").isVirtual())
			{
				ConfigNodes.getNode("showwarning").setValue(true);
			}
			else
			{
				ConfigNodes.getNode("showwarning").setValue(ConfigNodesOriginal.getNode("showwarning").getBoolean());
			}
			configManager.save(ConfigNodes);
			//end config file

			//mines config
			for (final Object nodes:ConfigNodesOriginal.getNode("mineName").getChildrenMap().keySet())
			{
				final String NodeName = nodes.toString();
				File mine = new File(path.getParent().toString()+"/Prisponge/Mines");
				mine.mkdirs();
				mine = new File(mine.getPath()+"/"+ NodeName +".conf");
				mine.createNewFile();
				configManager = HoconConfigurationLoader.builder().setPath(mine.toPath()).build();
				ConfigNodes = configManager.load();
				if (!ConfigNodesOriginal.getNode("mineName", NodeName, "Spawn").isVirtual())
				{
					ConfigNodes.getNode("Spawn", "Spawn_X").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "Spawn", "Spawn_X").getDouble());
					ConfigNodes.getNode("Spawn", "Spawn_Y").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "Spawn", "Spawn_Y").getDouble());
					ConfigNodes.getNode("Spawn", "Spawn_Z").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "Spawn", "Spawn_Z").getDouble());
					ConfigNodes.getNode("Spawn", "Spawn_Pitch").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "Spawn", "Spawn_Pitch").getDouble());
					ConfigNodes.getNode("Spawn", "Spawn_Yaw").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "Spawn", "Spawn_Yaw").getDouble());
					ConfigNodes.getNode("Spawn", "Spawn_Roll").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "Spawn", "Spawn_Roll").getDouble());
					ConfigNodes.getNode("Spawn", "world").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "Spawn", "world").getString());
				}
				ConfigNodes.getNode("autorun").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "autorun").getBoolean());
				ConfigNodes.getNode("random").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "random").getBoolean());
				ConfigNodes.getNode("renewformat").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "renewformat").getString());
				ConfigNodes.getNode("renewtime").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "renewtime").getInt());
				ConfigNodes.getNode("set").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "set").getInt());
				ConfigNodes.getNode("startupdelay").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "startupdelay").getInt());
				ConfigNodes.getNode("world").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "world").getString());
				ConfigNodes.getNode("depart_X").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "depart_X").getInt());
				ConfigNodes.getNode("depart_Y").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "depart_Y").getInt());
				ConfigNodes.getNode("depart_Z").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "depart_Z").getInt());
				ConfigNodes.getNode("fin_X").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "fin_X").getInt());
				ConfigNodes.getNode("fin_Y").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "fin_Y").getInt());
				ConfigNodes.getNode("fin_Z").setValue(ConfigNodesOriginal.getNode("mineName", NodeName, "fin_Z").getInt());

				configManager.save(ConfigNodes);
			}
			//end mines config
			original.delete();
		} catch (final IOException e)
		{
			prispongemine.plugin.getLogger().error("Failed to access config file!", e);
		}
	}
}
