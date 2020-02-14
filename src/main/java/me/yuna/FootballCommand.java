package me.yuna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class FootballCommand extends Command {
	
	String shortName;
	String shortNameA[] = new String[100] ;

	
	public FootballCommand() {
		super.name = "football";
	}
	
	
	public String parse (String responseBody) {
		JSONArray infos = new JSONArray(responseBody);
		for(int i= 0; i < infos.length(); i++) {
			JSONObject info = infos.getJSONObject(i);
			shortName = info.getString("shortName");
			shortNameA[i] = shortName;
		}
		return null;
	}
	
	
	
	@Override
	protected void execute(CommandEvent event) {
		
		BufferedReader reader;
		URL url;
		String line;
		StringBuffer responseContent = new StringBuffer();
		
		try {
			url = new URL("https://api.football-data.org/v2/competitions/2017/teams");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("X-Auth-Token", "8c466d25b9f448829b6aa8bac57d9003");
			
			int status = connection.getResponseCode();
			
			if (status > 299) {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while ((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				
				reader.close();
			}
			
			parse(responseContent.toString());
			
			for (int i = 0 ; i < shortNameA.length; i++) {
				event.reply(shortNameA[i]);
			}
			
			connection.disconnect();
				
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
			
			
		
		
	}

}
