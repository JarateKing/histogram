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

	@ConfigItem(
			keyName = "inputlag",
			name = "Show Clientside Input",
			description = "Display the range from the clientside input event to it being fully processed.",
			section = panelSection,
			position = 3
	)
	default boolean showInputLag()
	{
		return false;
	}

	@ConfigItem(
			keyName = "inputlagalpha",
			name = "Clientside Input Alpha",
			description = "Alpha value of input range.",
			section = panelSection,
			position = 4
	)
	default int inputLagAlpha()
	{
		return 50;
	}

	@ConfigItem(
			keyName = "serverlag",
			name = "Show Serverside Processing",
			description = "Display the range from when the server receives the event to it being fully processed.",
			section = panelSection,
			position = 5
	)
	default boolean showServerLag()
	{
		return false;
	}

	@ConfigItem(
			keyName = "serverlagalpha",
			name = "Serverside Processing Alpha",
			description = "Alpha value of server processing range.",
			section = panelSection,
			position = 6
	)
	default int serverLagAlpha()
	{
		return 50;
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
			position = 1
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
			section = eventSection,
			position = 2
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
			section = eventSection,
			position = 2
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
			section = eventSection,
			position = 2
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
			section = eventSection,
			position = 2
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
			section = eventSection,
			position = 2
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
			section = eventSection,
			position = 2
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
			section = eventSection,
			position = 2
	)
	default Color prayerColor()
	{
		return new Color(0, 0, 255, 255);
	}

	@ConfigItem(
			keyName = "pingmax",
			name = "Ping Delay Maximum",
			description = "The maximum delay due to ping (in milliseconds). Doesn't interrupt pings, just limits the value returned.",
			section = advancedSection,
			position = 0
	)
	default int pingMax()
	{
		return 10000;
	}

	@ConfigItem(
			keyName = "pingcount",
			name = "Ping Every nth",
			description = "How many gameticks occur before re-pinging",
			section = advancedSection,
			position = 1
	)
	default int pingCount()
	{
		return 5;
	}

	@ConfigItem(
			keyName = "delaymoveconst",
			name = "Move Delay Constant",
			description = "The value to add to ping to delay move inputs (in milliseconds)",
			section = advancedSection,
			position = 2
	)
	default int moveConst()
	{
		return 60;
	}

	@ConfigItem(
			keyName = "delaymovemult",
			name = "Move Delay Mult",
			description = "The value to add to ping to delay move inputs, multiplied by playercount (x1000)",
			section = advancedSection,
			position = 2
	)
	default int moveMult()
	{
		return 60;
	}

	@ConfigItem(
			keyName = "delayprayerconst",
			name = "Prayer Delay Constant",
			description = "The value to add to ping to delay prayer inputs (in milliseconds)",
			section = advancedSection,
			position = 2
	)
	default int prayerConst()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "delayprayermult",
			name = "Prayer Delay Mult",
			description = "The value to add to ping to delay prayer inputs, multiplied by playercount (x1000)",
			section = advancedSection,
			position = 2
	)
	default int prayerMult()
	{
		return 60;
	}

	@ConfigItem(
			keyName = "delayattackconst",
			name = "Attack Delay Constant",
			description = "The value to add to ping to delay attack inputs (in milliseconds)",
			section = advancedSection,
			position = 2
	)
	default int attackConst()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "delayattackmult",
			name = "Attack Delay Mult",
			description = "The value to add to ping to delay attack inputs, multiplied by playercount (x1000)",
			section = advancedSection,
			position = 2
	)
	default int attackMult()
	{
		return 90;
	}

	@ConfigItem(
			keyName = "delayspecialattackconst",
			name = "Special Attack Delay Constant",
			description = "The value to add to ping to delay specialattack inputs (in milliseconds)",
			section = advancedSection,
			position = 2
	)
	default int specialattackConst()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "delayspecialattackmult",
			name = "Special Attack Delay Mult",
			description = "The value to add to ping to delay specialattack inputs, multiplied by playercount (x1000)",
			section = advancedSection,
			position = 2
	)
	default int specialattackMult()
	{
		return 90;
	}

	@ConfigItem(
			keyName = "delayeatconst",
			name = "Eat Delay Constant",
			description = "The value to add to ping to delay eat inputs (in milliseconds)",
			section = advancedSection,
			position = 2
	)
	default int eatConst()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "delayeatmult",
			name = "Eat Delay Mult",
			description = "The value to add to ping to delay eat inputs, multiplied by playercount (x1000)",
			section = advancedSection,
			position = 2
	)
	default int eatMult()
	{
		return 90;
	}

	@ConfigItem(
			keyName = "delayuseconst",
			name = "Use Delay Constant",
			description = "The value to add to ping to delay use inputs (in milliseconds)",
			section = advancedSection,
			position = 2
	)
	default int useConst()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "delayusemult",
			name = "Use Delay Mult",
			description = "The value to add to ping to delay use inputs, multiplied by playercount (x1000)",
			section = advancedSection,
			position = 2
	)
	default int useMult()
	{
		return 90;
	}

	@ConfigItem(
			keyName = "delayequipconst",
			name = "Equip Delay Constant",
			description = "The value to add to ping to delay equip inputs (in milliseconds)",
			section = advancedSection,
			position = 2
	)
	default int equipConst()
	{
		return 30;
	}

	@ConfigItem(
			keyName = "delayequipmult",
			name = "Equip Delay Mult",
			description = "The value to add to ping to delay equip inputs, multiplied by playercount (x1000)",
			section = advancedSection,
			position = 2
	)
	default int equipMult()
	{
		return 90;
	}
}
