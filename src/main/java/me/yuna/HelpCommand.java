package me.yuna;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class HelpCommand extends Command {
	
	
	public HelpCommand() {
		super.name = "help";
	}

	@Override
	protected void execute(CommandEvent event) {
	
		EmbedBuilder builder = new EmbedBuilder();
		System.out.println(event.getSelfUser().getAvatarUrl());
		builder.setTitle("Command List");
		builder.setDescription("Here is the list of commands!" + "\n" + "For more info on a specific command, use `yuna help {command}`");
		builder.addField("Football", "`football teams` `football jogos`", true);
		builder.addField("Memes", "`memes`", false);
		
		event.reply(builder.build());
		
	}

}
