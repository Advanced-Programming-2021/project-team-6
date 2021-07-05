package controller;

public class ServerMessageHandler {

    public static String getServerMessage(String message) {
        System.out.println("A Message Came From Central Server : " + message);
        switch (message) {
            case "GameOn" :
                startGameScene();
                break;
        }
    }
}
