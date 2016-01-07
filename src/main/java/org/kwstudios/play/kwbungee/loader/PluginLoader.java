package org.kwstudios.play.kwbungee.loader;

import org.kwstudios.play.kwbungee.listener.EventListener;

import net.md_5.bungee.api.plugin.Plugin;

public class PluginLoader extends Plugin {

	@Override
	public void onEnable() {
		getProxy().getPluginManager().registerListener(this, new EventListener());
	}

}
