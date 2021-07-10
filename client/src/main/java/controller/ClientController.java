package controller;

import javafx.application.Platform;
import view.menus.Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.LogManager;


public class ClientController {


    public static String username;
    public static String opponentUsername;
    public static String token;
    private static Socket socket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    public static void initializeNetwork() {
        try {
            socket = new Socket("localhost", 7777);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String sendMessage(String message) throws IOException {
        dataOutputStream.writeUTF(message);
        System.out.println("message sent to server : " + message);
        dataOutputStream.flush();
        String response = dataInputStream.readUTF();
        System.out.println("response from server : " + response);
        return response;
    }

    public static String createUser(String username, String password, String nickname) throws Exception {
        return sendMessage("user create -u " + username + " -p " + password + " -n " + nickname);
    }

    public static String login(String username, String password) throws Exception {
        String result = sendMessage("user login -u " + username + " -p " + password);
        String portString = result.split(": ")[2];
        String realResult = result.split(": ")[0] + ": " + result.split(": ")[1];
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(Integer.parseInt(portString));
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                while (true) {
                    String message = dataInputStream.readUTF();
                    System.out.println("New Message From Central Server : " + message);
                    Platform.runLater(() -> {
                        if (message.endsWith("true")) {
                            try {
                                dataOutputStream.writeUTF(ServerMessageHandler.getServerMessage(message));
                            } catch (Exception ioException) {
                                ioException.printStackTrace();
                            }
                        } else {
                            try {
                                ServerMessageHandler.getServerMessage(message);
                            } catch (Exception ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    });

                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


        }).start();
        return realResult;
    }

    public static String logout() throws IOException {
        return sendMessage("logout " + token);
    }


    public static String scoreboard() throws IOException {
        return sendMessage("scoreboard " + token);
    }

    public static String waitForNewGame() throws IOException {
        return sendMessage("new three-rounded " + token);
    }

    public static String profile() throws IOException {
        return sendMessage("profile " + token);
    }

    public static String changePassword(String oldPassword, String newPassword) throws IOException {
        return sendMessage("change password " + oldPassword + " " + newPassword + " " + token);
    }

    public static String changeNickname(String newNickname) throws IOException {
        return sendMessage("change nickname " + newNickname + " " + token);
    }

    public static String buyCard(String card, boolean justWantToCheck) throws IOException {
        String command = (justWantToCheck) ? "can buy " : "buy ";
        return sendMessage("shop " + command + card + " " + token);
    }

    public static String loadAllCards() throws IOException {
        return sendMessage("shop show --all");
    }

    public static String showMoney() throws IOException {
            return sendMessage("shop show money " + token);
    }

    public static String increaseMoney(String amount) throws IOException {
        return sendMessage("increase -m " + amount + " " + token);
    }

    public static String importCard(String cardName) throws IOException {
        return sendMessage("import card " + cardName + " " + token);
    }

    public static String exportCard(String cardName) throws IOException {
        return sendMessage("export card " + cardName + " " + token);
    }

    public static String getDescription(String cardName) throws IOException {
        return sendMessage("get description " + cardName);
    }

    public static String cancelGameRequest() throws IOException {
        return sendMessage("cancel game" + " " + token);
    }

    public static String createMonsterCard(String name, String attackPower, String defencePower, String description,
                                           String level, String action, String price) throws IOException {
        return sendMessage("create monster card " + name + " " + attackPower + " " + defencePower + " " + action + " " + level + " "
                + description + " " + price + " " + token);
    }

    public static String createDeck(String name) throws IOException {
        return sendMessage("deck create " + name + " " + token);
    }

    public static String createSpellCard(String name, String description, String action, String price) throws IOException {
        return sendMessage("create spell card " + name + " \""
                + description + "\" " + token + " \"" + action + "\" " + price);
    }

    public static String deleteDeck(String name) throws IOException {
        return sendMessage("deck delete " + name);
    }

    public static String activateDeck(String name) throws IOException {
        return sendMessage("deck set-activate " + name + " " + token);
    }

    public static String addCardToSideDeck(String name, String card) throws IOException {
        return sendMessage("deck add-card -c " + card + " -d " + name + " -s " + token);
    }

    public static String addCardToMainDeck(String name, String card) throws IOException {
        return sendMessage("deck add-card -c " + card + " -d " + name + " " + token);
    }

    public static String removeCardFromSideDeck(String name, String card) throws IOException {
        return sendMessage("deck rm-card -c " + card + " -d " + name + " -s " + token);
    }

    public static String removeCardFromMainDeck(String name, String card) throws IOException {
        return sendMessage("deck rm-card -c " + card + " -d " + name + " " + token);
    }

    public static String showAllDecks() throws IOException {
        return sendMessage("deck show -a " + token);
    }

    public static String showSideDeck(String name) throws IOException {
        return sendMessage("deck show -d " + name + " -s " + token);
    }

    public static String showMainDeck(String name) throws IOException {
        return sendMessage("deck show -d " + name + " " + token);
    }

    public static String summon(String address) throws IOException {
        return sendMessage("summon " + address + " " + Game.duelId + " " + token);
    }

    public static String setMonster(String address) throws IOException {
        return sendMessage("set monster " + address + " " + token);
    }

    public static String setSpellAndTrap(String address) throws IOException {
        return sendMessage("set spell/trap " + address + " " + token);
    }

    public static String cheatWin() throws IOException {
        return sendMessage("duel set-winner " + token);
    }

    public static String cheatLP() throws IOException {
        return sendMessage("increase --LP " + Game.duelId + " " + token);
    }
    public static String inactiveCards() throws IOException{
        return sendMessage("get inactive cards "+ token);
    }
    public static String changePhase() throws IOException {
        return sendMessage("change phase " + token);
    }
    public static String changeTurn() throws IOException {
        return sendMessage("change turn " + token);
    }

}
