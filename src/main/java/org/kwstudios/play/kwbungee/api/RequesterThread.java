package org.kwstudios.play.kwbungee.api;

import java.io.IOException;
import java.util.HashMap;

public class RequesterThread implements Runnable {

	private String url;
	private HashMap<String, String> headers;
	private HashMap<String, String> parameters;

	public RequesterThread(String url, HashMap<String, String> headers, HashMap<String, String> parameters) {
		this.url = url;
		this.headers = headers;
		this.parameters = parameters;
	}

	@Override
	public void run() {
		Requester requester = new Requester(url, headers, parameters);

		String s = null;
		try {
			while ((s = requester.getReader().readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
