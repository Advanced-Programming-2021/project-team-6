package controller;

import view.menus.MainMenuView;

public class ServerMessageHandler {

   public static String getServerMessage(String message) {
        System.out.println("A Message Came From Central Server : " + message);
        if (message.startsWith("GameOn")) {
            String[] params = message.split(" ");
           return MainMenuView.startNewGame(params[0] , params[2] , params[1] , params[3]);
        }
        return "";
    }
}

