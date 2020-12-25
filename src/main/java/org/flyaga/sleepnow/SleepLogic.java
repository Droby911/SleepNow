package org.flyaga.sleepnow;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitScheduler;
import static org.bukkit.Bukkit.*;

public class SleepLogic {
    public static int current_sleeping_players_count; // тут будет число игроков, которые на данный момент спят
    public static double players_count_to_skip; // тут будет число игроков, необходимое для скипа ночи
    public static List<LivingEntity> current_sleeping_players_entity = new ArrayList<>(); // тут будет список игроков, которые спят в кровати
    public static List<Player> players; // тут будет список игроков в онлайне
    public static World world = getServer().getWorld("world"); // тут получаем мир по его названию
    public static LivingEntity playerEnt; // объявляем энтити, этим энтити будет игрок
    public static final int ToEightPlayersDivision = 3; // берем всех игроков на сервере -> если их ДО ВОСЬМИ ЧЕЛОВЕК, то делим их на число из этой переменной, и в результате деления получим число игроков, необходимых для скипа
    public static final long delay = 20L; // задержка плагина (в тиках)

    public static void logicStart() {
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(SleepNow.getInstance(), new Runnable() { // делаем что-то типа while(true) с задержкой
            @Override
            public void run() {
                players = world.getPlayers(); // получаем всех игроков в онлайне в список с типом Player (не путать с LivingEntity)
                long time = world.getTime(); // получаем время в мире
                boolean isDay = time < 12300 || time > 23850; // условия для определения что есть день, а что есть ночь
                if (isDay == false) { // если в мире ночь
                    playerManipulation(); // то начинаем работу
                }
            }
        }, 0L, delay); // вызываем метод run() каждые *delay* тиков
}


    public static void playerManipulation(){
        if (players.size() >= 4 && players.size() <= 8) { // если игроков больше или равно 4, и меньше или равно 8
            if (current_sleeping_players_entity != null) { // если список спящих (на данный момент) игроков НЕ пуст
                for (LivingEntity sleeping : current_sleeping_players_entity) { // циклом преобразуем список энтити в один энтити
                    boolean isSleeping = sleeping.isSleeping();
                    if (isSleeping == false) { // если данный энтити (а конкретно игрок) не спит
                        current_sleeping_players_count = -1; // убираем его из счетчика спящих
                        current_sleeping_players_entity.remove(sleeping); // убираем его из списка спящих энтити
                    }
                }
            }
            for (Player players_for : players) { // циклом преобразуем список игроков в одного игрока
                playerEnt = players_for; // тут один игрок
                boolean isSleeping = playerEnt.isSleeping();
                if (isSleeping == true) { // если он спит
                    if (!current_sleeping_players_entity.isEmpty()) { // и список из спящих игроков НЕ пуст
                        for (LivingEntity sleeping : current_sleeping_players_entity) { // циклом преобразуем список спящих энтити в один энтити
                            if (playerEnt.getName().equals(sleeping.getName())) { // если ник игрока равен нику спящего энтити
                                return; // тогда уходим
                            }
                        }
                    }
                    updateAndSet(playerEnt, ToEightPlayersDivision);
                }
            }
        }
        else{ // а иначе, если на сервере мало игроков, то и вовсе этого одного игрока хватит для скипа ночи
            for(Player players_for : players) {
                updateSingleSleeping(players_for);
            }
        }
    }

    public static void updateAndSet(LivingEntity playerEnt, int PlayersDivision){ // PlayersDivision - определяется в зависимости от того, сколько игроков на сервере
        current_sleeping_players_entity.add(playerEnt);
        current_sleeping_players_count = + 1;
        players_count_to_skip = Math.floor(players.size() / PlayersDivision); // делим количество игроков на PlayersDivision и округляем результат в МЕНЬШУЮ сторону
        if(current_sleeping_players_count >= (int)players_count_to_skip){ // если количество спящих игроков больше или равно количеству игроков, необходимых для скипа
            world.setTime(0);
            current_sleeping_players_count = 0;
            players_count_to_skip = 0;
            displayMessage(playerEnt, 2);
        }
        else{ // а если игрок в кровати, но все равно игроков не хватает для скипа, то...
            players_count_to_skip = players_count_to_skip - 1; // отнимаем 1 от количества игроков необходимых для скипа (так как один из игроков уже в кровати)
            displayMessage(playerEnt, 1);
        }
    }

    public static void updateSingleSleeping(LivingEntity playerEnt){ // когда хватает и одного игрока для скипа
        boolean isSleeping = playerEnt.isSleeping();
        if(isSleeping == true)

        {
            displayMessage(playerEnt, 2);
            world.setTime(0);
        }
    }

    public static void displayMessage(LivingEntity playerEnt, int what_display){
        if(what_display == 1){
            broadcastMessage("Игрок" + " " + playerEnt.getName() + " " + "лег в кровать. Для скипа ночи нужно еще" + " " + (int)players_count_to_skip + " " + "игроков.");
        }
        if(what_display == 2){
            broadcastMessage("Игрок" + " " + playerEnt.getName() + " " + "спит, скипаем ночь...");
        }
    }

}
