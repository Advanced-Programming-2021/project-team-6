package models;

import controller.menus.ImpExpMenuController;
import controller.menus.MainMenuController;
import serverConection.Main;
import serverConection.ServerController;

import java.util.ArrayList;
import java.util.Map;

public class Messenger {

    private ArrayList<Message> allMessage = new ArrayList<>();

    private Messenger() {
    }

    private static Messenger instance;

    public static Messenger getInstance() {
        if (instance == null)
            instance = new Messenger();
        return instance;
    }

    private String pin;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAllMessage(String token) {
        String messages = "";
        for (Message message : allMessage)
            messages += message.getSender() + ": " + message.getContent() + "\n";
        return messages;
    }

    public String addMessage(String token, String content) {
        Player player = Database.getInstance().getPlayerByToken(token);
        if (player == null) return "Error";
        allMessage.add(new Message(player.getUsername(), content, token));
        for (Map.Entry<String, Player> entry : MainMenuController.getInstance().loggedInUsers.entrySet())
            ServerController.sendMessageToSocket(entry.getKey(),
                    "new message " + entry.getValue().getUsername() + ": \"" + content + "\"", false);

        return "Success";
    }
}

class Message {

    private String sender;
    private String content;
    private String senderToken;

    public Message(String sender, String content, String senderToken) {
        this.content = content;
        this.sender = sender;
        this.senderToken = senderToken;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderToken() {
        return senderToken;
    }

    public void setSenderToken(String senderToken) {
        this.senderToken = senderToken;
    }
}