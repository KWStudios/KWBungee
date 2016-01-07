package org.kwstudios.play.kwbungee.loader;

import org.kwstudios.play.kwbungee.listener.EventListener;
import org.kwstudios.play.kwbungee.toolbox.MotdListGetter;

import net.md_5.bungee.api.plugin.Plugin;

public class PluginLoader extends Plugin {

	private static PluginLoader instance = null;

	@Override
	public void onEnable() {
		super.onEnable();

		PluginLoader.instance = this;

		MotdListGetter.getMotdsFromFile();

		getProxy().getPluginManager().registerListener(this, new EventListener());
	}

	public static PluginLoader getInstance() {
		return instance;
	}

}
