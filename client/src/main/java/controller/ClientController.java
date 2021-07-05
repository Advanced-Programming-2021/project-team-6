package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ClientController {

    public static String username;
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
                    if (message.endsWith("true"))
                        dataOutputStream.writeUTF(ServerMessageHandler.getServerMessage(message));
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
        return sendMessage("profile " + ClientController.token);
    }

    public static String changePassword(String oldPassword, String newPassword) throws IOException {
        return sendMessage("change password " + oldPassword + " " + newPassword + " " + ClientController.token);
    }

    public static String changeNickname(String newNickname) throws IOException {
        return sendMessage("change nickname " + newNickname + " " + ClientController.token);
    }

    public static String buyCard(String card) throws IOException {
        return sendMessage("shop buy " + card + " " + ClientController.token);
    }

    public static String loadAllCards() throws IOException {
        return sendMessage("shop show --all");
    }

    public static String showMoney() throws IOException {
        return sendMessage("shop show money " + ClientController.token);
    }

    public static String increaseMoney(String amount) throws IOException {
        return sendMessage("increase -m " + amount + " " + ClientController.token);
    }

    public static String importCard(String cardName) throws IOException {
        return sendMessage("import card " + cardName + " " + ClientController.token);
    }

    public static String exportCard(String cardName) throws IOException {
        return sendMessage("export card " + cardName + " " + ClientController.token);
    }
    public static String getDescription(String cardname) throws IOException {
        return sendMessage("get description "+ cardname);
    }

    public static String cancelGameRequest() throws IOException {
        return sendMessage("cancel game" + " " + ClientController.token);
    }

}
