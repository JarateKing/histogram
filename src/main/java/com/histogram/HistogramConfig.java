package com.histogram;

import java.awt.*;

import net.runelite.client.config.*;

@ConfigGroup("histogram")
public interface HistogramConfig extends Config
{
	@ConfigSection(
			name = "Panel",
			description = "Panel Settings",
			position = 0,
			closedByDefault = false
	)
	String panelSection = "Panel";

	@ConfigSection(
			name = "Events",
			description = "Events Settings",
			position = 1,
			closedByDefault = false
	)
	String eventSection = "Events";

	@ConfigSection(
			name = "Advanced",
			description = "Advanced Settings",
			position = 2,
			closedByDefault = true
	)
	String advancedSection = "Advanced";

	@ConfigItem(
			keyName = "panelsize",
			name = "Panel size",
			description = "Dimensions of the entire histogram panel (horizontal x vertical)",
			section = panelSection
	)
	default Dimension panelSize()
	{
		return new Dimension(100, 10);
	}

	@Alpha
	@ConfigItem(
			keyName = "bgcolor",
			name = "Background Color",
			description = "Color of the background panel",
			section = panelSection
	)
	default Color bgColor()
	{
		return new Color(0, 0, 0, 175);
	}

	@ConfigItem(
			keyName = "duration",
			name = "Duration",
			description = "How long (in milliseconds) it takes for events to traverse the histogram",
			section = panelSection
	)
	default int durationMS()
	{
		return 1800;
	}

	@ConfigItem(
			keyName = "linewidth",
			name = "Line Width",
			description = "How wide (in pixels) should each tick/input marker be",
			section = panelSection
	)
	default int linewidth()
	{
		return 2;
	}

	@ConfigItem(
			keyName = "antialiasing",
			name = "Anti-aliasing",
			description = "Should event indicators be anti-aliased",
			section = panelSection
	)
	default boolean antialiasing()
	{
		return true;
	}

	@ConfigItem(
			keyName = "idealticks",
			name = "Show Ideal Tickrate",
			description = "Should we display ideal 0.6s interval ticks as well as actual ticks",
			section = panelSection
	)
	default boolean useIdealTicks()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
			keyName = "tick",
			name = "Tick Color",
			description = "Color of the gametick event",
			section = eventSection,
			position = 0
	)
	default Color tickColor()
	{
		return new Color(0, 255, 255, 255);
	}

	@Alpha
	@ConfigItem(
			keyName = "idealtick",
			name = "Ideal Tick Color",
			description = "Color of the simulated ideal gametick timing",
			section = eventSection,
			position = 0
	)
	default Color idealTickColor()
	{
		return new Color(127, 255, 255, 127);
	}

	@Alpha
	@ConfigItem(
			keyName = "equip",
			name = "Equip Color",
			description = "Color of the equip event",
			section = eventSection
	)
	default Color equipColor()
	{
		return new Color(255, 255, 0, 255);
	}

	@Alpha
	@ConfigItem(
			keyName = "eat",
			name = "Eat Color",
			description = "Color of the eat event",
			section = eventSection
	)
	default Color eatColor()
	{
		return new Color(0, 255, 0, 255);
	}

	@Alpha
	@ConfigItem(
			keyName = "move",
			name = "Move Color",
			description = "Color of the walk/run events",
			section = eventSection
	)
	default Color moveColor()
	{
		return new Color(255, 0, 255, 255);
	}

	@Alpha
	@ConfigItem(
			keyName = "use",
			name = "Use Color",
			description = "Color of the use events",
			section = eventSection
	)
	default Color useColor()
	{
		return new Color(255, 255, 255, 255);
	}

	@Alpha
	@ConfigItem(
			keyName = "attack",
			name = "Attack Color",
			description = "Color of the attack events",
			section = eventSection
	)
	default Color attackColor()
	{
		return new Color(255, 0, 0, 255);
	}

	@Alpha
	@ConfigItem(
			keyName = "spec",
			name = "Special Attack Color",
			description = "Color of special attack events",
			section = eventSection
	)
	default Color specColor()
	{
		return new Color(255, 127, 0, 255);
	}

	@Alpha
	@ConfigItem(
			keyName = "prayer",
			name = "Prayer Color",
			description = "Color of the prayer events",
			section = eventSection
	)
	default Color prayerColor()
	{
		return new Color(0, 0, 255, 255);
	}

	@ConfigItem(
			keyName = "pingconstant",
			name = "Ping Delay Constant",
			description = "The value to add to ping to delay input ticks (in milliseconds)",
			section = advancedSection
	)
	default int pingConstant()
	{
		return 60;
	}

	@ConfigItem(
			keyName = "pingmax",
			name = "Ping Delay Maximum",
			description = "The maximum delay due to ping (in milliseconds). Doesn't interrupt pings, just limits the value returned.",
			section = advancedSection
	)
	default int pingMax()
	{
		return 10000;
	}

	@ConfigItem(
			keyName = "pingcount",
			name = "Ping Every nth",
			description = "How many gameticks occur before re-pinging",
			section = advancedSection
	)
	default int pingCount()
	{
		return 5;
	}
}
