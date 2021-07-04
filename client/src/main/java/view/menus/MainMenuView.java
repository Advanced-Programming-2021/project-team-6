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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.Prompt;
import view.PromptType;

import java.io.IOException;
import java.net.URISyntaxException;

public class MainMenuView {

    public StackPane stackPane;
    public ImageView backButton;

    public void openNewGame() throws IOException {
         String result = ClientController.waitForNewGame();
         switch (result) {
             case "Success" :
                 System.out.println("waiting for opponent");
                 break;
             case "GameOn" :
                 System.out.println("game on");
                 break;
             case "Error":
                 System.out.println("Error");
                 break;
         }
    }

    public void openDeckMenu() {
    }

    public void openShopMenu() {
    }

    public void openScoreboard() throws IOException {
        new ScoreBoardView().showScoreboard();

    }

    public void openProfileMenu() throws IOException, URISyntaxException {
        new ProfileMenuView().showProfile();
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
            Timeline animationTimeLine = new Timeline();
            Timeline currentPageAnimationTimeLine = new Timeline();
            KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame nextPageKeyFrame = new KeyFrame(Duration.seconds(1), nextPageKeyValue);
            KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), +1950, Interpolator.EASE_IN);
            animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
            KeyFrame currentPageKeyFrame = new KeyFrame(Duration.seconds(1), currentPageKeyValue);
            currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
            animationTimeLine.play();
            currentPageAnimationTimeLine.play();
            animationTimeLine.setOnFinished(actionEvent -> {
                stackPane.getChildren().remove(0);
                stackPane.getChildren().remove(0);
             //   stackPane.getChildren().remove(0);
                scene.setRoot(root);
            });
        }

    }

}
