package me.yuna.commands.fun;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class BomDia extends Command {
	
	public BomDia() {
	 super.name = "bom";
	}

	@Override
	protected void execute(CommandEvent event) {
		
		event.reply("Obrigado, mas quem vai ter um bom dia és tu <3");
	}

}
