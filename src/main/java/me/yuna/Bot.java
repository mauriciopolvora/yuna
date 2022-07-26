package me.yuna;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.security.auth.login.LoginException;

import org.json.JSONObject;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import me.yuna.commands.fun.Ball8Command;
import me.yuna.commands.fun.BoaNoite;
import me.yuna.commands.fun.BomDia;
import me.yuna.commands.api.AATCommand;
import me.yuna.commands.api.EcchiCommand;
import me.yuna.commands.api.FootballCommand;
import me.yuna.commands.HelpCommand;
import me.yuna.commands.api.MemeCommand;
import me.yuna.commands.api.SpaceX;
import me.yuna.commands.anime.KissCommand;
import me.yuna.commands.music.JoinVCCommand;
import me.yuna.commands.music.LeaveVCCommand;
import me.yuna.commands.music.NowPlayingCommand;
import me.yuna.commands.music.PauseCommand;
import me.yuna.commands.music.PlayCommand;
import me.yuna.commands.music.QueueCommand;
import me.yuna.commands.music.ResumeCommand;
import me.yuna.commands.music.SkipCommand;
import me.yuna.commands.music.StopCommand;
import me.yuna.commands.music.VolumeCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;


public class Bot {
	
	
	
	private JSONObject tokens;

	public Bot() throws LoginException, IOException {
		
		String file = new String(Files.readAllBytes(new File("tokens.tkn").toPath()));
		this.tokens = new JSONObject(file);
	
		//final JDA jda = new JDABuilder(AccountType.BOT).setToken(tokens.getString("discordtkn")).build();
		final JDA jda = JDABuilder.createDefault(tokens.getString("discordtkn")).build();
				
		CommandClientBuilder builder = new CommandClientBuilder();
		builder.setPrefix("yuna");
		builder.setActivity(Activity.watching("'yuna help'"));
		builder.setOwnerId("195992711982088192");
		builder.setHelpWord("helpme");
			
		
		CommandClient client = builder.build();
		EventWaiter waiter = new EventWaiter();

			// Help command
			client.addCommand(new HelpCommand());
			
			//api commands
			client.addCommand(new FootballCommand());
			client.addCommand(new MemeCommand());
			client.addCommand(new SpaceX(waiter));
			client.addCommand(new EcchiCommand());
			client.addCommand(new AATCommand());

			// Random commands (so far)
			client.addCommand(new Ball8Command());
			client.addCommand(new BoaNoite());
			client.addCommand(new BomDia());
			client.addCommand(new KissCommand());
			
			// Music commands
			client.addCommand(new JoinVCCommand());
			client.addCommand(new LeaveVCCommand());
			client.addCommand(new PlayCommand(waiter));
			client.addCommand(new StopCommand());
			client.addCommand(new SkipCommand());
			client.addCommand(new PauseCommand());
			client.addCommand(new ResumeCommand());
			client.addCommand(new VolumeCommand());
			client.addCommand(new QueueCommand());
			client.addCommand(new NowPlayingCommand());
			
			
			
		jda.addEventListener(waiter);
		jda.addEventListener(client);
	}
	
public static void main(String[] args) throws LoginException, IOException {
		
		new Bot();
		System.out.println("Bot enabled");
	}
}
