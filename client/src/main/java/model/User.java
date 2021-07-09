package model;

import java.util.ArrayList;

public class User {
    public int index;
    public String username;
    public int point;
    static public ArrayList<User> Users;
    static {
        Users = new ArrayList<>();
    }
    public User(int index, String username, int point) {
        this.index = index;
        this.username = username;
        this.point = point;
        Users.add(this);
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
