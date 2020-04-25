package me.yuna.commands.music;

import java.net.MalformedURLException;
import java.net.URL;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

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
		
		AudioManager audioManager = event.getGuild().getAudioManager();
		
		if (arguments.length == 0) {
			event.reply("Please provide arguments");
			event.reply("Use the bot using the following command:" + "\n" + "`yuna play {url} {index} (if on a playlist)`");
			
		} 
			
		if (!isUrl(arguments[0])) {			
			if (!audioManager.isConnected()) {
				connectBot(event, audioManager);				
			}
			String name = event.getArgs().replaceAll(" ", "_");
			String query ="ytsearch:" + name;
			System.out.println(query);
			manager.loadAndPlay(channel, query, "0");

            return;
            
		}		
		if (!audioManager.isConnected()) { 
			connectBot(event, audioManager);
		}
		
		if (arguments.length > 1) {
			 manager.loadAndPlay(channel, arguments[0], arguments [1]);
		} else {
			manager.loadAndPlay(channel, arguments[0], "0");
		}

        manager.getGuildMusicManager(event.getGuild()).player.setVolume(25);
		
	}
	
	private void connectBot(CommandEvent event, AudioManager audioManager) {
		GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
		
		if (!memberVoiceState.inVoiceChannel()) {
            event.reply("Not joined yet. Please join a voice channel first.");
            return;
        }
		 
		VoiceChannel voiceChannel = memberVoiceState.getChannel(); 
		event.reply("Auto-Joining your voice chat...");
		audioManager.openAudioConnection(voiceChannel);
	}
}