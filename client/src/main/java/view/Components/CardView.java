package view.Components;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;


public class CardView extends ImageView {
    private int i;
    private int j;

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}

