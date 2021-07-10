package model;

import java.util.ArrayList;

public class User {
    public int index;
    public String nickname;
    public int point;
    static public ArrayList<User> Users;
    static {
        Users = new ArrayList<>();
    }
    public User(int index, String nickname, int point) {
        this.index = index;
        this.nickname = nickname;
        this.point = point;
        Users.add(this);
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
