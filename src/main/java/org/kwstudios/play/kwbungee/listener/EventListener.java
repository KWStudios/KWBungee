package org.kwstudios.play.kwbungee.listener;

import java.util.Random;

import org.kwstudios.play.kwbungee.toolbox.ConstantHolder;
import org.kwstudios.play.kwbungee.toolbox.MotdListGetter;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventListener implements Listener {

	@EventHandler
	public void onPing(ProxyPingEvent event) {
		Random random = new Random();
		ServerPing serverPing = event.getResponse();
		try {
			if (MotdListGetter.getMotdList() != null) {
				int number = random.nextInt(MotdListGetter.getMotdList().size() - 1);
				String motd = MotdListGetter.getMotdList().get(number);
				if (motd != "") {
					serverPing.setDescription(
							ConstantHolder.MOTD_PREFIX + ChatColor.translateAlternateColorCodes('§', motd));
					event.setResponse(serverPing);
				}
			} else {

			}
		} catch (Exception e) {

		}
	}

	@EventHandler
	public void onPlayerConnected(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();
		BaseComponent header = new TextComponent(ChatColor.GREEN + player.getServer().getInfo().getName());
		BaseComponent footer = new TextComponent(
				ChatColor.YELLOW + "KWStudios" + ChatColor.GRAY + "." + "org" + ChatColor.RESET + "Network");
		player.setTabHeader(header, footer);
	}

}
