package model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Card {
    private int i;
    private int j;
    private String address;

    public Card(String address, int i, int j) {
        this.address = address;
        this.i = i;
        this.j = j;
    }


    public ImagePattern getImage() {
        return new ImagePattern(new Image(address));
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
}
