package org.kwstudios.play.kwbungee.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kwstudios.play.kwbungee.enums.BungeeMessageAction;
import org.kwstudios.play.kwbungee.json.BungeeRequest;
import org.kwstudios.play.kwbungee.json.FriendsRequest;
import org.kwstudios.play.kwbungee.json.PartyRequest;
import org.kwstudios.play.kwbungee.loader.PluginLoader;
import org.kwstudios.play.kwbungee.toolbox.ConstantHolder;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PartyAPI;
import de.simonsator.partyandfriends.api.party.PlayerParty;
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
				OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(player);
				PlayerParty party = PartyAPI.getParty(pafPlayer);
				if (party == null) {
					// TODO Send message back with empty player Array
					PartyRequest response = new PartyRequest(partyRequest.getPlayer(), partyRequest.getUuid(),
							new String[] {}, new String[] {}, false);
					BungeeRequest bungeeResponse = new BungeeRequest(response, null);
					String responseJson = gson.toJson(bungeeResponse);
					sendMessage(responseJson, server.getInfo());
					return;
				}

				boolean isLeader = party.isLeader(pafPlayer);

				String players[] = new String[party.getPlayers().size()];
				String uuids[] = new String[party.getPlayers().size()];
				for (int i = 0; i < party.getPlayers().size(); i++) {
					players[i] = party.getPlayers().get(i).getName();
					uuids[i] = party.getPlayers().get(i).getUniqueId().toString();
				}

				PartyRequest response = new PartyRequest(partyRequest.getPlayer(), partyRequest.getUuid(), players,
						uuids, isLeader);
				BungeeRequest bungeeResponse = new BungeeRequest(response, null);

				String responseJson = gson.toJson(bungeeResponse);

				sendMessage(responseJson, server.getInfo());
			} else if (action == BungeeMessageAction.FRIENDS) {
				Logger.getLogger("abbb").log(Level.INFO, "1. FRIENDS");
				FriendsRequest friendsRequest = request.getFriendsRequest();

				Logger.getLogger("abbb").log(Level.INFO,"2. REQUEST");
				ProxiedPlayer player = PluginLoader.getInstance().getProxy().getPlayer(friendsRequest.getPlayer());
				Logger.getLogger("abbb").log(Level.INFO,"3. ProxiedPlayer");
				OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(player);
				Logger.getLogger("abbb").log(Level.INFO,"4. PafPlayer");
				
				List<PAFPlayer> friends = pafPlayer.getFriends();
				Logger.getLogger("abbb").log(Level.INFO,"5. Friends " + friends.size());
				String[] friendNames = new String[friends.size()];
				Logger.getLogger("abbb").log(Level.INFO,"6. FriendNames");
				for (int i = 0; i < friends.size(); i++) {
					friendNames[i] = friends.get(i).getName();
				}
				Logger.getLogger("abbb").log(Level.INFO,"7. for done");
				
				FriendsRequest response = new FriendsRequest(friendsRequest.getPlayer(), friendNames);
				Logger.getLogger("abbb").log(Level.INFO,"8. response");
				BungeeRequest bungeeResponse = new BungeeRequest(null, response);
				Logger.getLogger("abbb").log(Level.INFO,"9. bungeeresponse");

				String responseJson = gson.toJson(bungeeResponse);
				Logger.getLogger("abbb").log(Level.INFO,"10. json");

				sendMessage(responseJson, server.getInfo());
				Logger.getLogger("abbb").log(Level.INFO,"11. sent\ndone.");
			}
		}

	}

}
