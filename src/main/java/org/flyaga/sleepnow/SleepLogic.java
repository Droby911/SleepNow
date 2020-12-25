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
    public static int current_sleeping = 0; //
    public static List<LivingEntity> current_sleeping_players_entity = new ArrayList<>(); // тут будет список игроков, которые спят в кровати
    public static List<Player> players; // тут будет список игроков в онлайне
    public static World world = getServer().getWorld("world"); // тут получаем мир по его названию
    public static LivingEntity playerEnt; // объявляем энтити, этим энтити будет игрок
    public static final int ToEightPlayersDivision = 2; // берем всех игроков на сервере -> если их ДО ВОСЬМИ ЧЕЛОВЕК, то делим их на число из этой переменной, и в результате деления получим число игроков, необходимых для скипа
    public static long delay = 20L; // задержка плагина (в тиках)

    public static void logicStart() {
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(SleepNow.getInstance(), new Runnable() { // делаем что-то типа while(true) с задержкой
            @Override
            public void run() {
                long time = world.getTime(); // получаем время в мире
                boolean isDay = time < 12300 || time > 23850; // условия для определения что есть день, а что есть ночь
                if (isDay == false) { // если в мире ночь
                    playerManipulation(); // то начинаем работу
                }
            }
        }, 0L, delay); // вызываем метод run() каждые *delay* тиков
}


    public static void playerManipulation(){
        players = world.getPlayers(); // получаем всех игроков в онлайне в список с типом Player (не путать с LivingEntity)
        players_count_to_skip = Math.floor(players.size() / ToEightPlayersDivision);
        if (players.size() >= 3 && players.size() <= 8) { // если игроков больше или равно 4, и меньше или равно 8
            for (Player players_for : players) { // циклом преобразуем список игроков в одного игрока
                playerEnt = players_for; // тут один игрок
                boolean isSleeping = playerEnt.isSleeping();
                if (isSleeping == true) { // если он спит
                    if(current_sleeping_players_entity.contains(playerEnt)){
                        continue;
                    }
                    else{
                        current_sleeping_players_entity.add(playerEnt);
                        current_sleeping = current_sleeping + 1;
                        displayMessage(playerEnt, 1);
                        updateAndSet(playerEnt);
                    }
                }
                else if(isSleeping == false && current_sleeping_players_entity.contains(playerEnt)){
                    current_sleeping_players_entity.remove(playerEnt);
                    current_sleeping = current_sleeping - 1;
                }
            }
        }
        else { // а иначе, если на сервере мало игроков, то и вовсе этого одного игрока хватит для скипа ночи
            for (Player players_for : players) {
                updateSingleSleeping(players_for);
            }
        }
    }

    public static void updateAndSet(LivingEntity playerEnt){ // PlayersDivision - определяется в зависимости от того, сколько игроков на сервере
        if(current_sleeping >= (int)players_count_to_skip){ // если количество спящих игроков больше или равно количеству игроков, необходимых для скипа
            world.setTime(0);
            current_sleeping = 0;
            players_count_to_skip = 0;
            current_sleeping_players_entity.clear();
            players = world.getPlayers(); // получаем всех игроков в онлайне в список с типом Player (не путать с LivingEntity)
            players_count_to_skip = Math.floor(players.size() / ToEightPlayersDivision);
            displayMessage(playerEnt, 2);
        }
    }

    public static void updateSingleSleeping(LivingEntity playerEnt){ // когда хватает и одного игрока для скипа
        boolean isSleeping = playerEnt.isSleeping();
        if(isSleeping == true)

        {
            world.setTime(0);
            displayMessage(playerEnt, 2);
        }
    }

    public static void displayMessage(LivingEntity playerEnt, int what_display){
        if(what_display == 1){
            int to_skip = (int)players_count_to_skip - current_sleeping;
            if(to_skip > 0){
                broadcastMessage("Игрок" + " " + playerEnt.getName() + " " + "лег в кровать. Для скипа ночи нужно еще" + " " + to_skip + " " + "игроков.");
            }
        }
        if(what_display == 2){
            broadcastMessage("Игрок" + " " + playerEnt.getName() + " " + "спит, скипаем ночь...");
        }
    }

}
