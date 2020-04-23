package me.yuna.commands.music;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import org.json.JSONObject;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    // Download-destination directory
    public static final File downloadDirectory = new File("audio_cache");

    private PlayerManager() {

        downloadDirectory.mkdirs();
        downloadDirectory.mkdir();

        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager(final Guild guild) {
        final long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(final TextChannel channel, final String trackUrl, final String index) {
        final GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        channel.sendMessage("Okay... *looking up*").queue();
        try {

            // Build request
            YoutubeDLRequest query = new YoutubeDLRequest(trackUrl);
            query.setOption("quiet"); // --quet
            query.setOption("ignore-errors"); // --ignore-errors
            query.setOption("dump-json"); // --dump-json
            query.setOption("retries", 10); // --retries 10

            // Make request and return response
            YoutubeDLResponse queryResult;
            queryResult = YoutubeDL.execute(query);

            String[] dataBuffer = queryResult.getOut().split("\n");

            for (String line : dataBuffer) {
                channel.sendTyping();
                //System.out.println("OUT: " + line);

                try {

                    JSONObject videoData = new JSONObject(line);
                    final String videoUrl = videoData.getString("webpage_url");
                    final String videoId = videoData.getString("id");
                    final String videoTitle = videoData.getString("title");
                    final String videoAuthor = videoData.getString("uploader");
                    final int videoDuration = videoData.getInt("duration");
                    String host = new URL(videoUrl).getHost();
                    final String fileName = host + "_" + videoId + "_";
                    // channel.sendMessage("FWD:: " + videoUrl + " - FN: " + fileName).queue();

                    // Build request
                    YoutubeDLRequest request = new YoutubeDLRequest(videoUrl, downloadDirectory.getAbsolutePath());
                    request.setOption("ignore-errors"); // --ignore-errors
                    request.setOption("extract-audio"); // --extract-audio
                    request.setOption("restrict-filenames"); // --restrict-filenames

                    // String sanatizedTitle = videoTitle.replace("\"", "'");

                    request.setOption("add-metadata"); // --add-metadata

                    request.setOption("output", fileName + "%(title)s.%(ext)s"); // --output www.youtube.com_tmbfT7-tZiM_%(title)s.%(ext)s
                    request.setOption("retries", 10); // --retries 10

                    // Make request and return response
                    // YoutubeDLResponse response;

                    channel.sendTyping();
                    YoutubeDL.execute(request);
                    channel.sendTyping();

                    // File downloadedFile = new File(downloadDirectory, fileName);

                    File[] foundFiles = downloadDirectory.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File file, String name) {
                            return name.indexOf(fileName, 0) == 0;
                        }
                    });

                    if (foundFiles.length <= 0) {
                        throw new FileNotFoundException("File, beginning with \"" + fileName + ".\" not found");
                    }

                    File downloadedFile = foundFiles[0];

                    if (!downloadedFile.exists()) {
                        throw new FileNotFoundException("File \"" + downloadedFile.getAbsolutePath() + "\"not found");
                    }

                    System.out.println("DLF: " + downloadedFile.getAbsolutePath());
                    playFromFile(downloadedFile.getAbsolutePath(), musicManager, channel, new AudioInformation(videoTitle, videoAuthor, videoUrl, videoDuration));
                
                
                } catch (YoutubeDLException e) {
                    e.printStackTrace(System.err);
                    channel.sendMessage("Skipping item: Failed youtube download").queue();
                } catch (MalformedURLException e) {
                    e.printStackTrace(System.err);
                    channel.sendMessage("F. ... check my logs pls .-.\n   ~ Marlformed URLS  (Skipping item)").queue();
                } catch (FileNotFoundException e) {
                    e.printStackTrace(System.err);
                    channel.sendMessage("F. ... check my logs pls .-.\n   ~ File not stored  (Skipping item)").queue();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    channel.sendMessage("F. ... check my logs pls .-.\n   ~ General Exception  (Skipping item)").queue();
                }
            }

            if (dataBuffer.length > 1) {
                channel.sendMessage("Successful added " + dataBuffer.length + " songs to queue").queue();
            }

        } catch (YoutubeDLException e) {
            e.printStackTrace(System.err);
            channel.sendMessage("F. ... check my logs pls .-.\n   ~ Failed youtube download").queue();
        }

    }

    private void playFromFile(String file, GuildMusicManager musicManager, TextChannel channel, AudioInformation audioInfo) {

        Future<Void> future = playerManager.loadItem(file, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(final AudioTrack track) {
                //AliasedAudioTrack aliasedTrack = new AliasedAudioTrack(track, title, author);
                track.setUserData(audioInfo);
                AudioInformation audioInfo = AudioInformation.fromTrack(track);
                channel.sendMessage("Adding to queue " + audioInfo.getTitle()).queue();
                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(final AudioPlaylist playlist) {
                channel.sendMessage("~ Playlist? .. heh .-.").queue();
            }

            @Override
            public void noMatches() {
                channel.sendMessage("~ Donwload not found? .-.").queue();
            }

            @Override
            public void loadFailed(final FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });

        while (!future.isDone()) {
            try {
                channel.sendTyping();
                Thread.sleep(100L);
            } catch (InterruptedException e) {
            }
        }
    }

    private void play(final GuildMusicManager musicManager, final AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

    public static class AudioInformation {
        private String title;
        private String author;
        private String url;
        private int duration;

        public AudioInformation(String title, String author, String url, int duration) {
            this.title = title;
            this.author = author;
            this.url = url;
            this.duration = duration;
        }

        /**
         * @return the author
         */
        public String getAuthor() {
            return author;
        }

        /**
         * @return the duration
         */
        public int getDuration() {
            return duration;
        }

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        public static AudioInformation fromTrack(AudioTrack track) {
            Object userData = track.getUserData();
            if (userData instanceof AudioInformation) {
                return (AudioInformation) userData;
            } else {
                throw new ClassCastException("User-data in AudioTrack is not of the type AudioInformation!");
            }
        }
    } 
}