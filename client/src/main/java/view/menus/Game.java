package view.menus;
import controller.AnimationUtility;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;


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
    public Rectangle dropReactorFXML;
    public static Rectangle dropReactor;
    public VBox fieldFXML;
    public static VBox field;

    public static void drawCardForPlayer(String cardName) {
        ImageView newCard = new ImageView();
        newCard.setFitWidth(150);
        newCard.setFitHeight(200);
        newCard.translateXProperty().set(hand.getChildren().size() * -40);
        Image cardImage = new Image(Game.class.getResource("/image/Cards/" + cardName + ".jpg").toExternalForm());
        newCard.setOnMouseEntered(mouseEvent -> AnimationUtility.playScalingAnimationOnACard(newCard , 0 , 1.2 , 1.2 , -80));
        newCard.setOnMouseExited(mouseEvent -> AnimationUtility.playScalingAnimationOnACard(newCard , 0 , 1 , 1 , 0));
        newCard.setOnDragDetected(mouseEvent ->  {
                Dragboard dragboard = newCard.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putImage(newCard.getImage());
                newCard.opacityProperty().set(0);
                dragboard.setContent(content);
                mouseEvent.consume();

        });
        newCard.setOnDragDone(mouseEvent ->  {
            newCard.opacityProperty().set(100);
            mouseEvent.consume();
        });
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
        dropReactor = dropReactorFXML;
        field = fieldFXML;
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
    public void dragEnter (DragEvent dragEvent) {
        dropReactor.setOpacity(100);
        dragEvent.consume();
    }
    public void dragEXit (DragEvent dragEvent) {
        dropReactor.setOpacity(0);
        dragEvent.consume();
    }
    public void dragDropped(DragEvent dragEvent) {
        System.out.println("ba aliz az ya aliz yek noghte kam darad vali...\n" +
                "ba aliz boodan koja va ya aliz goftan koja");
        dropReactor.setOpacity(0);
        dragEvent.consume();
    }

    public void acceptDrag(DragEvent event) {
        if (event.getDragboard().hasImage()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }
}
