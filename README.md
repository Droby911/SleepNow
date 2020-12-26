# SleepNow
- translated by Google Translate. If he translated incorrectly, please correct.
- the source code is explained in Russian, as I did not want to translate the explanations out of fear that it would become unclear what the code means

Spigot plugin for Minecraft 1.16.4 
- Allows you to skip the night if a certain number of people are sleeping on the server at the same time.
- Displays in chat how many players it takes to skip the night.
- Displays a message in the chat when the required number of players are gathered, and the night is skipped.

How does the plugin work?
- The night is skipped, provided that a certain number of players are sleeping at the moment. This number is determined depending on the total number of players on the server. If the server has more than eight players, but less than 60, then these players are divided by 3 (and rounded down), and the resulting number will mean how many players are needed to skip the night. If there are more than 60 players, then the players are divided by 4. If there are less than eight players, then one person is enough to skip the night.

What needs to be implemented:
1. Test the plugin (on a server with more than 4 players) and report bugs.

settings.yml:
- Configures the output of messages to the chat.
- Changes the language of the chat output.
- If you have a non-default world name, enter your world name in the settings file.

Languages (or if you need just change chat text) for the plugin can be added along the path plugins/SleepNow/language/you_language.yml, similar to the sleepnow_en.yml file (which will be created after the first launch of the plugin).

Standard and additional languages can be downloaded here (or create yourself):
https://github.com/0x115/SleepNow/tree/master/src/main/resources/language
