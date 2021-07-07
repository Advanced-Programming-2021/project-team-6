package controller;

import view.menus.MainMenuView;

import java.io.IOException;

public class ServerMessageHandler {

   public static String getServerMessage(String message) throws IOException {
        System.out.println("A Message Came From Central Server : " + message);
        if (message.startsWith("GameOn")) {
            String[] params = message.split(" ");
           return MainMenuView.startNewGame(params[1] , params[3] , params[2] , params[4]  , params[5] , params[6] , Integer.parseInt(params[7]), true);
        }
        return "";
    }
}

