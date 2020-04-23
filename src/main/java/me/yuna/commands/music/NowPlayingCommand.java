package me.yuna.commands.music;

import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import me.yuna.commands.music.PlayerManager.AudioInformation;
import net.dv8tion.jda.api.EmbedBuilder;

public class NowPlayingCommand extends Command {

	public NowPlayingCommand() {
		super.name = "np";
	}

	@Override
	protected void execute(CommandEvent event) {
		PlayerManager manager = PlayerManager.getInstance();
		GuildMusicManager musicManager = manager.getGuildMusicManager(event.getGuild());
		AudioPlayer player = musicManager.player;
		

		if(player.getPlayingTrack() == null) {
			event.reply("I am not playing annything currently");
			return;
		}
		
		AudioTrack track = player.getPlayingTrack();
		
		AudioInformation audioInfo = AudioInformation.fromTrack(track);

		EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Currently Playing: ");
			builder.setDescription(String.format(
	                "**Playing** [%s](%s)\n%s %s - %s",
	                audioInfo.getTitle(),
	                audioInfo.getUrl(),
	                player.isPaused() ? "\u23F8" : "▶",
	                formatTimeMills(track.getPosition()),
	                formatTimeSecs(audioInfo.getDuration())
	        ));
			
		event.reply(builder.build());
	}
	
	private String formatTimeMills(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis % TimeUnit.HOURS.toMillis(1) / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	
	private String formatTimeSecs(int timeInSecs) {
        final long hours = timeInSecs / TimeUnit.HOURS.toSeconds(1);
        final long minutes = timeInSecs % TimeUnit.HOURS.toSeconds(1) / TimeUnit.MINUTES.toSeconds(1);
        final long seconds = timeInSecs % TimeUnit.MINUTES.toSeconds(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
