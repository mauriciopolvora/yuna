package me.yuna.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

public class SkipCommand extends Command {
	
	public SkipCommand() {
		super.name = "skip";
	}

	@Override
	protected void execute(CommandEvent event) {
		PlayerManager manager = PlayerManager.getInstance();
		GuildMusicManager musicManager = manager.getGuildMusicManager(event.getGuild());
		TrackScheduler scheduler  = musicManager.scheduler;
		AudioPlayer player = musicManager.player;
		
		
		
		if(player.getPlayingTrack() == null) {
			event.reply("I am not playing annything currently");
		}
		
		scheduler.nextTrack();
		event.reply("Skipping to the next track");
		
	}

}
