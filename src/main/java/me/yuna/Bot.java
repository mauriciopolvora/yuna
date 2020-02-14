package me.yuna;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.security.auth.login.LoginException;

import org.json.JSONObject;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;


public class Bot {
	
	
	
	private JSONObject tokens;

	public Bot() throws LoginException, IOException {
		
		String file = new String(Files.readAllBytes(new File("tokens.tkn").toPath()));
		this.tokens = new JSONObject(file);
	
		final JDA jda = new JDABuilder(AccountType.BOT)
				.setToken(tokens.getString("discordtkn")).build();
				
		CommandClientBuilder builder = new CommandClientBuilder();
			builder.setPrefix("?");
			builder.setActivity(Activity.watching("you"));
			builder.setOwnerId("195992711982088192");
			
		
		CommandClient client = builder.build();

			client.addCommand(new MemeCommand());
			client.addCommand(new FootballCommand());
		
		jda.addEventListener(client);
		
		
	}
	
public static void main(String[] args) throws LoginException, IOException {
		
		new Bot();
		System.out.println("Bot enabled");
	}
}
