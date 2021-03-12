package me.yuna.commands.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;

public class AATCommand extends Command {

    public AATCommand() {
        super.name = "aat";
    }

    public JSONObject rObject() throws JSONException, MalformedURLException, IOException {

        return new JSONObject(IOUtils.toString(new URL(
                "https://api.pushshift.io/reddit/search/submission/?subreddit=AverageAnimeTiddies&size=500&score=>50"),
                Charset.forName("UTF-8")));

    }

    @Override
    protected void execute(CommandEvent event) {
        String[] arguments = event.getArgs().split(" ");
        int size = 0;
        System.out.println("Tamanho: " + arguments.length);
        System.out.println("Arg[0]: " + arguments[0]);
        if (!arguments[0].equals(""))
            size = Integer.parseInt(arguments[0]);
        System.out.println("Size: " + size);

        try {
            Random r = new Random();
            JSONObject object = rObject();
            JSONArray urlA = object.getJSONArray("data");

            if (size == 0) {
                JSONObject uri = urlA.getJSONObject(r.nextInt(99));
                String url = uri.getString("url");
                
                EmbedBuilder builder = new EmbedBuilder();
                System.out.println("Url= " + url);
                builder.setImage(url);

                event.reply(builder.build());
            }

            int aux = 0;
            while (aux < size) {
                JSONObject uri = urlA.getJSONObject(r.nextInt(99));
                String url = uri.getString("url");

                EmbedBuilder builder = new EmbedBuilder();
                System.out.println("Url= " + url);
                builder.setImage(url);

                event.reply(builder.build());
                System.out.println("Printed: " + aux);
                aux += 1;
            }

        } catch (JSONException | IOException e) {
            event.reply("Can't connect to api.");
            e.printStackTrace();
        }

    }

}

// https://api.pushshift.io/reddit/search/submission/?subreddit=ecchi&size=300&score=>500