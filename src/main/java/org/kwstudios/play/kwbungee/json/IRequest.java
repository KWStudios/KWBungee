package org.kwstudios.play.kwbungee.json;

import org.kwstudios.play.kwbungee.enums.BungeeMessageAction;

public interface IRequest {

	public BungeeMessageAction getAction();

	public boolean isRequest();

}
