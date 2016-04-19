package com.ricky30.prispongemine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.extent.ExtentBufferFactory;

import com.google.inject.Inject;
import com.ricky30.prispongemine.commands.commandAddOre;
import com.ricky30.prispongemine.commands.commandClear;
import com.ricky30.prispongemine.commands.commandDefine;
import com.ricky30.prispongemine.commands.commandDelete;
import com.ricky30.prispongemine.commands.commandFill;
import com.ricky30.prispongemine.commands.commandList;
import com.ricky30.prispongemine.commands.commandListBlocks;
import com.ricky30.prispongemine.commands.commandPrisponge;
import com.ricky30.prispongemine.commands.commandReload;
import com.ricky30.prispongemine.commands.commandRemoveOre;
import com.ricky30.prispongemine.commands.commandSave;
import com.ricky30.prispongemine.commands.commandStart;
import com.ricky30.prispongemine.commands.commandStop;
import com.ricky30.prispongemine.commands.commandTime;
import com.ricky30.prispongemine.commands.commandUpdate;
import com.ricky30.prispongemine.events.interactionevents;
import com.ricky30.prispongemine.task.timers;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "com.ricky30.prisponge", name = "prisponge", version = "1.0.1")
public class prispongemine
{
	public static Game game = null;
	public static ExtentBufferFactory EXTENT_BUFFER_FACTORY;
	@Inject
	private Logger logger;
	private ConfigurationNode config = null;
	public static prispongemine plugin;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfig;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;
	
	private Scheduler scheduler = Sponge.getScheduler();
	private Task.Builder taskBuilder = scheduler.createTaskBuilder();
	private Task task;
	
	public Task gettasks()
	{
		return this.task;
	}
	
	public Task.Builder getTaskbuilder()
	{
		return this.taskBuilder;
	}
	
	public ConfigurationNode getConfig()
	{
		return this.config;
	}
	
	public Path getDefaultConfig() 
	{
        return this.defaultConfig;
    }
	
	public ConfigurationLoader<CommentedConfigurationNode> getConfigManager() 
	{
        return this.configManager;
    }
	
	public Logger getLogger()
	{
		return this.logger;
	}

	@Listener
	public void onServerStart(GameInitializationEvent event)
	{
		getLogger().info("Prisponge start.");
		EXTENT_BUFFER_FACTORY = Sponge.getRegistry().getExtentBufferFactory();
		plugin = this;
		game = Sponge.getGame();
		try
		{
			reload();
			if (!Files.exists(getDefaultConfig())) 
			{

				Files.createFile(getDefaultConfig());
				setupconfig();

				getConfigManager().save(this.config);
			}
		}
		catch (IOException e)
		{
			getLogger().error("Couldn't create default configuration file!");
		}
		
		task = prispongemine.plugin.getTaskbuilder().execute(new Runnable()
		{
			public void run()
			{
				timers.run();
			}
		}).interval(1, TimeUnit.SECONDS).name("prisponge").submit(this);
		
		Sponge.getEventManager().registerListeners(this, new interactionevents());
		
		HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();
		
		subcommands.put(Arrays.asList("fill"), CommandSpec.builder()
				.description(Text.of("fill a prison"))
				.permission("prisponge.fill")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandFill())
				.build());
		subcommands.put(Arrays.asList("define"), CommandSpec.builder()
				.description(Text.of("active use of stick to define a prison"))
				.permission("prisponge.define")
				.executor(new commandDefine())
				.build());
		subcommands.put(Arrays.asList("save"), CommandSpec.builder()
				.description(Text.of("save a defined prison"))
				.permission("prisponge.save")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandSave())
				.build());
		subcommands.put(Arrays.asList("update"), CommandSpec.builder()
				.description(Text.of("update an already defined prison"))
				.permission("prisponge.update")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandUpdate())
				.build());
		subcommands.put(Arrays.asList("delete"), CommandSpec.builder()
				.description(Text.of("delete a prison"))
				.permission("prisponge.delete")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandDelete())
				.build());
		subcommands.put(Arrays.asList("start"), CommandSpec.builder()
				.description(Text.of("start autorefill of a prison"))
				.permission("prisponge.start")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandStart())
				.build());
		subcommands.put(Arrays.asList("stop"), CommandSpec.builder()
				.description(Text.of("stop autorefill of a prison"))
				.permission("prisponge.stop")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandStop())
				.build());
		subcommands.put(Arrays.asList("time"), CommandSpec.builder()
				.description(Text.of("set refill time of a prison there is few warnings before a prison refill"))
				.permission("prisponge.time")
				.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))),
				GenericArguments.onlyOne(GenericArguments.integer(Text.of("duration"))),
				GenericArguments.onlyOne(GenericArguments.string(Text.of("format"))))
				.executor(new commandTime())
				.build());
		subcommands.put(Arrays.asList("addore"), CommandSpec.builder()
				.description(Text.of("add Ore to a prison, by default, a prison is full of stone"))
				.permission("prisponge.addore")
				.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))),
				GenericArguments.onlyOne(GenericArguments.string(Text.of("orename"))),
				GenericArguments.onlyOne(GenericArguments.integer(Text.of("percentage"))))
				.executor(new commandAddOre())
				.build());
		subcommands.put(Arrays.asList("removeore"), CommandSpec.builder()
				.description(Text.of("remove Ore to a prison"))
				.permission("prisponge.removeore")
				.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))),
				GenericArguments.onlyOne(GenericArguments.string(Text.of("orename"))))
				.executor(new commandRemoveOre())
				.build());
		subcommands.put(Arrays.asList("reload"), CommandSpec.builder()
				.description(Text.of("reload config"))
				.permission("prisponge.reload")
				.executor(new commandReload())
				.build());
		subcommands.put(Arrays.asList("list"), CommandSpec.builder()
				.description(Text.of("list all prison"))
				.permission("prisponge.list")
				.executor(new commandList())
				.build());
		subcommands.put(Arrays.asList("listblocks"), CommandSpec.builder()
				.description(Text.of("list all block"))
				.permission("prisponge.listblocks")
				.executor(new commandListBlocks())
				.build());
		subcommands.put(Arrays.asList("clear"), CommandSpec.builder()
				.description(Text.of("clear a prison"))
				.permission("prisponge.clear")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandClear())
				.build());
				
		CommandSpec prispongecommand = CommandSpec.builder()
			    .description(Text.of("list all prisponge Command"))
			    .executor(new commandPrisponge())
			    .children(subcommands)
			    .build();
		Sponge.getCommandManager().register(this, prispongecommand, "prisponge");
		getLogger().info("Prisponge started.");
	}
	
	@Listener
	public void onServerStopping(GameStoppingServerEvent event)
	{
		getLogger().info("Prisponge stop.");
		try
		{
			getConfigManager().save(this.config);
		} catch (IOException e)
		{
			getLogger().error("Failed to save config file!", e);
		}
		getLogger().info("Prisponge stopped.");
	}
	
	private void setupconfig()
	{
        this.config.getNode("ConfigVersion").setValue(1);

        try
		{
			getConfigManager().save(this.config);
		} catch (IOException e) 
        {
            getLogger().error("Failed to save config file!", e);
        }
	}
	
	public void reload()
	{
		try
		{
			this.config = getConfigManager().load();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
