package controller.menus;

import models.Database;
import controller.ErrorChecker;
import models.Player;
import serverConection.ServerController;

import java.net.Socket;
import java.util.UUID;


public class RegisterMenuController {

    public static int nextPort = 1025;
    private RegisterMenuController() {
    }

    private static RegisterMenuController instance;

    public static RegisterMenuController getInstance() {
        if (instance == null)
            instance = new RegisterMenuController();
        return instance;
    }

    public static int nextPort(String token) {
        new Thread(() -> {
            boolean connected = false;
            int port = nextPort - 1;
                while (!connected) {
                    Socket socket;
                    try {
                        socket = new Socket( "127.0.0.1", port);
                    } catch (Exception ignored) {
                        continue;
                    }
                    ServerController.registerSocket(socket , token);
                    connected = true;
                }
        }).start();
        return nextPort++;
    }

    public String createUser(String username, String nickname, String password) {
        if (ErrorChecker.doesUsernameExist(username))
            return "Error: user with username " + username + " already exists";
        if (ErrorChecker.doesNicknameExist(nickname))
            return "Error: user with nickname " + nickname + " already exists";


        new Player(username, nickname, password);
        return "Success: user created successfully!";
    }

    public String login(String username, String password) {

        if (!ErrorChecker.doesUsernameExist(username))
            return "username and password didn't match!";

        Player player = Database.getInstance().getPlayerByUsername(username);
        if (!ErrorChecker.isPasswordCorrect(player, password))
            return "username and password didn't match!";

        MainMenuController.getInstance().setPlayerLoggedIn(player);
        String token = UUID.randomUUID().toString();
        MainMenuController.getInstance().loggedInUsers.put(token, player);
        Database.getInstance().getPlayerByUsername(username).setToken(token);
        return "Success: " + token;
    }
}
