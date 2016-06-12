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
		File file = new File(path.getParent().toString()+"/Prisponge");
		//make the directory
		file.mkdirs();
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
	
}
