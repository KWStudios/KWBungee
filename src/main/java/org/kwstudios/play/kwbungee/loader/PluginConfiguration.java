package org.kwstudios.play.kwbungee.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class PluginConfiguration {

	private static boolean inited = false;
	private static File configFile;
	private static Configuration configuration;

	private static void initSignConfiguration() {
		if (inited)
			return;
		else
			inited = true;

		if (!PluginLoader.getInstance().getDataFolder().exists())
			PluginLoader.getInstance().getDataFolder().mkdir();

		configFile = new File(PluginLoader.getInstance().getDataFolder(), "config.yml");

		if (!configFile.exists()) {
			try (InputStream in = PluginLoader.getInstance().getResourceAsStream("config.yml")) {
				Files.copy(in, configFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveConfiguration() {
		if (!inited)
			initSignConfiguration();

		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void reloadConfiguration() {
		if (!inited)
			initSignConfiguration();

		try {
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Configuration getConfiguration() {
		if(!inited)
			initSignConfiguration();

		return configuration;
	}

}
