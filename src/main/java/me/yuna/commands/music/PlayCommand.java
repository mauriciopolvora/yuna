package me.yuna.commands.music;

import java.awt.Color;

import java.net.MalformedURLException;
import java.net.URL;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand extends Command {
 
	private EventWaiter waiter;
	
	public PlayCommand(EventWaiter waiter) {
		super.name = "play";
		this.waiter = waiter;
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
			event.reply("Getting video info... Please wait.");
			String name = event.getArgs().replaceAll(" ", "_");
			String query ="ytsearch5:" + name;
			System.out.println(query);	
			String[] list = manager.list(channel, query);
			
			String print = "`1- " + list[0] + "`\n" + 
						"`2- " + list[1] + "`\n" + 
						"`3- " + list[2] + "`\n" + 
						"`4- " + list[3] + "`\n" + 
						"`5- " + list[4] + "`\n" +
						"`6- Exit`\n";
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Results");
			builder.setColor(Color.cyan);
			builder.setDescription(print);
			event.reply(builder.build());
			
			waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()) 
					, e -> {
						
						
						String message = e.getMessage().getContentRaw().toString();						
						int querylocation = Integer.parseInt(message);
						
						if(querylocation == 6) {
							event.reply("Exiting");
							return;
						}

						String queryPlay = "ytsearch:" + list[querylocation-1].replaceAll(" ", "_");
						manager.loadAndPlay(channel, queryPlay, "0");
		
					} );				

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