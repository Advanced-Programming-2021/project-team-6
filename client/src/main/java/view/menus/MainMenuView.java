package view.menus;


import controller.AnimationUtility;
import controller.ClientController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.util.Duration;
import view.MusicManager;
import view.Prompt;
import view.PromptType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;

public class MainMenuView {

    public StackPane stackPane;
    private static StackPane stackPaneOfMainMenu;
    public ImageView backButton;
    StackPane waiting;

    static ChatView chatBox = null;

    public MainMenuView() throws IOException {
    }


    public static String startNewGame(String playerName, String opponentName, String playerProfile, String opponentProfile, String playerDeckSize, String opponentDeckSize
            , int duelId, boolean isFirstPlayer) throws IOException {
        Game.duelId = duelId;
        stackPaneOfMainMenu.getChildren().add(setUpDuelDoor(playerName, opponentName, playerProfile, opponentProfile, playerDeckSize, opponentDeckSize, isFirstPlayer));
        return "";
    }

    private static Pane setUpDuelDoor(String playerName, String opponentName, String playerProfile, String opponentProfile, String playerDeckSize, String opponentDeckSize
            , boolean isFirstPlayer) throws IOException {
        MusicManager.playMusic(MusicManager.gameMusic, true);
        StackPane gameView = FXMLLoader.load(MainMenuView.class.getResource("/fxml/GameView.fxml"));
        gameView.setOpacity(0);
        Pane stackPane = new Pane();
        Pane leftDoorPane = new Pane(), rightDoorPane = new Pane();
        ImageView leftDuelDoor = new ImageView(new Image(MainMenuView.class.getResource("/image/leftDueldoor.gif").toExternalForm()));
        ImageView rightDuelDoor = new ImageView(new Image(MainMenuView.class.getResource("/image/Dueldoor.gif").toExternalForm()));
        ImageView playerProfilePicture = new ImageView(new Image(MainMenuView.class.getResource("/image/profilePicture/" + playerProfile + ".jpg").toExternalForm()));
        ImageView opponentProfilePicture = new ImageView(new Image(MainMenuView.class.getResource("/image/profilePicture/" + opponentProfile + ".jpg").toExternalForm()));
        leftDoorPane.getChildren().add(leftDuelDoor);
        leftDoorPane.getChildren().add(playerProfilePicture);
        leftDoorPane.getChildren().get(1).translateXProperty().set(103);
        leftDoorPane.getChildren().get(1).translateYProperty().set(222);
        rightDoorPane.getChildren().add(rightDuelDoor);
        rightDoorPane.getChildren().add(opponentProfilePicture);
        rightDoorPane.getChildren().get(1).translateXProperty().set(268);
        rightDoorPane.getChildren().get(1).translateYProperty().set(220);
        opponentProfilePicture.setFitWidth(230);
        opponentProfilePicture.setFitHeight(230);
        playerProfilePicture.setFitHeight(230);
        playerProfilePicture.setFitWidth(230);
        leftDuelDoor.setFitHeight(680);
        leftDuelDoor.setFitWidth(862.5);
        rightDuelDoor.setFitHeight(680);
        rightDuelDoor.setFitWidth(621);
        stackPane.getChildren().add(leftDoorPane);
        stackPane.getChildren().add(rightDoorPane);
        stackPane.getChildren().add(new Text(playerName));
        stackPane.getChildren().add(new Text(opponentName));
        stackPane.getChildren().get(0).translateXProperty().set(-680);
        stackPane.getChildren().get(1).translateXProperty().set(1500);
        stackPaneOfMainMenu.getChildren().remove(1);
        AnimationUtility.playDoorAnimation(leftDoorPane, rightDoorPane, stackPaneOfMainMenu, gameView);
        if (isFirstPlayer)
            setDuelInformation(gameView, playerName, playerProfile, opponentName, opponentProfile, playerDeckSize, opponentDeckSize);
        else
            setDuelInformation(gameView, opponentName, opponentProfile, playerName, playerProfile, opponentDeckSize, playerDeckSize);



        stackPaneOfMainMenu.getChildren().add(gameView);
        return stackPane;
    }

    private static void setDuelInformation(StackPane gameView, String playerName, String playerProfile,
                                           String opponentName, String opponentProfile, String playerDeckSize, String opponentDeckSize) {
        ImageView playerImage, opponentImage;
        Label playerLabel, opponentLabel;
        Label playerDeckLabel, opponentDeckLabel;
        playerImage = ((ImageView) gameView.getChildren().get(2));
        opponentImage = ((ImageView) gameView.getChildren().get(3));
        playerImage.setImage(new Image(MainMenuView.class.getResource("/image/profilePicture/" + playerProfile + ".jpg").toExternalForm()));
        opponentImage.setImage(new Image(MainMenuView.class.getResource("/image/profilePicture/" + opponentProfile + ".jpg").toExternalForm()));
        playerLabel = ((Label) gameView.getChildren().get(7));
        playerDeckLabel = ((Label) gameView.getChildren().get(8));
        opponentLabel = ((Label) gameView.getChildren().get(6));
        opponentDeckLabel = ((Label) gameView.getChildren().get(9));
        constructPlayerDeck(Integer.parseInt(playerDeckSize) / 5 , gameView);
        constructOpponentDeck(Integer.parseInt(opponentDeckSize) / 5, gameView , opponentDeckLabel);
        playerLabel.setText(playerName);
        opponentLabel.setText(opponentName);
        playerDeckLabel.setText(playerDeckSize);
        opponentDeckLabel.setText(opponentDeckSize);
    }

