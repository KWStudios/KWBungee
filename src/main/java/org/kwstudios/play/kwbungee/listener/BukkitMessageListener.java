package org.kwstudios.play.kwbungee.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.kwstudios.play.kwbungee.enums.BungeeMessageAction;
import org.kwstudios.play.kwbungee.json.BungeeRequest;
import org.kwstudios.play.kwbungee.json.FriendsRequest;
import org.kwstudios.play.kwbungee.json.PartyRequest;
import org.kwstudios.play.kwbungee.loader.PluginLoader;
import org.kwstudios.play.kwbungee.toolbox.ConstantHolder;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.simonsator.partyandfriends.api.FriendsAPI;
import de.simonsator.partyandfriends.api.PartyAPI;
import de.simonsator.partyandfriends.party.PlayerParty;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BukkitMessageListener implements Listener {

	private void sendMessage(String message, ServerInfo server) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.sendData(ConstantHolder.KW_CHANNEL_NAME, stream.toByteArray());
	}

	@EventHandler
	public void onPluginMessage(PluginMessageEvent event) {
		if (!event.getTag().equals(ConstantHolder.KW_CHANNEL_NAME)) {
			return;
		}
		if (!(event.getSender() instanceof Server)) {
			return;
		}

		Server server = (Server) event.getSender();
		ByteArrayInputStream stream = new ByteArrayInputStream(event.getData());
		DataInputStream input = new DataInputStream(stream);

		try {
			parseMessage(input.readUTF(), server);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseMessage(String message, Server server) {
		Gson gson = new Gson();
		BungeeRequest request = null;
		try {
			request = gson.fromJson(message, BungeeRequest.class);
		} catch (JsonSyntaxException e) {
			throw new JsonSyntaxException("The message received was corrupt. It should be JSON Syntax");
		}
		if (request == null) {
			return;
		}
		if (!request.isRequest()) {
			return;
		}

		for (BungeeMessageAction action : request.getActions()) {
			if (action == BungeeMessageAction.PARTY) {
				PartyRequest partyRequest = request.getPartyRequest();
				ProxiedPlayer player = PluginLoader.getInstance().getProxy()
						.getPlayer(UUID.fromString(partyRequest.getUuid()));
				PlayerParty party = PartyAPI.getParty(player);
				if (party == null) {
					// TODO Send message back with empty player Array
					PartyRequest response = new PartyRequest(partyRequest.getPlayer(), partyRequest.getUuid(),
							new String[] {}, new String[] {}, false);
					String responseJson = gson.toJson(response);
					sendMessage(responseJson, server.getInfo());
					return;
				}

				boolean isLeader = party.isleader(player);

				String players[] = new String[party.getAllPlayersInParty().size()];
				String uuids[] = new String[party.getAllPlayersInParty().size()];
				for (int i = 0; i < party.getAllPlayersInParty().size(); i++) {
					players[i] = party.getAllPlayersInParty().get(i).getName();
					uuids[i] = party.getAllPlayersInParty().get(i).getUniqueId().toString();
				}

				PartyRequest response = new PartyRequest(partyRequest.getPlayer(), partyRequest.getUuid(), players,
						uuids, isLeader);
				BungeeRequest bungeeResponse = new BungeeRequest(response, null);

				String responseJson = gson.toJson(bungeeResponse);

				sendMessage(responseJson, server.getInfo());
			} else if (action == BungeeMessageAction.FRIENDS) {
				FriendsRequest friendsRequest = request.getFriendsRequest();

				FriendsRequest response = new FriendsRequest(friendsRequest.getPlayer(),
						FriendsAPI.getFriends(friendsRequest.getPlayer()));
				BungeeRequest bungeeResponse = new BungeeRequest(null, response);

				String responseJson = gson.toJson(bungeeResponse);

				sendMessage(responseJson, server.getInfo());
			}
		}

	}

}
