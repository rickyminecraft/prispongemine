package com.ricky30.prispongemine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
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

@Plugin(id = "com.ricky30.prispongemine", name = "prispongemine", version = "1.0.2")
public class prispongemine
{
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
		getLogger().info("Prispongemine start.");
		EXTENT_BUFFER_FACTORY = Sponge.getRegistry().getExtentBufferFactory();
		plugin = this;
		try
		{
			reload();
			if (!Files.exists(getDefaultConfig())) 
			{

				Files.createFile(getDefaultConfig());
				setupconfig();
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
		}).interval(1, TimeUnit.SECONDS).name("prispongemine").submit(this);
		
		Sponge.getEventManager().registerListeners(this, new interactionevents());
		
		HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();
		
		subcommands.put(Arrays.asList("fill"), CommandSpec.builder()
				.description(Text.of("fill a mine"))
				.permission("prisponge.fill")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandFill())
				.build());
		subcommands.put(Arrays.asList("define"), CommandSpec.builder()
				.description(Text.of("active use of stick to define a mine"))
				.permission("prisponge.define")
				.executor(new commandDefine())
				.build());
		subcommands.put(Arrays.asList("save"), CommandSpec.builder()
				.description(Text.of("save a defined mine"))
				.permission("prisponge.save")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandSave())
				.build());
		subcommands.put(Arrays.asList("update"), CommandSpec.builder()
				.description(Text.of("update an already defined mine"))
				.permission("prisponge.update")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandUpdate())
				.build());
		subcommands.put(Arrays.asList("delete"), CommandSpec.builder()
				.description(Text.of("delete a mine"))
				.permission("prisponge.delete")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandDelete())
				.build());
		subcommands.put(Arrays.asList("start"), CommandSpec.builder()
				.description(Text.of("start autorefill of a mine"))
				.permission("prisponge.start")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandStart())
				.build());
		subcommands.put(Arrays.asList("stop"), CommandSpec.builder()
				.description(Text.of("stop autorefill of a mine"))
				.permission("prisponge.stop")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandStop())
				.build());
		subcommands.put(Arrays.asList("time"), CommandSpec.builder()
				.description(Text.of("set refill time of a mine there is few warnings before a mine refill"))
				.permission("prisponge.time")
				.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))),
				GenericArguments.onlyOne(GenericArguments.integer(Text.of("duration"))),
				GenericArguments.onlyOne(GenericArguments.string(Text.of("format"))))
				.executor(new commandTime())
				.build());
		subcommands.put(Arrays.asList("addore"), CommandSpec.builder()
				.description(Text.of("add Ore to a mine, by default, a mine is full of stone"))
				.permission("prisponge.addore")
				.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))),
				GenericArguments.onlyOne(GenericArguments.string(Text.of("orename"))),
				GenericArguments.onlyOne(GenericArguments.integer(Text.of("percentage"))))
				.executor(new commandAddOre())
				.build());
		subcommands.put(Arrays.asList("removeore"), CommandSpec.builder()
				.description(Text.of("remove Ore from a mine"))
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
				.description(Text.of("list all mine"))
				.permission("prisponge.list")
				.executor(new commandList())
				.build());
		subcommands.put(Arrays.asList("listblocks"), CommandSpec.builder()
				.description(Text.of("list all block"))
				.permission("prisponge.listblocks")
				.executor(new commandListBlocks())
				.build());
		subcommands.put(Arrays.asList("clear"), CommandSpec.builder()
				.description(Text.of("clear a mine"))
				.permission("prisponge.clear")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandClear())
				.build());
				
		CommandSpec prispongecommand = CommandSpec.builder()
			    .description(Text.of("list all prispongemine Command"))
			    .executor(new commandPrisponge())
			    .children(subcommands)
			    .build();
		Sponge.getCommandManager().register(this, prispongecommand, "prisponge");
		getLogger().info("Prispongemine started.");
	}
	
	@Listener
	public void onServerStopping(GameStoppingServerEvent event)
	{
		getLogger().info("Prispongemine stop.");
		save();
		getLogger().info("Prispongemine stopped.");
	}
	
	private void setupconfig()
	{
        this.config.getNode("ConfigVersion").setValue(1);
        save();
	}
	
	public void save()
	{
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