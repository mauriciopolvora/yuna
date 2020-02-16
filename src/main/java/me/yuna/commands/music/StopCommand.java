package me.yuna.commands.music;



import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class StopCommand extends Command {
   
	public StopCommand() {
		super.name = "stop";
	}

	@Override
	protected void execute(CommandEvent event) {
			PlayerManager manager = PlayerManager.getInstance();
	        GuildMusicManager musicManager = manager.getGuildMusicManager(event.getGuild());

	        musicManager.scheduler.getQueue().clear();
	        musicManager.player.stopTrack();
	        musicManager.player.setPaused(false);

	        event.reply("Stopping the player and clearing the queue");
	}
}