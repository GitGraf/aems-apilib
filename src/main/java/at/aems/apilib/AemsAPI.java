package at.aems.apilib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Provides static convenience methods to interact with the API.
 * @author Niggi
 */
public class AemsAPI {
	
	public static JsonElement call(String url, String httpMethod, String body) throws IOException {
		String rawResult = callRaw(url, httpMethod, body);
		try {
			return new JsonParser().parse(rawResult);
		} catch(JsonParseException ex) {
			return new JsonPrimitive("Error: Response is not valid JSON -> " + rawResult);
		}
	}
	
	public static String callRaw(String url, String httpMethod, String body) throws IOException {
		HttpURLConnection connection;
		URL apiUrl = new URL(url);
		connection = (HttpURLConnection) apiUrl.openConnection();
		
		connection.setRequestMethod(httpMethod);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length", Integer.toString(body.length()));
		connection.setDoOutput(true);
		connection.getOutputStream().write(body.getBytes("UTF-8"));
		
		connection.connect();
		
		String rawResult = readDataFromStream(connection.getInputStream());
		return rawResult;
	}
	
	private static String readDataFromStream(InputStream stream) throws IOException {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
	    StringBuffer buffer = new StringBuffer();
	    String line;
	    while((line = reader.readLine()) != null) {
	      buffer.append(line);
	    }
	    return buffer.toString();
	  }

}
