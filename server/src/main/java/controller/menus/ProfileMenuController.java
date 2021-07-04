package controller.menus;

import controller.ErrorChecker;
import models.Database;
import models.Player;
import serverConection.Output;

import java.util.jar.Manifest;

public class ProfileMenuController {

    private ProfileMenuController() {
    }

    private static ProfileMenuController instance;

    public static ProfileMenuController getInstance() {
        if (instance == null)
            instance = new ProfileMenuController();
        return instance;
    }

    public String changeNickname(String token, String nickName) {
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null)
            return "Error";
        if (ErrorChecker.doesNicknameExist(nickName))
            return "Error: user with this nickname " + nickName + " is already exists";


        player.setNickname(nickName);
        Database.getInstance().getPlayerByUsername(player.getUsername()).setNickname(nickName);
        return "Success: nickname changed successfully!";
    }

    public String changePassword(String token, String oldPassword, String newPassword) {
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null) return "Error";

        if (!ErrorChecker.isPasswordCorrect(player, oldPassword))
            return "Error: current password is invalid";


        if (ErrorChecker.doesOldPassEqualsNewPass(oldPassword, newPassword))
            return "Error: please enter a new password";


        player.setPassword(newPassword);
        return "Success: password changed successfully!";
    }

    public String getProfile(String token) {
        if (!MainMenuController.getInstance().loggedInUsers.containsKey(token))
            return "Error";
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        return "Success " + player.getUsername() + " " + player.getNickname() + " " + player.getPicture();
    }
}
