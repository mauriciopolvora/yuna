package me.yuna.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.managers.AudioManager;

public class ResumeCommand extends Command {


	public ResumeCommand() {
		super.name = "resume";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		PlayerManager manager = PlayerManager.getInstance();
		AudioManager audioManager = event.getGuild().getAudioManager();
        GuildMusicManager musicManager = manager.getGuildMusicManager(event.getGuild());


		if (audioManager.isConnected()) {
				
			if (musicManager.player.isPaused()) {
				musicManager.player.setPaused(false);
				event.reply("Resumed player.");
			} else {
				event.reply("Player is already running! (Use `pause` to pause)");
			}
			
		} else {
			
			event.reply("Not connected!");
			
		}
		
	}
}
