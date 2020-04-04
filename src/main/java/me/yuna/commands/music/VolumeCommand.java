package me.yuna.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.managers.AudioManager;

public class VolumeCommand extends Command {


	public VolumeCommand() {
		super.name = "volume";
		super.aliases = new String[]{"vol"};
	}
	
	@Override
	protected void execute(CommandEvent event) {
		PlayerManager manager = PlayerManager.getInstance();
		AudioManager audioManager = event.getGuild().getAudioManager();
        GuildMusicManager musicManager = manager.getGuildMusicManager(event.getGuild());
		String[] arguments = event.getArgs().split(" ");


		if (audioManager.isConnected()) {

			if (arguments.length > 0 && !arguments[0].isEmpty()) {
				
				try {
					
					int volume = Integer.parseInt(arguments[0]);
					musicManager.player.setVolume(volume);
					event.reply("Changed volume to `" + volume + "`");
					
				} catch (NumberFormatException ex) {
					event.reply("Invalid volume: `" + arguments[0] + "` - only integer-values allowed");
				}
				
			} else {
				
				event.reply("Current volume is `" + musicManager.player.getVolume() + "`");
				
			}
			
		} else {
			
			event.reply("Not connected!");
			
		}
		
	}
}