    private static void constructOpponentDeck(int deckSize, StackPane gameView, Label opponentDeckLabel) {
        StackPane playerDeck = (StackPane) gameView.getChildren().get(11);
        ImageView newCard = new ImageView();
        playerDeck.getChildren().add(newCard);
        newCard.setScaleY(0.15);
        newCard.setScaleX(0.15);
        newCard.setImage(new Image(MainMenuView.class.getResource("/image/backOfCard.jpg").toExternalForm()));
        AnimationUtility.playSimpleCardTransition(newCard , 9, 0 , deckSize , -1200 , 0 , 8);
    }

    private static void constructPlayerDeck(int deckSize, StackPane gameView) {
        StackPane playerDeck = (StackPane) gameView.getChildren().get(10);
            ImageView newCard  = new ImageView();
            playerDeck.getChildren().add(newCard);
            newCard.setScaleX(0.15);
            newCard.setScaleY(0.15);
            newCard.setImage(new Image(MainMenuView.class.getResource("/image/backOfCard.jpg").toExternalForm()));
            AnimationUtility.playSimpleCardTransition(newCard , 9, 0 , deckSize , -1200 , 0 , 4);


    }

    public void openNewGame() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        String result = ClientController.waitForNewGame();
        if (result.startsWith("Success")) {
            System.out.println("waiting for opponent");
            waiting = FXMLLoader.load(getClass().getResource("/fxml/waiting.fxml"));
            stackPane = (StackPane) backButton.getScene().getRoot();
            stackPaneOfMainMenu = stackPane;
            stackPane.getChildren().add(waiting);
        } else if (result.startsWith("GameOn")) {
            String opponentUsername = result.split(" ")[1];
            ClientController.opponentUsername = opponentUsername;
            waiting = FXMLLoader.load(getClass().getResource("/fxml/waiting.fxml"));
            stackPane = (StackPane) backButton.getScene().getRoot();
            stackPaneOfMainMenu = stackPane;
            stackPane.getChildren().add(waiting);
            String[] params = result.split(" ");
            MainMenuView.startNewGame(params[1], params[3], params[2], params[4], params[5], params[6], Integer.parseInt(params[7]), false);
            System.out.println("game on");
        } else if (result.startsWith("Error"))
            System.out.println("Error");


    }

    public void openDeckMenu() throws IOException {
        new DeckMenuView().showDeckMenu();
    }

    public void openShopMenu() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        new ShopMenuView().showShop();
    }

    public void openScoreboard() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        new ScoreBoardView().scoreBoard();

    }

    public void openProfileMenu() throws IOException, URISyntaxException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        new ProfileMenuView().showProfile();
    }

    public void openImpExpMenu() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        new ImpExpMenuView().showImpExpMenu();

    }

    public void openCreateCardMenu() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        new CreateCardMenuView().showCreateCardMenu();
    }

    public void openChatRoom() throws IOException {
        if (chatBox == null)
            chatBox = new ChatView();
        chatBox.initializeChat();
    }


    public void logout() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        String result = ClientController.logout();
        if (result.startsWith("Error")) {
            Prompt.showMessage("logout was not successful", PromptType.Error);
        }
        if (result.startsWith("Success")) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomeMenu.fxml"));
            Scene scene = backButton.getScene();
            scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
            stackPane = (StackPane) scene.getRoot();
            stackPaneOfMainMenu = stackPane;
            root.translateXProperty().set(-1950);
            stackPane.getChildren().add(root);
            Timeline currentPageAnimationTimeLine = new Timeline();
            Timeline animationTimeLine = new Timeline();
            animationTimeLine.setOnFinished(actionEvent -> {
                stackPane.getChildren().remove(0);
                stackPane.getChildren().remove(0);
                scene.setRoot(root);
            });
            KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame nextPageKeyFrame = new KeyFrame(Duration.seconds(1), nextPageKeyValue);
            KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(),
                    +1950, Interpolator.EASE_IN);
            KeyFrame currentPageKeyFrame = new KeyFrame(Duration.seconds(1), currentPageKeyValue);
            animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
            currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
            animationTimeLine.play();
            currentPageAnimationTimeLine.play();

        }

    }

    public void cancelGame() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        stackPane = (StackPane) backButton.getScene().getRoot();
        stackPaneOfMainMenu = stackPane;
        if (ClientController.cancelGameRequest().equals("Success"))
            stackPane.getChildren().remove(1);
    }

    public void changeMute() {
        MusicManager.isMusicOn *= -1;
        if (MusicManager.isMusicOn == 1)
            MusicManager.musicBackground.play();
        else
            MusicManager.musicBackground.stop();
    }

    public static ChatView getChat(){
        return chatBox;
    }
}
