package model;

import java.util.ArrayList;

public class Message {
    public static ArrayList<Message> allMessages = new ArrayList<>();

    public String username;
    public String content;

    public Message(String username, String content) {
        this.content = content;
        this.username = username;
        allMessages.add(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
