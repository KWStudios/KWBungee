package org.kwstudios.play.kwbungee.api;

public class PlayerJSON {
	private String name;
	private String uuid;
	private long first_played;
	private long last_played;
	private boolean is_online;
	private boolean is_banned;

	public PlayerJSON(String name, String uuid, long first_played, long last_played, boolean is_online,
			boolean is_banned) {
		super();
		this.name = name;
		this.uuid = uuid;
		this.first_played = first_played;
		this.last_played = last_played;
		this.is_online = is_online;
		this.is_banned = is_banned;
	}

	public String getName() {
		return name;
	}

	public String getUuid() {
		return uuid;
	}

	public long getFirst_played() {
		return first_played;
	}

	public long getLast_played() {
		return last_played;
	}

	public boolean isIs_online() {
		return is_online;
	}

	public boolean isIs_banned() {
		return is_banned;
	}

}
