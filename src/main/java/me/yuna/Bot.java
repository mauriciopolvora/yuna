package me.yuna;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.security.auth.login.LoginException;

import org.json.JSONObject;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import me.yuna.commands.Ball8Command;
import me.yuna.commands.FootballCommand;
import me.yuna.commands.HelpCommand;
import me.yuna.commands.MemeCommand;
import me.yuna.commands.music.JoinVCCommand;
import me.yuna.commands.music.LeaveVCCommand;
import me.yuna.commands.music.NowPlayingCommand;
import me.yuna.commands.music.PlayCommand;
import me.yuna.commands.music.QueueCommand;
import me.yuna.commands.music.SkipCommand;
import me.yuna.commands.music.StopCommand;
import me.yuna.commands.music.VolumeCommand;
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
		builder.setPrefix("yuna");
		builder.setActivity(Activity.watching("'yuna help'"));
		builder.setOwnerId("195992711982088192");
		builder.setHelpWord("helpme");
			
		
		CommandClient client = builder.build();

			// Help command
			client.addCommand(new HelpCommand());
			
			// Random commands (so far)
			client.addCommand(new MemeCommand());
			client.addCommand(new FootballCommand());
			client.addCommand(new Ball8Command());
			// Music commands
			client.addCommand(new JoinVCCommand());
			client.addCommand(new LeaveVCCommand());
			client.addCommand(new PlayCommand());
			client.addCommand(new StopCommand());
			client.addCommand(new SkipCommand());
			client.addCommand(new VolumeCommand());
			client.addCommand(new QueueCommand());
			client.addCommand(new NowPlayingCommand());
			
			
			
			
		jda.addEventListener(client);
		
		
	}
	
public static void main(String[] args) throws LoginException, IOException {
		
		new Bot();
		System.out.println("Bot enabled");
	}
}
