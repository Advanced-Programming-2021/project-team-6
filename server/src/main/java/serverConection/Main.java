package serverConection;

import models.Database;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Database.getInstance().loadingDatabase();
        runServer();
        Database.getInstance().updatingDatabase();

    }

    private static void runServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            while (true) {
                Socket socket = serverSocket.accept();
                createNewClient(serverSocket, socket);
            }
        } catch (Exception ignored) {

        }
    }

    private static void createNewClient(ServerSocket serverSocket, Socket socket) {
        new Thread(() -> {
            DataInputStream dataInputStream = null;
            try {

                dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                ServerController.getInputFromClient(dataInputStream, dataOutputStream);

            } catch (IOException e) {
                try {
                    assert dataInputStream != null;
                    dataInputStream.close();
                    socket.close();
                    serverSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Database.getInstance().updatingDatabase();
                System.out.println("a client disconnected!");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }).start();
    }

}
