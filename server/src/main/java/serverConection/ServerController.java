package serverConection;

import controller.menus.*;
import models.Database;
import models.Messenger;
import models.Player;
import models.Scoreboard;
import models.cards.Card;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerController {

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
            "^import card (?<name>\\S+) (?<token>\\S++)$",
            "^export card (?<name>\\S+) (?<token>\\S++)$",
            "^get description (?<name>.+)$",
            "^shop can buy (?<cardName>.+) (?<token>\\S+)$",
            "^cancel game (?<token>\\S+)$",
            "^create monster card (?<name>.+) (?<attack>\\d+) (?<defence>\\d+) (?<action>.+) (?<level>\\d+) (?<description>.+) (?<price>\\d+) (?<token>\\S+)$",
            "^create spell card (?<name>\\w+) \"(?<description>.+)\" (?<token>\\S+) \"(?<action>.+)\" (?<price>\\d+)$",
            "^deck create (?<name>\\w+) (?<token>\\S+)$",
            "^deck delete (?<name>\\w+)$",
            "^deck set-activate (?<name>\\w+) (?<token>\\S+)$",
            "^deck add-card (?:--card|-c) (?<cardName>.+) (?:--deck|-d) (?<deckName>.+) (?:--side|-s) (?<token>\\S+)$",
            "^deck add-card (?:--card|-c) (?<cardName>.+) (?:--deck|-d) (?<deckName>.+) (?<token>\\S+)$",
            "^deck rm-card (?:--card|-c) (?<cardName>.+) (?:--deck|-d) (?<deckName>.+) (?:--side|-s) (?<token>\\S+)$",
            "^deck rm-card (?:--card|-c) (?<cardName>.+) (?:--deck|-d) (?<deckName>.+) (?<token>\\S+)$",
            "^deck show -a (?<token>\\S+)$",
            "^deck show -d (?<deckName>.+) -s (?<token>\\S+)$",
            "^deck show -d (?<deckName>.+) (?<token>\\S+)$",
            "^summon (?<address>\\d+) (?<token>\\S+)$",
            "^set monster (?<address>\\d+) (?<token>\\S+)$",
            "^set spell/trap (?<address>\\d+) (?<token>\\S+)$",
            "^set -p (?<mode>attack|defence) (?<address>\\d+) (?<token>\\S+)$",
            "^duel set-winner (?<token>\\S+)$",
            "^increase --LP (?<duelID>\\S+) (?<myToken>\\S+)$",
            "^get inactive cards (?<token>\\S+)$",
            "^change phase (?<token>\\S+)$",
            "^change turn (?<token>\\S+)$",
            "^submission (?<token>\\S+)",
            "^get all messages (?<token>\\S+)$",
            "^send message \"(?<message>.+)\" (?<token>\\S+)$",
            "^attack from (?<fromCard>\\d+) to (?<toCard>\\d+) by (?<token>\\S+)$",
            "^attack direct from (?<fromCard>\\d+) by (?<token>\\S+)$",

    };
    private static HashMap<String, Socket> socketHashMap = new HashMap<>();

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
        return "";

    }

    public static String sendMessageToSocket(String token, String message, boolean isResponseNeeded) {
        message = message + " " + isResponseNeeded;
        Socket socket = socketHashMap.get(token);
        try {
            System.out.println("message sent to " + Database.getInstance().getPlayerByToken(token).getUsername() + " :" + message);
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
                return (ShoppingMenuController.getInstance().buyCard(commandMatcher.group("token"), cardName, false));
            case 10:
                return (ShoppingMenuController.getInstance().showAllCard());
            case 11:
                return (ShoppingMenuController.getInstance().showMoney(commandMatcher.group("token")));
            case 12:
                return ShoppingMenuController.getInstance().increaseMoney(commandMatcher.group("token"), Integer.parseInt(commandMatcher.group("amount")));
            case 13:
                return ImpExpMenuController.getInstance().importFromFile(commandMatcher.group("name"), commandMatcher.group("token"));
            case 14:
                return ImpExpMenuController.getInstance().exportToFile(commandMatcher.group("name"), commandMatcher.group("token"));
            case 15:
                Card card = Database.getInstance().getCardByName(commandMatcher.group("name"));
                return card.getName() + "\n" + card.getPrice() + "\n" + card.getDescription();
            case 16:
                cardName = commandMatcher.group("cardName");
                return (ShoppingMenuController.getInstance().buyCard(commandMatcher.group("token"), cardName, true));
            case 17:
                return MainMenuController.getInstance().cancelGame(commandMatcher.group("token"));
            case 18:
                return CreateCardMenuController.createCard(commandMatcher.group("name"), commandMatcher.group("attack"),
                        commandMatcher.group("defence"), commandMatcher.group("level"),
                        commandMatcher.group("description"), commandMatcher.group("action"), commandMatcher.group("token"),
                        commandMatcher.group("price"), true);
            case 19:
                return CreateCardMenuController.createCard(commandMatcher.group("name"), "", "", "",
                        commandMatcher.group("description"), commandMatcher.group("action"), commandMatcher.group("token"),
                        commandMatcher.group("price"), false);
            case 20:
                return DeckMenuController.getInstance().createDeck(commandMatcher.group("name"), commandMatcher.group("token"));
            case 21:
                return DeckMenuController.getInstance().deleteDeck(commandMatcher.group("name"));
            case 22:
                return DeckMenuController.getInstance().setActiveDeck(commandMatcher.group("name"), commandMatcher.group("token"));
            case 23:
                return DeckMenuController.getInstance().addCardToDeck(commandMatcher.group("cardName"), commandMatcher.group("deckName"), commandMatcher.group("token"), false);
            case 24:
                return DeckMenuController.getInstance().addCardToDeck(commandMatcher.group("cardName"), commandMatcher.group("deckName"), commandMatcher.group("token"), true);
            case 25:
                return DeckMenuController.getInstance().removeCardFromDeck(commandMatcher.group("cardName"), commandMatcher.group("deckName"), commandMatcher.group("token"), false);
            case 26:
                return DeckMenuController.getInstance().removeCardFromDeck(commandMatcher.group("cardName"), commandMatcher.group("deckName"), commandMatcher.group("token"), true);
            case 27:
                return DeckMenuController.getInstance().showAllDecks(commandMatcher.group("token"));
            case 28:
                return DeckMenuController.getInstance().showDeck(commandMatcher.group("deckName"),
                        commandMatcher.group("token"), false);
            case 29:
                return DeckMenuController.getInstance().showDeck(commandMatcher.group("deckName"),
                        commandMatcher.group("token"), true);
            case 30:
                token = commandMatcher.group("token");
                Player player = Database.getInstance().getPlayerByToken(token);
                return DuelMenuController.getDuelById(player.getDuelID() + "")
                        .summon(commandMatcher.group("address"), token);
            case 31:
                token = commandMatcher.group("token");
                player = Database.getInstance().getPlayerByToken(token);
                return DuelMenuController.getDuelById(player.getDuelID() + "")
                        .setMonster(commandMatcher.group("address"), token);
            case 32:
                token = commandMatcher.group("token");
                player = Database.getInstance().getPlayerByToken(token);
                return DuelMenuController.getDuelById(player.getDuelID() + "")
                        .setSpellAndTrap(commandMatcher.group("address"), token);
            case 33:
                token = commandMatcher.group("token");
                player = Database.getInstance().getPlayerByToken(token);
                return DuelMenuController.getDuelById(player.getDuelID() + "")
                        .setPosition(commandMatcher.group("mode"), token, commandMatcher.group("address"));
            case 34:
                token = commandMatcher.group("token");
                player = Database.getInstance().getPlayerByToken(token);
                return DuelMenuController.getDuelById(String.valueOf(player.getDuelID()))
                        .cheatForWinGame(token,false);
            case 35:
                token = commandMatcher.group("myToken");
                player = Database.getInstance().getPlayerByToken(token);
                return DuelMenuController.getDuelById(String.valueOf(player.getDuelID()))
                        .increaseLP(token);
            case 36:
                return DeckMenuController.getInstance().showInactiveCards(commandMatcher.group("token"));
            case 37:
                token = commandMatcher.group("token");
                player = Database.getInstance().getPlayerByToken(token);
                return DuelMenuController.getDuelById(String.valueOf(player.getDuelID()))
                        .changePhase(token);
            case 38:
                token = commandMatcher.group("token");
                player = Database.getInstance().getPlayerByToken(token);
                return DuelMenuController.getDuelById(String.valueOf(player.getDuelID()))
                        .setSecondPlayerTurn();
            case 39:
                token = commandMatcher.group("token");
                player = Database.getInstance().getPlayerByToken(token);
                return DuelMenuController.getDuelById(String.valueOf(player.getDuelID()))
                        .cheatForWinGame(token,true);
            case 40:
                return Messenger.getInstance().getAllMessage(commandMatcher.group("token"));
            case 41:
                return Messenger.getInstance().addMessage(commandMatcher.group("token"),commandMatcher.group("message"));
            case 42:
                token = commandMatcher.group("token");
                player = Database.getInstance().getPlayerByToken(token);
                return DuelMenuController.getDuelById(String.valueOf(player.getDuelID()))
                        .attack(commandMatcher.group("fromCard"),commandMatcher.group("toCard"), token);
            case 43:
                token = commandMatcher.group("token");
                player = Database.getInstance().getPlayerByToken(token);
                return DuelMenuController.getDuelById(String.valueOf(player.getDuelID()))
                        .attackDirect(commandMatcher.group("fromCard"), token);

        }
        return "";
    }


    private static Matcher findMatcher(String input, String regex) {

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }

}
