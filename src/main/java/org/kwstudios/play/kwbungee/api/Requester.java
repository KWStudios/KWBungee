package org.kwstudios.play.kwbungee.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

public class Requester {

	private final String USER_AGENT = "Mozilla/5.0";

	private String url;
	private HashMap<String, String> headers;
	private HashMap<String, String> parameters;

	private BufferedReader reader = null;

	public Requester(String url, HashMap<String, String> headers, HashMap<String, String> parameters) {
		this.url = url;
		this.headers = headers;
		this.parameters = parameters;
		try {
			sendPost();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BufferedReader getReader() {
		return reader;
	}

	private void sendPost() throws Exception {
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		for (Entry<String, String> header : headers.entrySet()) {
			con.setRequestProperty(header.getKey(), header.getValue());
		}

		con.setDoInput(true);
		con.setDoOutput(true);

		OutputStream os = con.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		writer.write(getPostDataString(parameters));

		writer.flush();
		writer.close();
		os.close();

		int responseCode = con.getResponseCode();
		// System.out.println("\nSending 'POST' request to URL : " + url);
		// System.out.println("Post parameters : " + urlParameters);
		// System.out.println("Response Code : " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} else {
			System.out.println("Something unexpected went wrong on the given Server! Response code: " + responseCode);
		}

	}

	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}

}
