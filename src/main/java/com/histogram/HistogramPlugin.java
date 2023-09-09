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
	name = "Histogram"
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

	private int ping = -1;
	private int checksTilPing = 0;

	private static final int HOP_GAMESTATE = 45;

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

		if (config.useIdealTicks()) {
			histogramOverlay.addEvent(EventType.IDEAL_TICK, 0.600f);
		}

		pingThreads.schedule(this::updatePing, 0, TimeUnit.SECONDS);
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked e) {
		String menuOption = e.getMenuOption();

		if (menuOption.equals("Wield") || menuOption.equals("Wear") || menuOption.equals("Remove") || menuOption.equals("Hold")) {
			histogramOverlay.addEvent(EventType.EQUIP, getInputDelay());
		}

		if (menuOption.equals("Eat") || menuOption.equals("Drink")) {
			histogramOverlay.addEvent(EventType.EAT, getInputDelay());
		}

		if (menuOption.equals("Walk here")) {
			histogramOverlay.addEvent(EventType.MOVE, getInputDelay());
		}

		if (menuOption.equals("Use")) {
			if (removeFormatting(e.getMenuTarget()).equals("Special Attack")) {
				histogramOverlay.addEvent(EventType.SPECIAL_ATTACK, getInputDelay());
			}
			else {
				histogramOverlay.addEvent(EventType.USE, getInputDelay());
			}
		}

		if (removeFormatting(menuOption).equals("Use Special Attack")) {
			histogramOverlay.addEvent(EventType.SPECIAL_ATTACK, getInputDelay());
		}

		if (menuOption.equals("Attack")) {
			histogramOverlay.addEvent(EventType.ATTACK, getInputDelay());
		}

		if (menuOption.equals("Activate") || menuOption.equals("Deactivate")) {
			histogramOverlay.addEvent(EventType.PRAYER, getInputDelay());
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState().getState() == HOP_GAMESTATE)
		{
			checksTilPing = 0;
		}
	}

	@Provides
	HistogramConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HistogramConfig.class);
	}

	private float getInputDelay()
	{
		float delay = (ping / 1000f) + (config.pingConstant() / 1000f);
		return Math.min(delay, (config.pingMax() / 1000f));
	}

	private void updatePing()
	{
		if (checksTilPing == 0)
		{
			int currentping = sendPing();

			if (currentping != -1)
			{
				ping = currentping;
				checksTilPing = config.pingCount() - 1;
			}
		}
		else
		{
			checksTilPing--;
		}
	}

	private int sendPing()
	{
		return Ping.ping(worldService.getWorlds().findWorld(client.getWorld()));
	}

	private String removeFormatting(String raw)
	{
		return raw.replaceAll("<[^>]*>", "");
	}
}
