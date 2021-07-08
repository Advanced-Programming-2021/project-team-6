package controller;

import view.menus.Game;
import view.menus.MainMenuView;

import java.io.IOException;

public class ServerMessageHandler {

    public static String getServerMessage(String message) throws IOException, InterruptedException {
        System.out.println("A Message Came From Central Server : " + message);
        String[] params = message.split(" ");
        if (message.startsWith("GameOn")) {
            return MainMenuView.startNewGame(params[1], params[3], params[2], params[4], params[5], params[6], Integer.parseInt(params[7]), true);
        } else if (message.startsWith("draw-p")) {
            int howManyCards = Integer.parseInt(params[1]);
            for (int i = 0; i < howManyCards; i++) {
                String[] cardNames = message.substring(9).split(",");
                Game.drawCardForPlayer(cardNames[i]);
            }
        } else if (message.startsWith("draw-o")) {
            int howManyCards = Integer.parseInt(params[1]);
            for (int i = 0; i < howManyCards; i++)
                Game.drawCardForOpponent();
        }
        return "";
    }
}

