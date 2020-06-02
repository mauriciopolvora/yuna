package me.yuna.commands.api;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SpaceX extends Command {

    private final EventWaiter waiter;

    public SpaceX(final EventWaiter waiter) {
        super.name = "spacex";
        this.waiter = waiter;
    }

    public StringBuffer responseRockets() {
        String line;
        BufferedReader reader;
        final StringBuffer responseContent = new StringBuffer();
        URL url;
        try {
            url = new URL("https://api.spacexdata.com/v3/rockets");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }

            reader.close();
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return responseContent;
    }

    public StringBuffer responseLaunches() {
        String line;
        BufferedReader reader;
        final StringBuffer responseContent = new StringBuffer();
        URL url;
        try {
            url = new URL("https://api.spacexdata.com/v3/launches");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }

            reader.close();
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return responseContent;
    }

    public Rocket[] parseRockets(final String responseBody) {

        final JSONArray info = new JSONArray(responseBody);
        final Rocket[] rockets = new Rocket[info.length()];

        for (int i = 0; i < info.length(); i++) {
            final JSONObject rocketJson = info.getJSONObject(i);

            rockets[i] = new Rocket(rocketJson.getString("rocket_name"), rocketJson.getString("description"),
                    rocketJson.getString("wikipedia"), rocketJson.getJSONArray("flickr_images").get(0).toString(),
                    rocketJson.getInt("cost_per_launch"), rocketJson.getInt("success_rate_pct"),
                    rocketJson.getJSONObject("height").getInt("meters"),
                    rocketJson.getJSONObject("engines").getInt("number"),
                    rocketJson.getJSONObject("engines").getString("type"));
        }

        return rockets;

    }

    public Launch parseLaunches(final String responseBody) {
        final JSONArray info = new JSONArray(responseBody);
        final Launch launch;

        for (int i = 0; i < info.length(); i++) {
            final JSONObject launchJson = info.getJSONObject(i);
            if (launchJson.getBoolean("upcoming") == true) {
                launch = new Launch(launchJson.getString("mission_name"), launchJson.getString("launch_date_utc"),
                        launchJson.getJSONObject("launch_site").getString("site_name_long"), true);
                return launch;
            }
        }
        return null;
    }

    public EmbedBuilder rocketInfo(final Rocket[] rockets, final int location) {

        final EmbedBuilder rocketinfo = new EmbedBuilder();
        rocketinfo.setColor(Color.cyan);
        rocketinfo.setThumbnail("https://www.spacex.com/static/images/share.jpg");
        rocketinfo.setTitle(rockets[location].getRocket_name());
        rocketinfo.setDescription(rockets[location].getRocket_description());
        rocketinfo.setImage(rockets[location].getRocket_images());
        rocketinfo.addField("**Cost:**", String.valueOf(rockets[location].getRocket_cost()), true);
        rocketinfo.addField("**Success rate percentage:**", String.valueOf(rockets[location].getRocket_success_rate()),
                true);
        rocketinfo.addField("**Height:**", String.valueOf(rockets[location].getRocket_height()) + " meters", true);
        rocketinfo.addField("**Engines:**", String.valueOf(rockets[location].getRocket_engine_number() + " "
                + rockets[location].getRocket_engine_type() + " engines."), true);
        rocketinfo.setFooter("All information taken from SpaceX public api",
                "https://www.spacex.com/static/images/share.jpg");

        return rocketinfo;
    }

    public static class Rocket {
        String rocket_name, rocket_description, rocket_wikipedia, rocket_images, rocket_engine_type;
        int rocket_cost, rocket_success_rate, rocket_height, rocket_engine_number;

        public Rocket(final String rocket_name, final String rocket_description, final String rocket_wikipedia,
                final String rocket_images, final int rocket_cost, final int rocket_success_rate,
                final int rocket_height, final int rocket_engine_number, final String rocket_engine_type) {
            this.rocket_cost = rocket_cost;
            this.rocket_name = rocket_name;
            this.rocket_description = rocket_description;
            this.rocket_wikipedia = rocket_wikipedia;
            this.rocket_images = rocket_images;
            this.rocket_engine_number = rocket_engine_number;
            this.rocket_engine_type = rocket_engine_type;
            this.rocket_success_rate = rocket_success_rate;
            this.rocket_height = rocket_height;
        }

        public int getRocket_cost() {
            return rocket_cost;
        }

        public String getRocket_description() {
            return rocket_description;
        }

        public String getRocket_images() {
            return rocket_images;
        }

        public String getRocket_name() {
            return rocket_name;
        }

        public String getRocket_wikipedia() {
            return rocket_wikipedia;
        }

        public int getRocket_engine_number() {
            return rocket_engine_number;
        }

        public String getRocket_engine_type() {
            return rocket_engine_type;
        }

        public int getRocket_height() {
            return rocket_height;
        }

        public int getRocket_success_rate() {
            return rocket_success_rate;
        }

    }

    public static class Launch {

        String missionName, launchDate, launchSite;
        boolean upcoming;

        public Launch(final String missionName, final String launchDate, final String launchSite,
                final boolean upcoming) {
            this.missionName = missionName;
            this.launchDate = launchDate;
            this.launchSite = launchSite;
            this.upcoming = upcoming;
        }

        public String getLaunchDate() {
            String date = launchDate;

            int startIndex = date.indexOf("T") - 1;
            int endIndex = date.indexOf("Z") + 1;
            String replacement = "";
            String toBeReplaced = date.substring(startIndex + 1, endIndex);

            return launchDate.replaceAll(toBeReplaced, replacement);
        }

        public String getLaunchSite() {
            return launchSite;
        }

        public String getMissionName() {
            return missionName;
        }

    }

    @Override
    protected void execute(final CommandEvent event) {
        final String commands = "`1 - Rockets`" + "\n" + "`2 - Next launch`";
        final EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Space X");
        builder.setDescription(
                "Here you can find some active information about SpaceX missions and rockets being manufactured.");
        builder.addField("Type the number you want to get info from", commands, true);
        builder.setThumbnail("https://www.spacex.com/static/images/share.jpg");
        builder.setFooter("All information taken from SpaceX public api",
                "https://www.spacex.com/static/images/share.jpg");
        builder.setColor(Color.cyan);
        event.reply(builder.build());

        waiter.waitForEvent(MessageReceivedEvent.class, e -> e.getAuthor().equals(event.getAuthor())
                && e.getChannel().equals(event.getChannel()) && !e.getAuthor().isBot(), e -> {

                    final String response = e.getMessage().getContentRaw().toString();
                    if (response.equalsIgnoreCase("1")) {

                        final Rocket[] rockets = parseRockets(responseRockets().toString());
                        final String[] rocketNames = new String[rockets.length];

                        for (int i = 0; i < rockets.length; i++) {
                            rocketNames[i] = rockets[i].getRocket_name();
                        }

                        final EmbedBuilder rocketEmbed = new EmbedBuilder();
                        String embedDescription = "";
                        for (int i = 0; i < rocketNames.length; i++) {
                            embedDescription += "`" + String.valueOf(i) + "- " + rocketNames[i] + "`\n";
                        }
                        rocketEmbed.setTitle("Space X active rockets");
                        rocketEmbed.addField("**Type the number you want to get info from**", embedDescription, true);
                        rocketEmbed.setColor(Color.cyan);
                        rocketEmbed.setFooter("All information taken from SpaceX public api",
                                "https://www.spacex.com/static/images/share.jpg");
                        rocketEmbed.setThumbnail("https://www.spacex.com/static/images/share.jpg");

                        event.reply(rocketEmbed.build());

                        waiter.waitForEvent(MessageReceivedEvent.class, e1 -> e1.getAuthor().equals(event.getAuthor())
                                && e1.getChannel().equals(event.getChannel()), e1 -> {
                                    event.reply(rocketInfo(rockets, Integer.parseInt(e1.getMessage().getContentRaw()))
                                            .build());
                                });
                    }

                    if (response.equalsIgnoreCase("2")) {

                        final Launch launch = parseLaunches(responseLaunches().toString());

                        EmbedBuilder launchEmbed = new EmbedBuilder();
                        launchEmbed.setTitle(launch.getMissionName());
                        launchEmbed.addField("**Launch date:**", launch.getLaunchDate(), true);
                        launchEmbed.addField("**Launch Site:**", launch.getLaunchSite(), false);
                        launchEmbed.setColor(Color.cyan);
                        launchEmbed.setFooter("All information taken from SpaceX public api",
                                "https://www.spacex.com/static/images/share.jpg");
                        launchEmbed.setThumbnail("https://www.spacex.com/static/images/share.jpg");
                        event.reply(launchEmbed.build());

                    }

                });

    }

}