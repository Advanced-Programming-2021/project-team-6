package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;


public class ClientController {

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
        dataOutputStream .flush();
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
                    System.out.println(dataInputStream.readUTF());
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


}
