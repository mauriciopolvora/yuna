package me.yuna.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.managers.AudioManager;

public class PauseCommand extends Command {


	public PauseCommand() {
		super.name = "pause";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		PlayerManager manager = PlayerManager.getInstance();
		AudioManager audioManager = event.getGuild().getAudioManager();
        GuildMusicManager musicManager = manager.getGuildMusicManager(event.getGuild());


		if (audioManager.isConnected()) {
				
			if (!musicManager.player.isPaused()) {
				musicManager.player.setPaused(true);
				event.reply("Paused player.");
			} else {
				event.reply("Player is already paused! (Use `resume` to resume)");
			}
			
		} else {
			
			event.reply("Not connected!");
			
		}
		
	}
}
