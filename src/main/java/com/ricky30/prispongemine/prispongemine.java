package com.ricky30.prispongemine;

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
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.extent.ExtentBufferFactory;

import com.google.inject.Inject;
import com.ricky30.prispongemine.commands.*;
import com.ricky30.prispongemine.config.ManageConfig;
import com.ricky30.prispongemine.events.interactionevents;
import com.ricky30.prispongemine.task.*;
import ninja.leaping.configurate.ConfigurationNode;

@Plugin(id = "com.ricky30.prispongemine", name = "prispongemine", version = "3.0.1")
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

	private final Scheduler scheduler = Sponge.getScheduler();
	private final Task.Builder taskBuilder = scheduler.createTaskBuilder();
	private Task task;
	private Task task_fill;
	private Task task_clear;
	private Task task_autorun = null;

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
		ManageConfig.Init(defaultConfig);
		this.config = ManageConfig.getConfig();

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
				.description(Text.of("set refill time of a mine. There is few warnings before a mine refill"))
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
		subcommands.put(Arrays.asList("warning"), CommandSpec.builder()
				.description(Text.of("show or hide mine warnings"))
				.permission("prisponge.warning")
				.arguments(GenericArguments.onlyOne(GenericArguments.bool(Text.of("show"))))
				.executor(new commandShowWarnings())
				.build());
		subcommands.put(Arrays.asList("random"), CommandSpec.builder()
				.description(Text.of("allow mine to use random ore between added ore for this mine"))
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
		final boolean OK = this.config.getNode("ConfigVersion").getValue().equals(5);
		if (!OK)
		{
			getLogger().warn("Prispongemine config must be updated");
			getLogger().warn("Please use /prisponge updateconfig");
		}
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
		ManageConfig.Save();
		getLogger().info("Prispongemine stopped.");
	}

	public String GetTool()
	{
		return this.config.getNode("tool").getString();
	}

	public void StartRunnningAll()
	{
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

	public void StopAutorun()
	{
		task_autorun.cancel();
		task_autorun = null;
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
