package me.yuna.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveVCCommand extends Command {

	
	public LeaveVCCommand() {
		super.name = "leave";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		
		AudioManager audioManager = event.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            event.reply("I'm not connected to a voice channel");
            return;
        }
		
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(event.getMember())) {
           event.reply("You have to be in the same voice channel as me to use this");
            return;
        }
        
        audioManager.closeAudioConnection();
        event.reply("Leaving your Voice Channel");
        
	}

}
