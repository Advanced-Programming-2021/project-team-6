package controller.menus;

import models.Database;
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

    public  String cancelGame(String token) {
        waitingLobby.remove(token);
        return "Success";
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
                    waitingLobby.remove(waitingClientToken);
                    Player foundedOpponent = Database.getInstance().getPlayerByToken(waitingClientToken);
                    Player player = Database.getInstance().getPlayerByToken(token);
                    String response = "GameOn" + " " + foundedOpponent.getNickname() + " " + foundedOpponent.getPicture() + " " + player.getNickname() + " " +player.getPicture();
                    ServerController.sendMessageToSocket(waitingClientToken, response , false);
                    return response;
                }
            }
            waitingLobby.put(token , isThreeRounded);
            return "Success";

    }
}
