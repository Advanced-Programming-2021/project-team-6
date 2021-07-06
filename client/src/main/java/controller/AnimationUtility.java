package controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class AnimationUtility {

    public static void playDoorAnimation(Pane leftDoor, Pane rightDoor , StackPane menuStackPane , StackPane nextView) {
        KeyValue rightDoorKValue = new KeyValue(rightDoor.translateXProperty() , 610 , Interpolator.LINEAR),
                leftDoorKValue = new KeyValue(leftDoor.translateXProperty() , -20 , Interpolator.LINEAR);
        KeyFrame rightDoorKeyFrame = new KeyFrame(Duration.seconds(3), rightDoorKValue) ,
        leftDoorKeyFrame = new KeyFrame(Duration.seconds(3) , leftDoorKValue);
        Timeline leftDoorAnimation = new Timeline(leftDoorKeyFrame) , rightDoorAnimation = new Timeline(rightDoorKeyFrame);
        leftDoorAnimation.setOnFinished(actionEvent -> {
            menuStackPane.getChildren().remove(0);
            nextView.setOpacity(100);
            playCloseDoorAnimation(leftDoor , rightDoor);
        });
        leftDoorAnimation.play();
        rightDoorAnimation.play();
    }
    public static void playCloseDoorAnimation(Pane leftDoor, Pane rightDoor) {
        KeyValue rightDoorKValue = new KeyValue(rightDoor.translateXProperty() , 1500 , Interpolator.LINEAR),
                leftDoorKValue = new KeyValue(leftDoor.translateXProperty() , -900 , Interpolator.LINEAR);
        KeyFrame rightDoorKeyFrame = new KeyFrame(Duration.seconds(3), rightDoorKValue) ,
                leftDoorKeyFrame = new KeyFrame(Duration.seconds(3) , leftDoorKValue);
        Timeline leftDoorAnimation = new Timeline(leftDoorKeyFrame) , rightDoorAnimation = new Timeline(rightDoorKeyFrame);
        leftDoorAnimation.delayProperty().set(Duration.seconds(3));
        rightDoorAnimation.delayProperty().set(Duration.seconds(3));
        leftDoorAnimation.play();
        rightDoorAnimation.play();
    }
}
