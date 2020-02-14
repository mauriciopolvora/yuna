package me.yuna;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;


public class Bot {
	
	

	
	public Bot() throws LoginException {
		
		final JDA jda = new JDABuilder(AccountType.BOT)
				.setToken("NjA1NDg0OTU3NDI2NjQ3MTQ3.XkPJlg.St9eh_HId4AHhBf118MshNIBN40").build();
				
		CommandClientBuilder builder = new CommandClientBuilder();
			builder.setPrefix("?");
			builder.setActivity(Activity.watching("you"));
			builder.setOwnerId("195992711982088192");
			
		
		CommandClient client = builder.build();

			client.addCommand(new MemeCommand());
			client.addCommand(new FootballCommand());
		
		jda.addEventListener(client);
		
		
	}
	
public static void main(String[] args) throws LoginException {
		
		new Bot();
		System.out.println("Bot enabled");
	}
}
