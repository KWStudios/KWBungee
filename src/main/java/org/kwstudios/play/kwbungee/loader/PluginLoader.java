package org.kwstudios.play.kwbungee.loader;

import java.util.HashMap;

import org.kwstudios.play.kwbungee.commands.LobbyCommand;
import org.kwstudios.play.kwbungee.listener.EventListener;
import org.kwstudios.play.kwbungee.toolbox.MotdListGetter;

import net.md_5.bungee.api.plugin.Plugin;

public class PluginLoader extends Plugin {

	private static PluginLoader instance = null;
	private static HashMap<String, String> headers = new HashMap<String, String>();
	private static HashMap<String, String> parameters = new HashMap<String, String>();

	@Override
	public void onEnable() {
		super.onEnable();

		PluginLoader.instance = this;

		MotdListGetter.getMotdsFromFile();

		getProxy().getPluginManager().registerListener(this, new EventListener());

		getProxy().getPluginManager().registerCommand(this, new LobbyCommand());
	}

	public void setupApiHashMaps() {
		if (PluginConfiguration.getConfiguration().getString("settings.authorization") != null) {
			String authorization = PluginConfiguration.getConfiguration().getString("settings.authorization");
			headers.put("Authorization-Code", authorization);
		}
	}

	public static HashMap<String, String> getHeaders() {
		return headers;
	}

	public static HashMap<String, String> getParameters() {
		return parameters;
	}

	public static PluginLoader getInstance() {
		return instance;
	}

}
