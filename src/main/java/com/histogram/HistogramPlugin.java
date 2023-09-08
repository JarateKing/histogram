package com.histogram;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.worldhopper.ping.Ping;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.api.events.*;
import net.runelite.client.game.WorldService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@PluginDescriptor(
	name = "Example"
)
public class HistogramPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private HistogramConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private WorldService worldService;

	private HistogramOverlay histogramOverlay;
	private ScheduledExecutorService pingThreads;

	@Override
	protected void startUp() throws Exception
	{
		histogramOverlay = new HistogramOverlay(config);
		overlayManager.add(histogramOverlay);

		pingThreads = Executors.newScheduledThreadPool(20);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(histogramOverlay);
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		histogramOverlay.addEvent(EventType.TICK);
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked e) {
		String menuOption = e.getMenuOption();

		if (menuOption.equals("Wield") || menuOption.equals("Wear") || menuOption.equals("Remove") || menuOption.equals("Hold")) {
			pingThreads.schedule(() -> { histogramOverlay.addEvent(EventType.EQUIP, getInputDelay()); }, 0, TimeUnit.SECONDS);
		}

		if (menuOption.equals("Eat") || menuOption.equals("Drink")) {
			pingThreads.schedule(() -> { histogramOverlay.addEvent(EventType.EAT, getInputDelay()); }, 0, TimeUnit.SECONDS);
		}

		if (menuOption.equals("Walk here")) {
			pingThreads.schedule(() -> { histogramOverlay.addEvent(EventType.MOVE, getInputDelay()); }, 0, TimeUnit.SECONDS);
		}

		if (menuOption.equals("Use")) {
			if (removeFormatting(e.getMenuTarget()).equals("Special Attack")) {
				pingThreads.schedule(() -> { histogramOverlay.addEvent(EventType.SPECIAL_ATTACK, getInputDelay()); }, 0, TimeUnit.SECONDS);
			}
			else {
				pingThreads.schedule(() -> { histogramOverlay.addEvent(EventType.USE, getInputDelay()); }, 0, TimeUnit.SECONDS);
			}
		}

		if (removeFormatting(menuOption).equals("Use Special Attack")) {
			pingThreads.schedule(() -> { histogramOverlay.addEvent(EventType.SPECIAL_ATTACK, getInputDelay()); }, 0, TimeUnit.SECONDS);
		}

		if (menuOption.equals("Attack")) {
			pingThreads.schedule(() -> { histogramOverlay.addEvent(EventType.ATTACK, getInputDelay()); }, 0, TimeUnit.SECONDS);
		}

		if (menuOption.equals("Activate") || menuOption.equals("Deactivate")) {
			pingThreads.schedule(() -> { histogramOverlay.addEvent(EventType.PRAYER, getInputDelay()); }, 0, TimeUnit.SECONDS);
		}
	}

	@Provides
	HistogramConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HistogramConfig.class);
	}

	private float getInputDelay()
	{
		int ping = Ping.ping(worldService.getWorlds().findWorld(client.getWorld()));
		float delay = (ping / 1000f) * (config.pingCoefficient() / 1000f) + (config.pingConstant() / 1000f);
		return Math.min(delay, (config.pingMax() / 1000f));
	}

	private String removeFormatting(String raw)
	{
		return raw.replaceAll("<[^>]*>", "");
	}
}