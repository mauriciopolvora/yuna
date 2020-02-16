package me.yuna.commands;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class MemeCommand extends Command {

	
	
	public MemeCommand() {
		super.name = "meme";
		
		
	}
	
	
	public JSONObject object() throws MalformedURLException, IOException {
		return new JSONObject(IOUtils.toString(new URL("https://meme-api.herokuapp.com/gimme"), Charset.forName("UTF-8")));
	}
	
	@Override
	protected void execute(CommandEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Color.blue);
		
		try {
			builder.setTitle(object().getString("title"));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		try {
			builder.setImage(object().getString("url"));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		try {
			builder.setDescription("Subreddit: " + object().getString("subreddit"));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		event.reply(builder.build());
		
		
		
	}

	
}

	