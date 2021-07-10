package model;

import controller.ClientController;

import java.io.IOException;
import java.util.ArrayList;

public class User {
    static public ArrayList<User> Users;
    static public ArrayList<Card> inactiveCards;

    static {
        Users = new ArrayList<>();
        inactiveCards = new ArrayList<>();
    }

    public int index;
    public String nickname;
    public int point;

    public User(int index, String nickname, int point) throws IOException {
        this.index = index;
        this.nickname = nickname;
        this.point = point;
        Users.add(this);
    }

    public static void setInactiveCards() throws IOException {
        String[] string = ClientController.inactiveCards().split(":");
        for (String str : string) {
            inactiveCards.add(new Card(str, "/image/Cards/" + str + ".jpg"));
        }
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
