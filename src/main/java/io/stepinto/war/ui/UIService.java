package io.stepinto.war.ui;

import io.stepinto.war.game.Game;
import io.stepinto.war.game.GameOptions;
import io.stepinto.war.player.Player;
import io.stepinto.war.player.PlayerList;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * @author adam.fitzpatrick
 */
public class UIService {

    private String intro = "\n\n***********      Welcome to WAR!      ***********\n"
            + "\n"
            + "This is very simple card game of pure chance which can be played by 2-6 players."
            + "\n"
            + "To play, the entire deck of cards is shuffled and is dealt out such that each player receives an equal "
            + "number of cards.  Any extra cards are discarded.\n"
            + "\n"
            + "Play begins with each player laying down one card.  The player with the highest face value collects "
            + "all cards in play and sets them aside.  When all cards have been played, each player counts his or "
            + "cards collected in battle.  The player with the most cards wins.\n"
            + "\n"
            + "In the case of a tie for the highest value card, the tied players will each lay down another card "
            + "face down, and then a third card face up.  The player with the highest value for this third card "
            + "wins the round.  Should another tie occur, this process repeats until the tie is resolved. "
            + "should a player run out of cards prior to the end of the game, they will no longer participate. "
            + "However, such a player may still win if they have accumulated more cards than their opponents.\n\n";
    private static String reportLineFormatString = "%-15s %-15s %-15d %-15d %-15d";

    private Scanner scanner;
    private String[] names;
    private String playFormatter;

    public UIService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setPlayerList(PlayerList playerList) {
        names = playerList.getPlayers().stream().map(Player::getName).toArray(String[]::new);
        playFormatter = "    ";
        for (String name: names) {
            playFormatter += "%-15s";
        }
    }

    public void showGameIntro() {
        System.out.println(intro);
    }

    public void writeGameFlavor() {
        System.out.println("\n\nShuffling deck...\n\nDealing cards...\n");
    }

    public void readPlayerCount() {
        int playerCount = 0;

        while(playerCount < 2 || playerCount > 6) {
            String input = readInput("Enter number of players (2-6): ");
            try {
                playerCount = Integer.parseInt(input);
                if (playerCount < 2 || playerCount > 6) {
                    System.out.println("Please enter a value from 2 to 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a value from 2 to 6.");
            }
        }

        GameOptions.playerCount = playerCount;
    }

    public String readPlayerName() {
        String playerName = "";
        while(playerName.length() == 0) {
            playerName = readInput("Enter player name: ");
        }
        return playerName;
    }

    public String readPlayerName(int player) {
        String playerName = "";
        while(playerName.length() == 0) {
            playerName = readInput(String.format("Enter player %s name: ", player));
        }
        return playerName;
    }

    public void listExistingPlayers() {
        System.out.println("\nSelect an existing player or type 0 to enter a new player:");
        List<Player> players = GameOptions.getAvailableGlobalPlayers();
        for (Player player: players) {
            System.out.println(Integer.toString(players.indexOf(player) + 1) + ". " + player.getName());
        }
        System.out.println("");
    }

    public void selectExistingPlayer() {
        int playerNumber = -1;

        while(playerNumber < 0) {
            String input = readInput("Enter player number: ");
            try {
                playerNumber = Integer.parseInt(input);
                if (playerNumber < 0 || playerNumber > GameOptions.getAvailableGlobalPlayers().size()) {
                    System.out.println("Please select a valid player.");
                    playerNumber = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid player.");
            }
        }

        if (playerNumber > 0) {
            GameOptions.getNextGamePlayers().getPlayers()
                    .add(GameOptions.getAvailableGlobalPlayers().get(playerNumber - 1));
        } else {
            GameOptions.getNextGamePlayers().getPlayers().add(new Player(this.readPlayerName()));
        }
    }

    public void advanceRound(Game game) {
        String input = readInput("Press enter for next round, or enter 'a' to auto-run.");

        if (input.equals("a")) {
            game.setAutoRun(true);
        }
    }

    public void writeRoundHeader(int round) {
        this.writeRoundHeader(round, null);
    }

    public void writeRoundHeader(int round, String winner) {
        String results = "Results:";
        if (winner == null) {
            results = "";
            winner = "";
        }
        String spacer = results.equals("") ? "" : "\n";
        System.out.println(
                String.format("%sRound %d %s %s\n----------------", spacer, round, results, winner.toUpperCase()));
    }

    public void writeLeaderBoard() {
        System.out.println(String.format("\n\n    %10s%s%10s\n    -------------------------------", "", "LEADERBOARD", ""));
        System.out.println(String.format("    %-16s%15s\n    %-16s%15s", "Name", "Games Won", "----", "---------"));
        GameOptions.getGlobalPlayerList().getPlayers().stream()
                .sorted(Comparator.comparing(player -> player.getGamesWon()))
                .forEach(player -> {
                    System.out.println(String.format("    %-16s%15d", player.getName(), player.getGamesWon()));
                });
        System.out.println("\n\n");
    }

    public boolean readPlayAgain() {
        String input = "q";
        while (!input.toLowerCase().substring(0, 1).equals("y") && !input.toLowerCase().substring(0, 1).equals("n")) {
            input = readInput("Play again? (y/n): ").concat("q").toLowerCase().substring(0, 1);
        }
        return input.equals("y");
    }

    public void writePlayHeaders() {
        System.out.println(String.format(playFormatter, (Object[]) names));
    }

    public void writePlay(PlayerList playerList, boolean tie) {
        writePlay(playerList, tie, false);
    }

    public void writePlay(PlayerList playerList, boolean tie, boolean facedown) {
        String[] values = playerList.getPlayers().stream().map(player -> player.getCurrentPlay().toString())
                .toArray(String[]::new);
        if (facedown) {
            for (int k = 0; k < playerList.getPlayers().size(); k++) {
                if (playerList.getPlayers().get(k).getCurrentPlay().getFace() == null) {
                    values[k] = "----";
                } else {
                    values[k] = "****";
                }
            }
        }
        String formatter = playFormatter + (tie ? "  <--- TIE!!" : "");
        System.out.println(String.format(formatter, (Object[]) values));
    }

    public void writeRoundResults(int round, PlayerList playerList, String winner) {
        this.writeRoundHeader(round, winner);
        System.out.println(String.format("%-15s %-15s %-15s %-15s %-15s", "Name", "Card", "Points", "Rounds Won", "Cards Remaining"));
        playerList.getPlayers().stream()
                .forEach(player -> {
                    System.out.println(String.format(reportLineFormatString,
                            player.getName(),
                            player.getCurrentPlay().toString(),
                            player.getCardsWon(),
                            player.getRoundsWon(),
                            player.getHand().size()
                    ));
                });
        System.out.println("\n");
    }

    private String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
