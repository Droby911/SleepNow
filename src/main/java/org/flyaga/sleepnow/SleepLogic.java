package org.flyaga.sleepnow;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitScheduler;
import static org.bukkit.Bukkit.*;

public class SleepLogic {
    public static double players_count_to_skip; // тут будет число игроков, необходимое для скипа ночи
    public static int current_sleeping = 0; // тут будет число спящих игроков на данный момент
    public static List<LivingEntity> current_sleeping_players_entity = new ArrayList<>(); // тут будет список игроков, которые спят в кровати
    public static List<Player> players; // тут будет список игроков в онлайне
    public static String world_name = SleepNow.getInstance().getSettingsConfig().getString("world-name");
    public static World world = getServer().getWorld(world_name); // тут получаем мир по его названию
    public static LivingEntity playerEnt; // объявляем энтити, этим энтити будет игрок
    public static long delay = 20L; // задержка плагина (в тиках)
    public static String silent_mode = SleepNow.getInstance().getSettingsConfig().getString("silent-mode"); // получаем значение silent-mode из файла настроек
    public static boolean is_message_displayed = false; // выставляется в true, когда в чат выводится сообщение для скипа ночи. Нужна для того, что бы в чат не было спама из сообщений о пропуске ночи.

    public static void logicStart() {
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(SleepNow.getInstance(), new Runnable() { // делаем что-то типа while(true) с задержкой
            @Override
            public void run() {
                long time = world.getTime(); // получаем время в мире
                boolean isDay = time < 12300 || time > 23850; // условия для определения что есть день, а что есть ночь
                if (isDay == false) { // если в мире ночь
                    is_message_displayed = false; // ставим false, т.е обнуляем переменную после того как начнется ночь
                    playerManipulation(); // то начинаем работу
                }
            }
        }, 0L, delay); // вызываем метод run() каждые *delay* тиков
    }


    public static void playerManipulation() {
        players = world.getPlayers(); // получаем всех игроков в онлайне в список с типом Player (не путать с LivingEntity)
        if (players.size() >= 8 && players.size() <= 59) { // если игроков больше или равно 7, и меньше или равно 59
            players_count_to_skip = Math.floor(players.size() / 3); // получяем количество игроков, необходимых для сна путем деления на 3 и округления в меньшкую сторону
            sleepingPlayerLogic(players); // идем в обработку игроков
        } else if (players.size() >= 60) { // если игроков больше 60
            players_count_to_skip = Math.floor(players.size() / 4);
            sleepingPlayerLogic(players);
        } else {
            for (Player players_for : players) {
                updateSingleSleeping(players_for); // вызываем функцию, которая позволит скипнуть ночь даже одному игроку
            }
        }
    }


    public static void sleepingPlayerLogic(List<Player> players) {
        for (Player players_for : players) { // циклом преобразуем список игроков в одного игрока
            playerEnt = players_for; // тут один игрок
            boolean isSleeping = playerEnt.isSleeping();
            if (isSleeping == true) { // если он спит
                if (current_sleeping_players_entity.contains(playerEnt)) { // если этот игрок до сих пор в кровати
                    continue; // то пропускаем его
                } else { // иначе
                    current_sleeping_players_entity.add(playerEnt); // добавляем его в список спящих
                    current_sleeping = current_sleeping + 1; // прибавляем кол-во спящих на один
                    displayMessage(playerEnt, 1); // показываем сообщение что игрок лег спать
                    updateAndSet(playerEnt); // проверяем, хватает ли игроков для скипа
                }
            } else if (isSleeping == false && current_sleeping_players_entity.contains(playerEnt)) { // если игрок уже не спит, но он еще в списке спящих
                current_sleeping_players_entity.remove(playerEnt); // удаляем его из списка
                current_sleeping = current_sleeping - 1; // убавляем кол-во спящих на один
            }
        }
    }

    public static void updateAndSet(LivingEntity playerEnt){ // PlayersDivision - определяется в зависимости от того, сколько игроков на сервере
        if(current_sleeping >= (int)players_count_to_skip){ // если количество спящих игроков больше или равно количеству игроков, необходимых для скипа
            world.setTime(0); // ставим утро
            current_sleeping = 0; // обнуляем списки
            players_count_to_skip = 0; // обнуляем списки
            current_sleeping_players_entity.clear(); // обнуляем списки
            if(is_message_displayed == false){ // если сообщение о пропуске ночи было показано, то не выводим сообщение, чтобы избежать одинаковых сообщений из-за цикла
                displayMessage(playerEnt, 2); // показываем сообщение о скипе
            }
        }
    }

    public static void updateSingleSleeping(LivingEntity playerEnt){ // когда хватает и одного игрока для скипа
        boolean isSleeping = playerEnt.isSleeping();
        if(isSleeping == true)

        {
            world.setTime(0); // ставим утро
            displayMessage(playerEnt, 2); // показываем сообщение о скипе
        }
    }

    public static void displayMessage(LivingEntity playerEnt, int what_display){
        if(silent_mode.equals("false")) { // если сайлент-мод в конфиге отключен
            if (what_display == 1) { // sleeping-not-enough-players-message
                displayMessageConstructor(playerEnt, what_display);
            }
            if (what_display == 2) { // sleeping-enough-players-message
                displayMessageConstructor(playerEnt, what_display);
            }
        }
        else if(silent_mode.equals("not-enough") && what_display == 1){ // если настройка not-enough
            displayMessageConstructor(playerEnt, what_display);
        }
        else if(silent_mode.equals("enough") && what_display == 2) { // если настройка enough
            displayMessageConstructor(playerEnt, what_display);
        }
        else{
            return;
        }
    }

    public static void displayMessageConstructor(LivingEntity playerEnt, int what_display){
        if (what_display == 1) { // sleeping-not-enough-players-message
            int to_skip = (int)players_count_to_skip - current_sleeping; // получаем кол-во игроков, необходимых для скипа, с учетом того, что текущий игрок лег спать
            if (to_skip > 0) {
                String msg = SleepLang.sleeping_not_enough_players_message.replaceAll("(?:\\{0)(?:})", playerEnt.getName()); // заменяем {0} в конфиге
                msg = msg.replaceAll("(?:\\{1)(?:})", Integer.toString(to_skip)); // заменяем {1} в конфиге
                msg = msg.replaceAll("(&([a-f0-9]))", "\u00A7$2");

                String msgU = "&a---------------------------";
                msgU = msgU.replaceAll("(&([a-f0-9]))", "\u00A7$2");
                broadcastMessage(msgU);
                broadcastMessage(msg); // выводим в чат
                broadcastMessage(msgU);
            }
        }
        if (what_display == 2) { // sleeping-enough-players-message
            String msg = SleepLang.sleeping_enough_players_message.replaceAll("(?:\\{0)(?:})", playerEnt.getName());
            msg = msg.replaceAll("(&([a-f0-9]))", "\u00A7$2");
            String msgU = "&3---------------------------";
            msgU = msgU.replaceAll("(&([a-f0-9]))", "\u00A7$2");
            broadcastMessage(msgU);
            broadcastMessage(msg);
            broadcastMessage(msgU);
            is_message_displayed = true;
        }
    }

}
