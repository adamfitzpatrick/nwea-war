package io.stepinto.war.player;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author adam.fitzpatrick
 */
public class PlayerList {

    @Getter
    public List<Player> players;

    public PlayerList() {
        players = new ArrayList<>();
    }

    public int playAll() {
        return playList(players);
    }

    public void resetPlays() {
        players.stream().forEach(player -> player.resetPlay());
    }

    public int playList(List<Player> playersToPlay) {
        return playersToPlay.stream()
                .map(player -> {
                    player.play();
                    if (player.getCurrentPlay() != null) {
                        return 1;
                    }
                    return 0;
                }).reduce(0, (count, card) -> count + card);
    }

    public List<Player> getRoundLeaders() {
        List<Player> currentPlayers = players.stream()
                .filter(player -> player.getCurrentPlay() != null)
                .sorted(Comparator.comparing(Player::getCurrentPlay).reversed()).collect(Collectors.toList());
        List<Player> leaders = new ArrayList<>();

        for (Player player: currentPlayers) {
            if (leaders.size() == 0 || leaders.get(0).getCurrentPlay().compareTo(player.getCurrentPlay()) == 0) {
                leaders.add(player);
            }
        }

        return leaders;
    }

    public int availablePlayers() {
        return players.stream().filter(player -> player.getHand().size() > 0).collect(Collectors.toList()).size();
    }

    public Player getPointsLeader() {
        return players.stream()
                .sorted(Comparator.comparing(Player::getCardsWon).reversed())
                .collect(Collectors.toList())
                .get(0);
    }
}
