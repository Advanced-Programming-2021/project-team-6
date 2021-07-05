package view.menus;


import controller.ClientController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.Prompt;
import view.PromptType;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class MainMenuView {

    public StackPane stackPane;
    private static StackPane stackPaneOfMainMenu;
    public ImageView backButton;
    StackPane waiting;

    public static String startNewGame(String playerName , String opponentName , String playerProfile , String opponentProfile) {
        Label playerLabel = new Label(playerName) ,opponentLabel = new Label(opponentName);
        StackPane stackPane = new StackPane();
    }

    public void openNewGame() throws IOException {
        String result = ClientController.waitForNewGame();


            if (result.startsWith("Success")) {
                System.out.println("waiting for opponent");
                waiting = FXMLLoader.load(getClass().getResource("/fxml/waiting.fxml"));
                stackPane = (StackPane) backButton.getScene().getRoot();
                stackPane.getChildren().add(waiting);
            }
            else if (result.startsWith("GameOn")) {
                    waiting = FXMLLoader.load(getClass().getResource("/fxml/waiting.fxml"));
                    stackPane = (StackPane) backButton.getScene().getRoot();
                    stackPane.getChildren().add(waiting);
                    System.out.println("game on");
            }
            else if(result.startsWith("Error"))
                System.out.println("Error");


    }

    public void openDeckMenu() {
    }

    public void openShopMenu() throws IOException {
        new ShopMenuView().showShop();
    }

    public void openScoreboard() throws IOException {
        new ScoreBoardView().showScoreboard();

    }

    public void openProfileMenu() throws IOException, URISyntaxException {
        new ProfileMenuView().showProfile();
    }

    public void openImpExpMenu() throws IOException {
        new ImpExpMenuView().showImpExpMenu();

    }

    public void openCreateCardMenu() {

    }

    public void logout() throws IOException {
        String result = ClientController.logout();
        if (result.startsWith("Error")) {
            Prompt.showMessage("logout was not successful", PromptType.Error);
        }
        if (result.startsWith("Success")) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomeMenu.fxml"));
            Scene scene = backButton.getScene();
            stackPane = (StackPane) scene.getRoot();
            root.translateXProperty().set(-1950);
            stackPane.getChildren().add(root);
            stackPaneOfMainMenu = stackPane;
            Timeline currentPageAnimationTimeLine = new Timeline();
            Timeline animationTimeLine = new Timeline();
            animationTimeLine.setOnFinished(actionEvent -> {
                stackPane.getChildren().remove(0);
                stackPane.getChildren().remove(0);
                scene.setRoot(root);
            });
            KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame nextPageKeyFrame = new KeyFrame(Duration.seconds(1), nextPageKeyValue);
            KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), +1950, Interpolator.EASE_IN);
            KeyFrame currentPageKeyFrame = new KeyFrame(Duration.seconds(1), currentPageKeyValue);
            animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
            currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
            animationTimeLine.play();
            currentPageAnimationTimeLine.play();

        }

    }

    public void cancelGame() throws IOException {
        stackPane = (StackPane) backButton.getScene().getRoot();
        stackPaneOfMainMenu = stackPane;
        if (ClientController.cancelGameRequest().equals("Success"))
            stackPane.getChildren().remove(1);
    }
}
