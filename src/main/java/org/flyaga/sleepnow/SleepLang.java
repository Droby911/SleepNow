package org.flyaga.sleepnow;

public class SleepLang {
    public static String language = SleepNow.getInstance().getSettingsConfig().getString("language");
    public static String sleeping_not_enough_players_message;
    public static String sleeping_enough_players_message;
    public boolean isCustomLang(){
        if(!language.equals("sleepnow_en.yml")){
            return true;
        }
        return false;
    }

    public void customMessagesLoad(){
        if(isCustomLang() == false){
            sleeping_not_enough_players_message = SleepNow.getInstance().getDefaultLangConfig().getString("sleeping-not-enough-players-message");
            sleeping_enough_players_message = SleepNow.getInstance().getDefaultLangConfig().getString("sleeping-enough-players-message");
        }
        else{
            sleeping_not_enough_players_message = SleepNow.getInstance().getCustomLangConfig().getString("sleeping-not-enough-players-message");
            sleeping_enough_players_message = SleepNow.getInstance().getCustomLangConfig().getString("sleeping-enough-players-message");
        }
    }
}
