package com.histogram;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HistogramPluginTest
{
	public static void main(String[] args) throws Exception
	{
		try {
			ExternalPluginManager.loadBuiltin(HistogramPlugin.class);
			RuneLite.main(args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}