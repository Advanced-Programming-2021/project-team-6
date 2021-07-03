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
        dataOutputStream.writeUTF("user create -u " + username + " -p " + password + " -n " + nickname);
        System.out.println(username + " "+  nickname+ " " + password);
        dataOutputStream.flush();
        return dataInputStream.readUTF();
    }

    public static String login(String username, String password) throws Exception {
        dataOutputStream.writeUTF("user login -u " + username + " -p " + password);
        dataOutputStream.flush();
        return dataInputStream.readUTF();
    }

    public static String logout() throws IOException {
        dataOutputStream.writeUTF("logout " + ClientController.token);
        dataOutputStream.flush();

        String r = dataInputStream.readUTF();
        return r;
    }


}
