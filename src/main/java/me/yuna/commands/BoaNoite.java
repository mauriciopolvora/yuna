package me.yuna.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class BoaNoite extends Command {
	
	public BoaNoite() {
	 super.name = "dorme";
	}

	@Override
	protected void execute(CommandEvent event) {
		
		event.reply("Obrigado, boa noite dorme bem <3, sonhos cor de rosa abraçados ao teu noddy.");
	}

}