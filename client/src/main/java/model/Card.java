package model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Card {
    private int i;
    private int j;
    private String address;
    private String name;
    private boolean isUP;
    public Card(String name, String address, int i, int j) {
        this.name = name;
        this.address = address;
        this.i = i;
        this.j = j;
    }

    public Card(String name , String address , boolean isUp) {
        this.name = name;
        this.isUP = isUp;
        this.address = address;
    }


    public Image getImage() {
        return new Image(address);
    }

    public boolean isUP() {
        return isUP;
    }

    public void setUP(boolean UP) {
        isUP = UP;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return this.getAddress().equals(card.getAddress());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
