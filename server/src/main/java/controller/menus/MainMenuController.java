package controller.menus;

import models.Player;
import serverConection.ServerController;

import java.util.HashMap;
import java.util.SplittableRandom;

public class MainMenuController {


    private static MainMenuController instance;
    private Player playerLoggedIn;
    public HashMap<String, Player> loggedInUsers = new HashMap<>();
    HashMap<String , Boolean> waitingLobby = new HashMap<>();

    private MainMenuController() {
    }

    public static MainMenuController getInstance() {
        if (instance == null)
            instance = new MainMenuController();
        return instance;
    }

    public Player getPlayerLoggedIn() {
        return playerLoggedIn;
    }

    public void setPlayerLoggedIn(Player playerLoggedIn) {
        this.playerLoggedIn = playerLoggedIn;
    }

    public String logout(String token){
        if (!loggedInUsers.containsKey(token))
            return "Error";
        loggedInUsers.remove(token);
        return "Success";
    }


    public String registerOnGame(boolean isThreeRounded , String token) {
            for (String waitingClientToken : waitingLobby.keySet()) {
                if (!token.equals(waitingClientToken)&& waitingLobby.get(waitingClientToken) == isThreeRounded) {
                    ServerController.sendMessageToSocket(waitingClientToken, "GameOn", false);
                    return "GameOn";
                }
            }
            waitingLobby.put(token , isThreeRounded);
            return "Successful";

    }
}
