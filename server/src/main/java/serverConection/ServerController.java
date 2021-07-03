package serverConection;

import controller.menus.MainMenuController;
import controller.menus.RegisterMenuController;
import models.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerController {

    private static final String[] regexes = {
            "^user create (--username|-u) (?<username>\\w+) (--password|-p) (?<password>\\w+) (--nickname|-n) (?<nickname>\\w+)$",
            "^user login (--username|-u) (?<username>\\w+) (--password|-p) (?<password>\\w+)$",
            "^logout (<?token> \\S+)$"

    };

    public static void getInputFromClient(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException, IOException {
        while (true) {
            String command = dataInputStream.readUTF();
            System.out.println("message from " + command);
            String message = command.split(":")[1];
            String result = processCommand(message);
            if (result.equals("")) return;
            dataOutputStream.writeUTF(result);
            dataOutputStream.flush();
        }
    }

    private static String processCommand(String command){
        Matcher commandMatcher;
        int whichCommand;
        for (whichCommand = 0; whichCommand < regexes.length; whichCommand++) {
            commandMatcher = findMatcher(command, regexes[whichCommand]);
            if (commandMatcher.find())
                return executeCommands(commandMatcher, whichCommand);

        }
        return "";

    }

    private static String executeCommands(Matcher commandMatcher, int witchCommand){
        switch (witchCommand){
            case 0:
                return RegisterMenuController.getInstance().createUser(commandMatcher.group("username"),
                        commandMatcher.group("nickname"), commandMatcher.group("password"));
            case 1:
                return RegisterMenuController.getInstance().login(commandMatcher.group("username"), commandMatcher.group("password"));
            case 2:
                return MainMenuController.getInstance().logout(commandMatcher.group("token"));
        }
        return "";
    }

    private static Matcher findMatcher(String input, String regex) {

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }

}
