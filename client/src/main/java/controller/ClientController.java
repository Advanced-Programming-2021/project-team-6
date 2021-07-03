package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


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

    public static String createUser(String username, String password, String nickname) throws Exception {
        return sendMessage(token + ":" + "user create -u " + username + " -p " + password + " -n " + nickname);
    }

    public static String login(String username, String password) throws Exception {
        return sendMessage(token + ":" +"user login -u " + username + " -p " + password);
    }

    public static String logout() throws IOException {
        return sendMessage(token + ":" +"logout " + ClientController.token);
    }
     private static String sendMessage(String message) throws IOException {
         dataOutputStream.writeUTF(message);
         System.out.println("message sent to server : " + message);
         dataOutputStream .flush();
         String response = dataInputStream.readUTF();
         System.out.println("response from server : " + response);
         return response;
     }


}
