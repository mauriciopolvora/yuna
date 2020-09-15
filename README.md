# Yuna Discord BOT 
Yuna is a discord bot written in java, using JDA library. It has basic commands and easy to change and use it yourself.
For music playback it downaloads the song locally for a `/cache` folder in order to get smooth playback

This was a personal project that helped me understand public libraries and how to work with API's, it gave a more in depth insight of how i can more easily control and get information and how to display it in a clean UI.

## Features

- Fast loading of songs
- No lag playback
- Supports many sites, including Youtube, Soundcloud and all `ytdownload` supported sources.
- Acess to meme API

## Setup 

- Make sure you have gradle installed by typing `gradle` in your command line. If not you can check it how to do it [here](https://gradle.org/install/)
- Browse to your root directory and create a `tokens.tkn` file. 
- In JSON add a `discordtkn` string with your discord application token inside.
-  Run `gradle run` and do `yuna help` on a discord channel that yuna was added to to see the Bot's commands.