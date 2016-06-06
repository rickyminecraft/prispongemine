package com.ricky30.prispongemine.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import com.ricky30.prispongemine.prispongemine;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ManageMines
{
	private static ConfigurationNode ConfigNodes = null;
	private static ConfigurationLoader<CommentedConfigurationNode> configManager;
	private static Path config; //config directory: /config/Prisponge/Mines

	public static void SetPath(Path path)
	{
		final File file = new File(path+"/Mines");
		file.mkdirs();
		config = file.toPath();
	}

	public static ConfigurationLoader<CommentedConfigurationNode> getConfigManager()
	{
		return configManager;
	}

	public static ConfigurationNode getConfig()
	{
		return ConfigNodes;
	}

	public static boolean SaveMine(String Name, boolean overWrite)
	{
		Boolean OK = true;
		if (overWrite)
		{
			final File file = new File(config+"/"+ Name+".conf");
			configManager = HoconConfigurationLoader.builder().setPath(file.toPath()).build();
			try
			{
				configManager.save(ConfigNodes);
			} catch (IOException e)
			{
				prispongemine.plugin.getLogger().error("Failed to save config file!", e);
			}
			return OK;
		}
		final File file = new File(config+"/"+ Name+".conf");
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch (final IOException e)
			{
				prispongemine.plugin.getLogger().error("Failed to save config file!", e);
			}
		}
		else
		{
			OK = false;
		}
		configManager = HoconConfigurationLoader.builder().setPath(file.toPath()).build();
		try
		{
			ConfigNodes = configManager.load();
		} catch (final IOException e)
		{
			prispongemine.plugin.getLogger().error("Failed to load config file!", e);
		}
		return OK;
	}

	public static boolean LoadMine(String Name)
	{
		boolean OK = true;
		final File file = new File(config+"/"+ Name+".conf");
		try
		{
			if (!file.exists())
			{
				OK = false;
			}
			configManager = HoconConfigurationLoader.builder().setPath(file.toPath()).build();
			ConfigNodes = configManager.load();

		} catch (final IOException e)
		{
			prispongemine.plugin.getLogger().error("Failed to access mine config file!", e);
		}
		return OK;
	}

	public static boolean RemoveMine(String Name)
	{
		boolean OK = false;
		final File file = new File(config+"/"+ Name+".conf");
		if (file.exists())
		{
			OK = file.delete();
		}
		return OK;
	}

	public static String[] GetAllMines()
	{
		final File file = new File(config.toString());
		final String[] list = file.list();
		return list;
	}
}
