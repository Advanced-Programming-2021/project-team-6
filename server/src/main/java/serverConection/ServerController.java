package serverConection;

import controller.menus.*;
import models.Database;
import models.Scoreboard;
import models.cards.Card;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerController {

    private static HashMap<String, Socket> socketHashMap = new HashMap<>();
    private static final String[] regexes = {
            "^user create (--username|-u) (?<username>\\w+) (--password|-p) (?<password>\\w+) (--nickname|-n) (?<nickname>\\w+)$",
            "^user login (--username|-u) (?<username>\\w+) (--password|-p) (?<password>\\w+)$",
            "^logout (?<token>\\S+)$",
            "^new three-rounded (?<token>\\S+)$",
            "^new one-rounded (?<token>\\S+)$",
            "^scoreboard (?<token>\\S+)$",
            "^profile (?<token>\\S+)$",
            "^change password (?<oldPassword>\\w+) (?<newPassword>\\w+) (?<token>\\S+)$",
            "^change nickname (?<newNickname>\\w+) (?<token>\\S+)$",
            "^shop buy (?<cardName>.+) (?<token>\\S+)$",
            "^shop show --all$",
            "shop show money (?<token>\\S+)$",
            "^increase (--money|-m) (?<amount>\\d+) (?<token>\\S+)",
            "^import card (?<name>\\S+) (?<token>S++)$",
            "^export card (?<name>\\S+) (?<token>S++)$",
            "^get description (?<name>.+)$",
            "^shop can buy (?<cardName>.+) (?<token>\\S+)$",
            "^cancel game (?<token>\\S+)$",
            "^create card (?<name>.+) (?<attack>\\d+) (?<defence>\\d+) (?<level>\\d+) (?<description>.+) (?<token>\\S+)$",
    };

    public static void registerSocket(Socket socket, String token) {
        socketHashMap.put(token, socket);
    }

    public static void getInputFromClient(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws Exception {
        while (true) {
            String command = dataInputStream.readUTF();
            System.out.println("message from " + command);
            String result = processCommand(command);
            System.out.println("response set :" + result);
            if (result.equals("")) return;
            dataOutputStream.writeUTF(result);
            dataOutputStream.flush();
        }
    }

    private static String processCommand(String command) throws Exception {
        Matcher commandMatcher;
        int whichCommand;
        for (whichCommand = 0; whichCommand < regexes.length; whichCommand++) {
            commandMatcher = findMatcher(command, regexes[whichCommand]);
            if (commandMatcher.find())
                return executeCommands(commandMatcher, whichCommand);

        }
        System.out.println(1);
        return "";

    }

    public static String sendMessageToSocket(String token, String message, boolean isResponseNeeded) {
        message = message + " " + isResponseNeeded;
        Socket socket = socketHashMap.get(token);
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(message);
            if (isResponseNeeded)
                return dataInputStream.readUTF();
            return "";
        } catch (Exception ignored) {
            return "Error";
        }
    }

    private static String executeCommands(Matcher commandMatcher, int witchCommand) throws Exception {
        switch (witchCommand) {
            case 0:
                return RegisterMenuController.getInstance().createUser(commandMatcher.group("username"),
                        commandMatcher.group("nickname"), commandMatcher.group("password"));
            case 1:
                String result = RegisterMenuController.getInstance().login(commandMatcher.group("username"), commandMatcher.group("password"));
                String token = result.split(": ")[1];
                int port = RegisterMenuController.nextPort(token);
                return result + ": " + port;
            case 2:
                return MainMenuController.getInstance().logout(commandMatcher.group("token"));
            case 3:
                return MainMenuController.getInstance().registerOnGame(true, commandMatcher.group("token"));
            case 4:
                return MainMenuController.getInstance().registerOnGame(false, commandMatcher.group("token"));
            case 5:
                return Scoreboard.getInstance().showScoreboard(commandMatcher.group("token"));
            case 6:
                return ProfileMenuController.getInstance().getProfile(commandMatcher.group("token"));
            case 7:
                return ProfileMenuController.getInstance().changePassword(commandMatcher.group("token"),
                        commandMatcher.group("oldPassword"), commandMatcher.group("newPassword"));
            case 8:
                return ProfileMenuController.getInstance().changeNickname(commandMatcher.group("token"),
                        commandMatcher.group("newNickname"));
            case 9:
                String cardName = commandMatcher.group("cardName");
                return (ShoppingMenuController.getInstance().buyCard(commandMatcher.group("token"), cardName , false));
            case 10:
                return (ShoppingMenuController.getInstance().showAllCard());
            case 11:
                return (ShoppingMenuController.getInstance().showMoney("playerLoggedIn"));
            case 12:
                return ShoppingMenuController.getInstance().increaseMoney("playerLoggedIn", Integer.parseInt(commandMatcher.group("amount")));
            case 13:
                return ImpExpMenuController.getInstance().importFromFile(commandMatcher.group("name"), commandMatcher.group("token"));
            case 14:
                return ImpExpMenuController.getInstance().exportToFile(commandMatcher.group("name"), commandMatcher.group("token"));
            case 15:
                Card card = Database.getInstance().getCardByName(commandMatcher.group("name"));
                return card.getName() + "\n" + card.getPrice() + "\n" + card.getDescription();
            case 16:
                cardName = commandMatcher.group("cardName");
                return (ShoppingMenuController.getInstance().buyCard(commandMatcher.group("token"), cardName , true));
            case 17:
                return MainMenuController.getInstance().cancelGame(commandMatcher.group("token"));
            case 18:
                return CreateCardMenuController.getInstance().createCard(commandMatcher.group("name"), commandMatcher.group("attack"),
                        commandMatcher.group("defence"), commandMatcher.group("level"),
                        commandMatcher.group("description"), commandMatcher.group("token"));
        }
        return "";
    }

    private static Matcher findMatcher(String input, String regex) {

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }

}
