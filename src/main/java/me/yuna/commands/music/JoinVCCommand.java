package me.yuna.commands.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinVCCommand extends Command {

	public JoinVCCommand() {
		super.name = "join";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		
		
		
		AudioManager audioManager = event.getGuild().getAudioManager();
		
		if (audioManager.isConnected()) {
	            event.reply("I'm already connected to a channel");
	            return;
	        }
		 
		GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
		 
		if (!memberVoiceState.inVoiceChannel()) {
	            event.reply("Please join a voice channel first");
	            return;
	        }
		 
		VoiceChannel voiceChannel = memberVoiceState.getChannel(); 
		audioManager.openAudioConnection(voiceChannel);
		event.reply("Joining your voice chat");
		
	}

}
