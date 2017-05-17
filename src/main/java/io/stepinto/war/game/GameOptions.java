package io.stepinto.war.game;

import io.stepinto.war.player.Player;
import io.stepinto.war.player.PlayerList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author adam.fitzpatrick
 */
public class GameOptions {

    @Getter
    @Setter
    public static boolean playingAgain;

    @Getter
    @Setter
    public static int playerCount;

    @Getter
    @Setter
    public static PlayerList globalPlayerList = new PlayerList();

    @Getter
    @Setter
    public static PlayerList nextGamePlayers;

    public static void updateGlobalList() {
        nextGamePlayers.getPlayers().stream().forEach(GameOptions::addUniqueGlobalPlayer);
    }

    public static List<Player> getAvailableGlobalPlayers() {
        return globalPlayerList.getPlayers().stream()
                .filter(player -> nextGamePlayers.getPlayers().indexOf(player) == -1)
                .collect(Collectors.toList());
    }

    public static void reset() {
        nextGamePlayers = new PlayerList();
    }

    private static void addUniqueGlobalPlayer(Player player) {
        List<Player> matchingPlayers = globalPlayerList.getPlayers().stream()
                .filter(globalPlayer -> globalPlayer.getName().equals(player.getName()))
                .collect(Collectors.toList());
        if (matchingPlayers.size() == 0) {
            globalPlayerList.getPlayers().add(player);
        }
    }
}
