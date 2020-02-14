package me.yuna;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class FootballCommand extends Command {
	
	String shortName;

	
	public FootballCommand() {
		super.name = "football";
	}
	
		
	public String[] parseTeamNames (String responseBody) {
		
		JSONObject info = new JSONObject(responseBody);
		JSONArray teams = info.getJSONArray("teams");
			
		String[] shortNameA = new String[teams.length()];
		
		for (int i = 0; i < teams.length(); i++) {
			JSONObject team = teams.getJSONObject(i);
			shortNameA[i] = team.getString("shortName");
		}
		return shortNameA;
	}
	
	public Match[] parseMatches (String responseBody) {
		
		JSONObject info = new JSONObject(responseBody);
		JSONArray matchesJson = info.getJSONArray("matches");
			
		Match[] matches = new Match[matchesJson.length()];
		
		for (int i = 0; i < matchesJson.length(); i++) {
			JSONObject matchJson = matchesJson.getJSONObject(i);
			
			String winner;
			if (matchJson.getJSONObject("score").isNull("winner")){
				winner = "";
			} else {
				winner =matchJson.getJSONObject("score").getString("winner");
			}
			
			matches[i] = new Match(
					matchJson.getJSONObject("homeTeam").getString("name"),
					matchJson.getJSONObject("awayTeam").getString("name"),
					winner,
					matchJson.getString("utcDate"),
					matchJson.getInt("matchday"),
					matchJson.getJSONObject("season").getInt("currentMatchday")
				);
					
		}
		
		return matches;
	}
	
	public static class Match {
		
		String homeTeam;
		String awayTeam;
		String winner;
		String matchDate;
		int matchDay;
		int currentMatchDay;
		
		public Match(String homeTeam, String awayTeam, String winner, String matchDate, int matchDay, int currentMatchDay) {
		
			this.homeTeam = homeTeam;
			this.awayTeam = awayTeam;			
			this.winner = winner;
			this.matchDate = matchDate;
			this.matchDay = matchDay;
			this.currentMatchDay = currentMatchDay;
		}

		public String getHomeTeam() {
			return homeTeam;
		}

		public void setHomeTeam(String homeTeam) {
			this.homeTeam = homeTeam;
		}

		public String getAwayTeam() {
			return awayTeam;
		}

		public void setAwayTeam(String awayTeam) {
			this.awayTeam = awayTeam;
		}

		public String getWinner() {
			return winner;
		}

		public void setWinner(String winner) {
			this.winner = winner;
		}

		public String getMatchDate() {
			return matchDate;
		}

		public void setMatchDate(String matchDate) {
			this.matchDate = matchDate;
		}

		public int getMatchDay() {
			return matchDay;
		}

		public void setMatchDay(int matchDay) {
			this.matchDay = matchDay;
		}
		
		public int getCurrentMatchDay() {
			return currentMatchDay;
		}
		
		public void setCurrentMatchDay(int currentMatchDay) {
			this.currentMatchDay = currentMatchDay;
		}
		
		
	}
	
	
	
	@Override
	protected void execute(CommandEvent event) {
	
		
		String[] arguments = event.getArgs().split(" ");
				
		/**
		 * First argument "teams", command to print all teams in the competition
		 */
		
		if (arguments[0].equalsIgnoreCase("equipas")) {
			BufferedReader reader;
			URL url;
			String line;
			StringBuffer responseContent = new StringBuffer();
			
			//HTTPS request
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
				
				//Full reponse string builder	
				String[] shortNameA = parseTeamNames(responseContent.toString());
				String response = "Teams in the competion:";
				for (int i = 0 ; i < shortNameA.length; i++) {
					response += "\n" + shortNameA[i];
				}
				
				//Discord reponse buileder (embed)
				
				EmbedBuilder builder = new EmbedBuilder();
				
				builder.setTitle("Equipas na Primeira Liga");
				builder.setThumbnail("https://img.quizur.com/f/img5cd8683f2b45d9.11096988.png?lastEdited=1557686338");
				builder.setDescription(response);
				builder.setColor(Color.BLUE);
				event.reply(builder.build());
				
				connection.disconnect();
					
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		} else if (arguments[0].equalsIgnoreCase("jogos")) {
			BufferedReader reader;
			URL url;
			String line;
			StringBuffer responseContent = new StringBuffer();
			
			try {
				url = new URL("https://api.football-data.org/v2/competitions/2017/matches");
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
				
				Match[] matches = parseMatches(responseContent.toString());
				
				int currentMatchDay = matches[0].getCurrentMatchDay();
				String response = "Jogos nos proximos 2 dias: " + "\n";
				
				for (int i = 0; i < matches.length; i++) {
					
					while(matches[i].getMatchDay() >= currentMatchDay 
							&& matches[i].getMatchDay() <= (currentMatchDay + 0)) {
						StringBuilder matchDateB = new StringBuilder(matches[i].getMatchDate());
						matchDateB.setCharAt(10, ' ');
						matchDateB.setCharAt(19, ' ');
						String matchDate = matchDateB.toString();
						String homeTeam = matches[i].getHomeTeam();
						String awayTeam = matches[i].getAwayTeam();
						String winner = matches[i].getWinner();
						if (winner.equalsIgnoreCase("AWAY_TEAM"))
							winner = awayTeam;
						else if (winner.equalsIgnoreCase("HOME_TEAM"))
							winner = homeTeam;
						else if (winner.equalsIgnoreCase("DRAW"))
							winner = "Empate";
						
						response = response + "**" + matchDate + "**" + "\n" + homeTeam +" " + "**X**" + " " + awayTeam + " | " + "Winner: " + winner + "\n" + "\n" ;
						i++;
					}
					
				}
				
				event.reply(response);
				
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

}
