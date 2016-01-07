package org.kwstudios.play.kwbungee.toolbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.kwstudios.play.kwbungee.loader.PluginLoader;

public class MotdListGetter {

	private static List<String> motdList = new ArrayList<String>();

	public static void getMotdsFromFile() {
		File motdsFile = new File(PluginLoader.getInstance().getDataFolder().getPath() + File.separator + "messages"
				+ File.separator + "motds.kw");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(motdsFile), "UTF-8"))) {
			for (String line; (line = br.readLine()) != null;) {
				// process the line.
				motdList.add(line.trim());
			}
		} catch (Exception e) {
			motdList = null;
			System.out.println("No motds.kw File was created yet. Skipping that...");
		}
	}

	public static List<String> getMotdList() {
		return motdList;
	}

}
