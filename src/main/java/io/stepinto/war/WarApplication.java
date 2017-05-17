package io.stepinto.war;

import io.stepinto.war.game.GameRunner;
import io.stepinto.war.ui.UIService;

import java.util.Scanner;

public class WarApplication {

    public static void main(String[] args) {
        new GameRunner(new UIService(new Scanner(System.in))).runGames();
    }
}
