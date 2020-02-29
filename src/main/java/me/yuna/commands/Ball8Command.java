package me.yuna.commands;



import java.util.Random;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Ball8Command extends Command {

	public Ball8Command() {
			super.name = "8ball";
			
	}
	
	
	@Override
	protected void execute(CommandEvent event) {
		String question = event.getArgs().toString();
		String answer = magic8ball();
				
		event.reply("**" + event.getAuthor().getName() + " asked" + ": **" + question + "\n" + "**Answer: **" + answer);
			
		
	}
	
	public String magic8ball  () {
		
		Random r = new Random();

		int choice = 1 + r.nextInt(15);
		String response = "";

		if ( choice == 1 )
			response = "It is certain";
		else if ( choice == 2 )
			response = "It is decidedly so";
		else if ( choice == 3 )
			response = "Without a doubt";
		else if ( choice == 4 )
			response = "Yes - definitely";
		else if ( choice == 5 )
			response = "You may rely on it";
		else if ( choice == 6 )
			response = "As I see it, yes";
		else if ( choice == 7 )
			response = "Most likely";
		else if ( choice == 8 )
			response = "Outlook good";
		else if ( choice == 9 )
			response = "Yes.";
		else if ( choice == 10 )
			response = "My reply is no";
		else if ( choice == 11 )
			response = "My sources say no";
		else if ( choice == 12 )
			response = "Outlook not so good";
		else if ( choice == 13 )
			response = "Very doubtful";
		else if ( choice == 14 )
			response = "Dont think so";
		else if ( choice == 15 )
			response = "Not at all";
		else 
			response = "8-BALL ERROR!";
		return response;

	
}

}
