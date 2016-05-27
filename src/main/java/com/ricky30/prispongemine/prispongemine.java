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
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.extent.ExtentBufferFactory;

import com.google.inject.Inject;
import com.ricky30.prispongemine.commands.commandAddOre;
import com.ricky30.prispongemine.commands.commandAltar;
import com.ricky30.prispongemine.commands.commandAutorun;
import com.ricky30.prispongemine.commands.commandChangetool;
import com.ricky30.prispongemine.commands.commandClear;
import com.ricky30.prispongemine.commands.commandDefine;
import com.ricky30.prispongemine.commands.commandDelete;
import com.ricky30.prispongemine.commands.commandFill;
import com.ricky30.prispongemine.commands.commandList;
import com.ricky30.prispongemine.commands.commandPrisponge;
import com.ricky30.prispongemine.commands.commandRandomOre;
import com.ricky30.prispongemine.commands.commandReload;
import com.ricky30.prispongemine.commands.commandRemoveOre;
import com.ricky30.prispongemine.commands.commandRunall;
import com.ricky30.prispongemine.commands.commandSave;
import com.ricky30.prispongemine.commands.commandSet;
import com.ricky30.prispongemine.commands.commandSpawn;
import com.ricky30.prispongemine.commands.commandStart;
import com.ricky30.prispongemine.commands.commandStop;
import com.ricky30.prispongemine.commands.commandTime;
import com.ricky30.prispongemine.commands.commandUpdate;
import com.ricky30.prispongemine.commands.commandUpdateconfig;
import com.ricky30.prispongemine.events.interactionevents;
import com.ricky30.prispongemine.task.AutorunTask;
import com.ricky30.prispongemine.task.ClearTask;
import com.ricky30.prispongemine.task.FillTask;
import com.ricky30.prispongemine.task.Timers;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "com.ricky30.prispongemine", name = "prispongemine", version = "2.0.4")
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

	private final Scheduler scheduler = Sponge.getScheduler();
	private final Task.Builder taskBuilder = scheduler.createTaskBuilder();
	private Task task;
	private Task task_fill;
	private Task task_clear;
	public Task task_autorun = null;

	public Task gettask()
	{
		return this.task;
	}

	public Task gettask_fill()
	{
		return this.task_fill;
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
		catch (final IOException e)
		{
			getLogger().error("Couldn't create default configuration file!");
		}

		task = prispongemine.plugin.getTaskbuilder().execute(new Runnable()
		{
			@Override
			public void run()
			{
				Timers.run();
			}
		}).interval(1, TimeUnit.SECONDS).name("prispongemine").submit(this);

		Sponge.getEventManager().registerListeners(this, new interactionevents());

		final HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();

		subcommands.put(Arrays.asList("fill"), CommandSpec.builder()
				.description(Text.of("fill a mine"))
				.permission("prisponge.fill")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandFill())
				.build());
		subcommands.put(Arrays.asList("define"), CommandSpec.builder()
				.description(Text.of("allow use of stick to define a mine"))
				.permission("prisponge.define")
				.executor(new commandDefine())
				.build());
		subcommands.put(Arrays.asList("changetool"), CommandSpec.builder()
				.description(Text.of("change tool used to define a mine"))
				.permission("prisponge.changetool")
				.executor(new commandChangetool())
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
		subcommands.put(Arrays.asList("autorun"), CommandSpec.builder()
				.description(Text.of("change autorun value of a mine"))
				.permission("prisponge.autorun")
				.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))),
						GenericArguments.onlyOne(GenericArguments.bool(Text.of("autorun"))))
				.executor(new commandAutorun())
				.build());
		subcommands.put(Arrays.asList("random"), CommandSpec.builder()
				.description(Text.of("change random value of a mine"))
				.permission("prisponge.random")
				.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))),
						GenericArguments.onlyOne(GenericArguments.bool(Text.of("random"))))
				.executor(new commandRandomOre())
				.build());
		subcommands.put(Arrays.asList("addore"), CommandSpec.builder()
				.description(Text.of("add Ore to a mine, by default, a mine is full of stone"))
				.permission("prisponge.addore")
				.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))),
						GenericArguments.onlyOne(GenericArguments.doubleNum(Text.of("percentage"))))
				.executor(new commandAddOre())
				.build());
		subcommands.put(Arrays.asList("removeore"), CommandSpec.builder()
				.description(Text.of("remove Ore from a mine"))
				.permission("prisponge.removeore")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
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
		subcommands.put(Arrays.asList("altar"), CommandSpec.builder()
				.description(Text.of("define an altar"))
				.permission("prisponge.altar")
				.executor(new commandAltar())
				.build());
		subcommands.put(Arrays.asList("clear"), CommandSpec.builder()
				.description(Text.of("clear a mine"))
				.permission("prisponge.clear")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandClear())
				.build());
		subcommands.put(Arrays.asList("spawn"), CommandSpec.builder()
				.description(Text.of("Set the spawn for players inside the mine"))
				.permission("prisponge.spawn")
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
				.executor(new commandSpawn())
				.build());
		subcommands.put(Arrays.asList("startall"), CommandSpec.builder()
				.description(Text.of("start all mine"))
				.permission("prisponge.startall")
				.executor(new commandRunall())
				.build());
		subcommands.put(Arrays.asList("stopall"), CommandSpec.builder()
				.description(Text.of("stop all mine"))
				.permission("prisponge.stopall")
				.executor(new commandRunall())
				.build());
		subcommands.put(Arrays.asList("updateconfig"), CommandSpec.builder()
				.description(Text.of("update config file"))
				.permission("prisponge.updateconfig")
				.executor(new commandUpdateconfig())
				.build());
		subcommands.put(Arrays.asList("set"), CommandSpec.builder()
				.description(Text.of("change set of a mine"))
				.permission("prisponge.set")
				.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))),
						GenericArguments.onlyOne(GenericArguments.integer(Text.of("setnumber"))))
				.executor(new commandSet())
				.build());
		subcommands.put(Arrays.asList("autorundelay"), CommandSpec.builder()
				.description(Text.of("change autorun delay of a mine"))
				.permission("prisponge.delay")
				.arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("name")))),
						GenericArguments.onlyOne(GenericArguments.integer(Text.of("delay"))))
				.executor(new commandSet())
				.build());

		final CommandSpec prispongecommand = CommandSpec.builder()
				.description(Text.of("list all prispongemine Command"))
				.executor(new commandPrisponge())
				.children(subcommands)
				.build();
		Sponge.getCommandManager().register(this, prispongecommand, "prisponge");
		getLogger().info("Prispongemine started.");
	}

	//autorun
	@Listener
	public void onServerReady(GameStartedServerEvent event)
	{
		StartRunnningAll();
	}

	@Listener
	public void onServerStopping(GameStoppingServerEvent event)
	{
		getLogger().info("Prispongemine stop.");
		task.cancel();
		if (task_fill != null)
		{
			task_fill.cancel();
		}
		task = null;
		task_fill = null;
		save();
		getLogger().info("Prispongemine stopped.");
	}

	public void StartRunnningAll()
	{
		//runnning the autorun in a task make it run in a separate thread
		AutorunTask.Init();
		task_autorun = prispongemine.plugin.getTaskbuilder().execute(new Runnable()
		{
			@Override
			public void run()
			{
				AutorunTask.run();
			}
		}).interval(1, TimeUnit.SECONDS).name("Autoruntask").submit(this);
	}

	private void setupconfig()
	{
		this.config.getNode("ConfigVersion").setValue(4);
		this.config.getNode("tool").setValue(ItemTypes.STICK.getId());
		this.config.getNode("RemindSecondList").setValue("1, 2, 3, 4, 5, 10, 15, 30, 60, 90, 120, 180, 300");
		this.config.getNode("messageDump").setValue("NoMessages");
		save();
	}

	public void save()
	{
		try
		{
			getConfigManager().save(this.config);
		} catch (final IOException e) 
		{
			getLogger().error("Failed to save config file!", e);
		}
	}

	public void reload()
	{
		try
		{
			this.config = getConfigManager().load();
		} catch (final IOException e)
		{
			getLogger().error("Failed to load config file!", e);
		}
	}

	public String GetTool()
	{
		return this.config.getNode("tool").getString();
	}

	public void StartTaskFill()
	{
		if (task_fill == null)
		{
			task_fill = prispongemine.plugin.getTaskbuilder().execute(new Runnable()
			{
				@Override
				public void run()
				{
					FillTask.run();
				}
			}).interval(1, TimeUnit.SECONDS).name("Filltask").submit(this);
		}
	}

	public void StopTaskFill()
	{
		task_fill.cancel();
		task_fill = null;
	}

	public void StartTaskClear()
	{
		if (task_clear == null)
		{
			task_clear = prispongemine.plugin.getTaskbuilder().execute(new Runnable()
			{
				@Override
				public void run()
				{
					ClearTask.run();
				}
			}).interval(1, TimeUnit.SECONDS).name("Cleartask").submit(this);
		}
	}

	public void StopTaskClear()
	{
		task_clear.cancel();
		task_clear = null;
	}
}
