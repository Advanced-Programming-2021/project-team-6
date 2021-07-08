package controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
            nextView.setOpacity(100);
            playCloseDoorAnimation(leftDoor , rightDoor , menuStackPane);
        });
        leftDoorAnimation.play();
        rightDoorAnimation.play();
    }
    public static void playCloseDoorAnimation(Pane leftDoor, Pane rightDoor , StackPane menuStackPane) {
        KeyValue rightDoorKValue = new KeyValue(rightDoor.translateXProperty() , 1500 , Interpolator.LINEAR),
                leftDoorKValue = new KeyValue(leftDoor.translateXProperty() , -900 , Interpolator.LINEAR);
        KeyFrame rightDoorKeyFrame = new KeyFrame(Duration.seconds(3), rightDoorKValue) ,
                leftDoorKeyFrame = new KeyFrame(Duration.seconds(3) , leftDoorKValue);
        Timeline leftDoorAnimation = new Timeline(leftDoorKeyFrame) , rightDoorAnimation = new Timeline(rightDoorKeyFrame);
        leftDoorAnimation.delayProperty().set(Duration.seconds(3));
        rightDoorAnimation.delayProperty().set(Duration.seconds(3));
        leftDoorAnimation.setOnFinished(actionEvent -> {
            menuStackPane.getChildren().remove(2);
            menuStackPane.getChildren().remove(0);
        });
                rightDoorAnimation.play();
                leftDoorAnimation.play();
    }
    public static synchronized void playSimpleCardTransition(ImageView card , int destX , int destY , int deckSize , int startX , int startY , int delay) {
        card.translateXProperty().set(startX);
        card.translateYProperty().set(startY);
        KeyValue cardYValue = new KeyValue(card.translateYProperty() , destY , Interpolator.EASE_OUT) , cardXValue = new KeyValue(card.translateXProperty(), destX , Interpolator.EASE_OUT );
        KeyFrame cardYFrame = new KeyFrame(Duration.millis(250) , cardYValue) , cardXFrame = new KeyFrame(Duration.millis(250) , cardXValue);
        Timeline cardYTimeLine = new Timeline(cardYFrame),
         cardXTimeLine = new Timeline(cardXFrame);
        cardXTimeLine.setDelay(Duration.seconds(delay));
        cardYTimeLine.setDelay(Duration.seconds(delay));
        cardXTimeLine.setCycleCount(deckSize);
        cardYTimeLine.setCycleCount(deckSize);
        cardYTimeLine.play();
        cardXTimeLine.play();
    }
    public static void playScalingAnimationOnACard(ImageView card, double delay , double destX , double destY , double destTranslateY) {
        KeyValue cardYValue = new KeyValue(card.scaleYProperty() , destY , Interpolator.EASE_OUT) , cardXValue = new KeyValue(card.scaleXProperty(), destX , Interpolator.EASE_OUT );
        KeyFrame cardYFrame = new KeyFrame(Duration.millis(300) , cardYValue) , cardXFrame = new KeyFrame(Duration.millis(300) , cardXValue);
        Timeline cardYTimeLine = new Timeline(cardYFrame),
                cardXTimeLine = new Timeline(cardXFrame);
        KeyValue cardTYValue = new KeyValue(card.translateYProperty() , destTranslateY , Interpolator.EASE_OUT);
        KeyFrame cardTYFrame = new KeyFrame(Duration.millis(300) , cardTYValue);
        Timeline cardTYTimeLine = new Timeline(cardTYFrame);
        cardTYTimeLine.play();
        cardXTimeLine.setDelay(Duration.seconds(delay));
        cardYTimeLine.setDelay(Duration.seconds(delay));
        cardYTimeLine.play();
        cardXTimeLine.play();
    }

    public static void animateTranslateY(Parent object, int delay, double destX ,double destY , int duration) {
        KeyValue cardYValue = new KeyValue(object.translateYProperty() , destY , Interpolator.EASE_OUT) , cardXValue = new KeyValue(object.translateXProperty(), destX , Interpolator.EASE_OUT );
        KeyFrame cardYFrame = new KeyFrame(Duration.millis(duration) , cardYValue) , cardXFrame = new KeyFrame(Duration.millis(duration) , cardXValue);
        Timeline cardXTimeLine = new Timeline(cardXFrame),
                 cardYTimeLine = new Timeline(cardYFrame);
        cardYTimeLine.setDelay(Duration.seconds(delay));
        cardXTimeLine.setDelay(Duration.seconds(delay));
        cardYTimeLine.play();
        cardXTimeLine.play();
    }
}
