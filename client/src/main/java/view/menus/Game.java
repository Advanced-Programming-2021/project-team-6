package view.menus;

import controller.AnimationUtility;
import controller.ClientController;
import controller.ServerMessageHandler;
import javafx.animation.RotateTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.Card;
import view.MusicManager;
import view.Prompt;
import view.PromptType;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

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
    public static ArrayList<Card> cardsOfHand = new ArrayList<>();
    public static ArrayList<Card> cardsOfMonsterZone = new ArrayList<>();
    public static ArrayList<Card> cardsOfOpponentMonsterZone = new ArrayList<>();
    public static ArrayList<Card> cardsOfSpellZone = new ArrayList<>();
    public static ArrayList<Card> cardsOfOpponentSpellZone = new ArrayList<>();
    public Label numberOfCardsRemainingInPlayersDeckFXML;
    public static Label numberOfCardsRemainingInPlayersDeck;
    public static Label numberOfCardsRemainingInOpponentsDeck;
    public Label numberOfCardsRemainingInOpponentsDeckFXML;
    public Label myLPText;
    public Label LPTextOpponentFxml;
    public static Label LPTextOpponent;
    public ToggleGroup setOrSummonFXML;
    public static ToggleGroup setOrSummon;
    public static String phase = "DRAW";
    public ImageView phaseBilFXML;
    public static ImageView phaseBil;


    public static void drawCardForPlayer(String cardName, double delay) {
        String type = cardName.split(":")[0];
        cardName = cardName.split(":")[1];
        numberOfCardsRemainingInOpponentsDeck.setText((Integer.parseInt(numberOfCardsRemainingInOpponentsDeck.getText()) - 1) + "");
        String address = "/image/Cards/" + cardName + ".jpg";
        ImageView newCard = new ImageView();
        newCard.setFitWidth(150);
        newCard.setFitHeight(200);
        newCard.translateXProperty().set(hand.getChildren().size() * -40);
        cardsOfHand.add(new Card(cardName, address, true, type));
        Image cardImage = new Image(Game.class.getResource("/image/backOfCard.jpg").toExternalForm());
        newCard.setOnMouseEntered(mouseEvent -> AnimationUtility.playScalingAnimationOnACard(newCard, 0, 1.2, 1.2, -80));
        newCard.setOnMouseReleased(mouseEvent ->
                {
                    try {
                        Card card = cardsOfHand.get(hand.getChildren().indexOf(newCard));
                        String description = ClientController.getDescription(card.getName());
                        String cardAddress = card.getAddress();
                        showCardInfo(description, cardAddress);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
        );
        newCard.setOnMouseExited(mouseEvent -> AnimationUtility.playScalingAnimationOnACard(newCard, 0, 1, 1, 0));
        newCard.setOnDragDetected(mouseEvent -> {
            newCard.setOpacity(0);
            Dragboard dragboard = newCard.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putImage(newCard.getImage());
            dragboard.setContent(content);
            mouseEvent.consume();

        });
        newCard.setOnDragDone(mouseEvent -> {
            if (mouseEvent.isAccepted()) {
                int cardAddress = hand.getChildren().indexOf(newCard);
                String result = "";
                Card cardModel = cardsOfHand.get(hand.getChildren().indexOf(newCard));
                ;
                try {
                    if (setOrSummon.getToggles().get(0).isSelected()) {
                        if (cardsOfHand.get(cardAddress).getType().equals("m")) {
                            result = ClientController.setMonster(String.valueOf(cardAddress));
                            setMonster(result, newCard, cardModel);
                        } else {
                            result = ClientController.setSpellAndTrap(String.valueOf(cardAddress));
                            setSpell(result, newCard, cardModel);
                        }
                    } else if (cardsOfHand.get(cardAddress).getType().equals("m")) {
                        result = ClientController.summon(String.valueOf(cardAddress));
                        summon(result, newCard, cardModel);
                    } else
                        System.out.println("activate effect");

                } catch (Exception ignored) {
                }
            }
            newCard.opacityProperty().set(100);
            mouseEvent.consume();
        });
        newCard.setImage(cardImage);
        hand.getChildren().add(newCard);
        AnimationUtility.cardGoesFromPlayerDeckToTheirHand(newCard, hand, address, delay);
    }

    private static void setSpell(String result, ImageView card, Card cardModel) {

    }

    public static void summonForOpponent(String name, String type) {
        String address = "/image/Cards/" + name + ".jpg";
        ImageView card = (ImageView) opponentHand.getChildren().get(opponentHand.getChildren().size() - 1);
        card.setImage(new Image(Game.class.getResource(address).toExternalForm()));
        card.setFitWidth(82.5);
        card.setFitHeight(110);
        card.setTranslateX(0);
        card.setTranslateY(0);
        opponentHand.getChildren().remove(card);
        cardsOfOpponentMonsterZone.add(new Card(name, address, false, type));
        ((HBox) field.getChildren().get(1)).getChildren().add(card);
    }

    public static void setMonsterForOpponent(String name, String type) {
        String address = "/image/Cards/" + name + ".jpg";
        ImageView card = (ImageView) opponentHand.getChildren().get(opponentHand.getChildren().size() - 1);
        cardsOfOpponentMonsterZone.add(new Card(name, address, false, type));
        opponentHand.getChildren().remove(card);
        ((HBox) field.getChildren().get(1)).getChildren().add(card);
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.3), card);
        rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setFromAngle(0);
        rotateTransition.setFromAngle(90);
        card.setFitWidth(82.5);
        card.setFitHeight(110);
        card.setTranslateX(0);
        card.setTranslateY(0);
        rotateTransition.play();
    }

    private static void summon(String result, ImageView card, Card cardModel) {
        if (result.startsWith("Success")) {
            card.setTranslateX(0);
            card.setTranslateY(0);
            hand.getChildren().remove(card);
            card.setFitWidth(82.5);
            card.setFitHeight(110);
            ((HBox) field.getChildren().get(2)).getChildren().add(card);
            cardsOfMonsterZone.add(cardModel);
            cardsOfHand.remove(cardModel);
        }
    }

    private static void setMonster(String result, ImageView card, Card cardModel) {
        if (result.startsWith("Success")) {
            hand.getChildren().remove(card);
            card.setFitWidth(82.5);
            card.setFitHeight(110);
            card.setTranslateX(0);
            card.setTranslateY(0);
            ((HBox) field.getChildren().get(2)).getChildren().add(card);
            cardsOfMonsterZone.add(cardModel);
            cardModel.setUP(false);
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.3), card);
            rotateTransition.setAxis(Rotate.Z_AXIS);
            rotateTransition.setFromAngle(0);
            rotateTransition.setFromAngle(90);
            rotateTransition.play();
            card.setImage(new Image(Game.class.getResource("/image/backOfCard.jpg").toExternalForm()));
            cardsOfHand.remove(cardModel);
        }
    }

    private static void showCardInfo(String description, String cardAddress) throws IOException {
        Parent cardInfo = FXMLLoader.load(Game.class.getResource("/fxml/CardInfo.fxml"));
        ((StackPane) hand.getScene().getRoot()).getChildren().add(cardInfo);
        ((StackPane) cardInfo).getChildren().get(5).setOnMouseClicked(mouseEvent -> ((StackPane) hand.getScene().getRoot()).getChildren().remove(cardInfo));
        ((StackPane) cardInfo).getChildren().get(4).setOnMouseClicked(mouseEvent -> ((StackPane) hand.getScene().getRoot()).getChildren().remove(cardInfo));
        ((ImageView) cardInfo.getChildrenUnmodifiable().get(3)).setImage(new Image(Game.class.getResource(cardAddress).toExternalForm()));
        ((Text) cardInfo.getChildrenUnmodifiable().get(2)).setText(description);
    }

    public static void drawCardForOpponent(double delay) {

        ImageView newCard = new ImageView();
        newCard.translateXProperty().set(opponentHand.getChildren().size() * -40);
        newCard.setOnMouseEntered(mouseEvent -> AnimationUtility.playScalingAnimationOnACard(newCard, 0, 1.2, 1.2, 0));
        newCard.setOnMouseExited(mouseEvent -> AnimationUtility.playScalingAnimationOnACard(newCard, 0, 1, 1, 0));
        newCard.setFitWidth(150);
        newCard.setFitHeight(200);
        Image cardImage = new Image(Game.class.getResource("/image/backOfCard.jpg").toExternalForm());
        newCard.setImage(cardImage);
        opponentHand.getChildren().add(newCard);
        AnimationUtility.cardGoesFromOpponentDeckToTheirHand(newCard, opponentHand, delay);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phaseBil = phaseBilFXML;
        opponentDeck = opponentDeckFXML;
        playerDeck = playerDeckFXML;
        hand = handFXML;
        opponentHand = opponentHandFXML;
        dropReactor = dropReactorFXML;
        field = fieldFXML;
        LPTextOpponent = LPTextOpponentFxml;
        setOrSummon = setOrSummonFXML;
        numberOfCardsRemainingInOpponentsDeck = numberOfCardsRemainingInOpponentsDeckFXML;
        numberOfCardsRemainingInPlayersDeck = numberOfCardsRemainingInPlayersDeckFXML;
        setOrSummonFXML.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
    }

    public void getDeckDown() {
        AnimationUtility.animateTranslatePosition(hand, 0, hand.translateXProperty().get(), 380, 300);
    }

    public void bringDeckUp() {
        AnimationUtility.animateTranslatePosition(hand, 0, hand.translateXProperty().get(), 300, 300);
    }

    public void getOpponentDeckDown() {
        AnimationUtility.animateTranslatePosition(opponentHand, 0, hand.translateXProperty().get(), -400, 300);
    }

    public void bringOpponentDeckUp() {
        AnimationUtility.animateTranslatePosition(opponentHand, 0, hand.translateXProperty().get(), -330, 300);
    }

    public void dragEnter(DragEvent dragEvent) {
        dropReactor.setOpacity(100);
        dragEvent.consume();
    }

    public void dragEXit(DragEvent dragEvent) {
        dropReactor.setOpacity(0);
        dragEvent.consume();
    }

    public void dragDropped(DragEvent dragEvent) {
        dragEvent.setDropCompleted(true);
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

    public static String getCardName(ImageView card) {
        String url = card.getImage().getUrl();
        return url.replaceAll("file:/media/black-titan/E295-8FF4/Univertsity/In%20Progress/AP/Yu-Gi-Oh/client/target/classes/image/Cards/", "")
                .replaceAll(".jpg", "").replaceAll("%20", " ");
    }

    public void cheatLP() throws IOException {
        String result = ClientController.cheatLP();

        if (result.startsWith("Error")) return;

        myLPText.setText(result);
        MusicManager.playMusic(MusicManager.LPSound, false);
    }

    public static void cheatLPOpponent() {
        LPTextOpponent.setText(String.valueOf(Integer.parseInt(LPTextOpponent.getText()) + 1));
        MusicManager.playMusic(MusicManager.LPSound, false);
    }


    public void cheatWin() throws IOException, InterruptedException {
        String message = ClientController.cheatWin();

        Prompt.showMessage(message, PromptType.Message);
        MusicManager.playMusic(MusicManager.winSound, false);

        TimeUnit.SECONDS.sleep(3);
        Parent root = FXMLLoader.load(ServerMessageHandler.class.getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(ServerMessageHandler.class.getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }

    public static void changePhaseGraphically(String phase) {
        ServerMessageHandler.isFirstDraw = false;
        Game.phase = phase;
        switch (phase) {
            case "DRAW":
                AnimationUtility.animateTranslatingPhaseBil(phaseBil, 0, -210, 3);
                break;
            case "STANDBY":
                AnimationUtility.animateTranslatingPhaseBil(phaseBil, 0, -130, 3);
                break;
            case "MAIN1":
                AnimationUtility.animateTranslatingPhaseBil(phaseBil, 0, -50, 3);
                break;
            case "BATTLE":
                AnimationUtility.animateTranslatingPhaseBil(phaseBil, 0, 35, 3);
                break;
            case "MAIN2":
                AnimationUtility.animateTranslatingPhaseBil(phaseBil, 0, 115, 3);
                break;
            case "END":
                AnimationUtility.animateTranslatingPhaseBil(phaseBil, 0, 200, 3);
                break;
        }
    }

    public void changePhase(MouseEvent mouseEvent) throws IOException {
        String result = ClientController.changePhase();
        if (result.startsWith("Error")) {
            Prompt.showMessage(result.split(":")[1], PromptType.Error);
        } else changePhaseGraphically(result.split(": ")[1]);
    }


    public void openPause() throws IOException {
        Parent cardInfo = FXMLLoader.load(Game.class.getResource("/fxml/Pause.fxml"));
        ((StackPane) hand.getScene().getRoot()).getChildren().add(cardInfo);
        ((StackPane) cardInfo).getChildren().get(5).setOnMouseClicked(mouseEvent -> ((StackPane) hand.getScene().getRoot()).getChildren().remove(cardInfo));
        ((StackPane) cardInfo).getChildren().get(2).setOnMouseClicked(mouseEvent -> ((StackPane) hand.getScene().getRoot()).getChildren().remove(cardInfo));
        ((StackPane) cardInfo).getChildren().get(3).setOnMouseClicked(mouseEvent -> {
            try {
                ClientController.surrender();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        ((StackPane) cardInfo).getChildren().get(4).setOnMouseClicked(mouseEvent -> {
            try {

                String message = ClientController.surrender();

                Prompt.showMessage(message, PromptType.Message);
                MusicManager.playMusic(MusicManager.winSound, false);

                TimeUnit.SECONDS.sleep(3);
                Parent root = FXMLLoader.load(ServerMessageHandler.class.getResource("/fxml/MainMenu.fxml"));
                Scene scene = new Scene(root);
                scene.setCursor(new ImageCursor(new Image(ServerMessageHandler.class.getResource("/image/mouse.jpg").toString())));
                WelcomeMenuView.mainStage.setScene(scene);
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}