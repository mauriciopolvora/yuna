package me.yuna.commands.music;

import java.net.MalformedURLException;
import java.net.URL;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.TextChannel;

public class PlayCommand extends Command {
 
	public PlayCommand() {
		super.name = "play";
		
	}
	
	private boolean isUrl(String input) {
        try {
            new URL(input);

            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }
	
	@Override
	protected void execute(CommandEvent event) {
		PlayerManager manager = PlayerManager.getInstance();
		TextChannel channel = event.getTextChannel();
		String[] arguments = event.getArgs().split(" ");
		
	
		
		if (arguments.length == 0) {
			event.reply("Please provide arguments");
			event.reply("Use the bot using the following command:" + "\n" + "`yuna play {url} {index} (if on a playlist)`");
			
		} 
			
		if (!isUrl(arguments[0])) {
			
            // Use the youtube api for search instead, making a lot of requests with "ytsearch:" will get you blocked
			
            event.reply("Please provide a valid youtube, soundcloud or bandcamp link");

            return;
            
		}
		
		
		
		if (arguments.length > 1)
			 manager.loadAndPlay(channel, arguments[0], arguments [1]);
		else  manager.loadAndPlay(channel, arguments[0], "0");

		
       
        manager.getGuildMusicManager(event.getGuild()).player.setVolume(10);
		
	}
}