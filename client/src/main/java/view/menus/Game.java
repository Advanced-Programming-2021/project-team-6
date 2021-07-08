package view.menus;
import controller.AnimationUtility;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


import java.net.URL;
import java.util.ResourceBundle;

public class Game implements Initializable {
    public static int duelId;
    public StackPane opponentDeckFXML;
    public static StackPane opponentDeck;
    public StackPane playerDeckFXML;
    public StackPane playerDeck;
    public HBox handFXML;
    public static HBox hand;
    public HBox opponentHandFXML;
    public static HBox opponentHand;

    public static void drawCardForPlayer(String cardName) {
        ImageView newCard = new ImageView();
        newCard.setFitWidth(150);
        newCard.setFitHeight(200);
        newCard.translateXProperty().set(hand.getChildren().size() * -40);
        Image cardImage = new Image(Game.class.getResource("/image/Cards/" + cardName + ".jpg").toExternalForm());
        newCard.setOnMouseEntered(mouseEvent -> AnimationUtility.playScalingAnimationOnACard(newCard , 0 , 1.2 , 1.2 , -80));
        newCard.setOnMouseExited(mouseEvent -> AnimationUtility.playScalingAnimationOnACard(newCard , 0 , 1 , 1 , 0));
        newCard.setImage(cardImage);
        hand.getChildren().add(newCard);
    }

    public static void drawCardForOpponent() {
        ImageView newCard = new ImageView();
        newCard.translateXProperty().set(opponentHand.getChildren().size() * -40);
        newCard.setOnMouseEntered(mouseEvent -> AnimationUtility.playScalingAnimationOnACard(newCard , 0 , 1.2 , 1.2 , 0));
        newCard.setOnMouseExited(mouseEvent -> AnimationUtility.playScalingAnimationOnACard(newCard , 0 , 1 , 1 , 0));
        newCard.setFitWidth(150);
        newCard.setFitHeight(200);
        Image cardImage = new Image(Game.class.getResource("/image/backOfCard.jpg").toExternalForm());
        newCard.setImage(cardImage);
        opponentHand.getChildren().add(newCard);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        opponentDeck = opponentDeckFXML;
        playerDeck = playerDeckFXML;
        hand = handFXML;
        opponentHand = opponentHandFXML;
    }

    public void getDeckDown() {
        AnimationUtility.animateTranslateY(hand , 0 , hand.translateXProperty().get() , 380 , 300);
    }
    public void bringDeckUp() {
        AnimationUtility.animateTranslateY(hand , 0 , hand.translateXProperty().get() , 300 , 300);
    }
    public void getOpponentDeckDown() {
        AnimationUtility.animateTranslateY(opponentHand , 0 , hand.translateXProperty().get() , -400 , 300);
    }
    public void bringOpponentDeckUp() {
        AnimationUtility.animateTranslateY(opponentHand , 0 , hand.translateXProperty().get() , -330 , 300);
    }
}
