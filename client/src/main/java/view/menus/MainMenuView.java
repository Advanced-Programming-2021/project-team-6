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
import javafx.util.Duration;
import view.Prompt;
import view.PromptType;

import java.io.IOException;

public class MainMenuView {

    public StackPane stackPane;
    public ImageView backButton;

    public void openNewGame() {
    }

    public void openDeckMenu() {
    }

    public void openShopMenu() {
    }

    public void openScoreboard() {
    }

    public void openProfileMenu() {
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
            root.translateXProperty().set(-1200);
            stackPane.getChildren().add(root);
            Timeline animationTimeLine = new Timeline();
            Timeline currentPageAnimationTimeLine = new Timeline();
            KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame nextPageKeyFrame = new KeyFrame(Duration.seconds(1), nextPageKeyValue);
            KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), +1200, Interpolator.EASE_IN);
            animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
            KeyFrame currentPageKeyFrame = new KeyFrame(Duration.seconds(1), currentPageKeyValue);
            currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
            animationTimeLine.play();
            currentPageAnimationTimeLine.play();
            animationTimeLine.setOnFinished(actionEvent -> {
                stackPane.getChildren().remove(0);
                stackPane.getChildren().remove(0);
                stackPane.getChildren().remove(0);
                scene.setRoot(root);
            });
        }

    }

}
