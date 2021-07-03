package controller.menus;

import models.Player;

import java.util.HashMap;

public class MainMenuController {


    private static MainMenuController instance;
    private Player playerLoggedIn;
    public HashMap<String, Player> loggedInUsers = new HashMap<>();

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


}
