package me.yuna.commands.anime;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Random;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;


public class RandomImageGet extends Command{

	
	public RandomImageGet() {
		super.name = "kiss";
	}
	
	
	@Override
	protected void execute(CommandEvent event) {
		
		Random rand = new Random();
		int number = rand.nextInt(66);
		
		File file = new File("kiss/" + number + ".gif");
		String[] arguments = event.getArgs().split(" ");
		
		if (arguments[0].equalsIgnoreCase("")) {
			event.reply("heyy dont kiss the air, tag someone!");
		} else {
			
			MessageChannel channel = event.getChannel(); // = reference of a MessageChannel
			 EmbedBuilder embed = new EmbedBuilder();
			 embed.setImage("attachment://image.gif") // we specify this in sendFile as image
			      .setDescription(event.getAuthor().getName() + " kisses " + arguments[0]);
			 
			 channel.sendFile(file, "image.gif").embed(embed.build()).queue();
			
		}

		
		
		
		
		
	}

}