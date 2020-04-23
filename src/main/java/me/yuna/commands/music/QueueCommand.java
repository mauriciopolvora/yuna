package me.yuna.commands.music;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.yuna.commands.music.PlayerManager.AudioInformation;
import net.dv8tion.jda.api.EmbedBuilder;

public class QueueCommand extends Command {
	
	public QueueCommand() {
		super.name = "queue";
	}	
	
	@Override
	protected void execute(CommandEvent event) {
	
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
		
		if(queue.isEmpty()) {
			event.reply("The queue is empty");
		}
		
		int trackCount = Math.min(queue.size(), 20);
		List<AudioTrack> tracks = new ArrayList<>(queue);
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Current Queue (Total: " + queue.size() + ")");
		
		for (int i = 0; i < trackCount; i++) {
			AudioTrack track = tracks.get(i);
			AudioInformation info = AudioInformation.fromTrack(track);
			
			builder.setColor(Color.cyan);
			builder.appendDescription(String.format(
					"%s - %s\n",
					info.getTitle(),
					info.getAuthor()
					));
			
		}
		
		event.reply(builder.build());
	}

}

