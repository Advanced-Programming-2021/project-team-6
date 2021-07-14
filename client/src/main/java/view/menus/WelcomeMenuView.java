package view.menus;

import controller.ClientController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.MusicManager;

import java.io.IOException;


public class WelcomeMenuView extends Application {

    public ImageView registerButton;
    public StackPane stackPane;
    public static Stage mainStage;

    public static void main(String[] args) {
        ClientController.initializeNetwork();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent pane = FXMLLoader.load(getClass().getResource("/fxml/WelcomeMenu.fxml"));
        Scene scene = new Scene(pane);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        stage.setScene(scene);
        mainStage = stage;
        mainStage.setResizable(false);
        stage.show();
        MusicManager.playMusic(MusicManager.musicBackground, true);

    }

    public void exitClicked() {
        System.exit(0);
    }

    public void openRegisterPage() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick,false);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/RegisterMenu.fxml"));
        Scene scene = registerButton.getScene();
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        root.translateXProperty().set(-1950);
        stackPane.getChildren().add(root);
        Timeline animationTimeLine = new Timeline();
        Timeline currentPageAnimationTimeLine = new Timeline();
        KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), 1950, Interpolator.EASE_IN);
        KeyFrame currentPageKeyFrame = new KeyFrame(Duration.seconds(1), currentPageKeyValue);
        KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame nextPageKeyFrame = new KeyFrame(Duration.seconds(1), nextPageKeyValue);
        currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
        animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
        animationTimeLine.play();
        currentPageAnimationTimeLine.play();
        animationTimeLine.setOnFinished(actionEvent -> {
            stackPane.getChildren().remove(scene.getRoot().getChildrenUnmodifiable().get(0));
        });
    }


    public void openLoginPage() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick,false);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginMenu.fxml"));
        Scene scene = registerButton.getScene();
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        root.translateXProperty().set(+1950);
        stackPane.getChildren().add(root);
        Timeline animationTimeLine = new Timeline();
        Timeline currentPageAnimationTimeLine = new Timeline();
        KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), -1950, Interpolator.EASE_IN);
        KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame nextPageKeyFrame = new KeyFrame(Duration.seconds(1), nextPageKeyValue);
        animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
        KeyFrame currentPageKeyFrame = new KeyFrame(Duration.seconds(1), currentPageKeyValue);
        currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
        animationTimeLine.play();
        currentPageAnimationTimeLine.play();
        animationTimeLine.setOnFinished(actionEvent -> stackPane.getChildren().remove(scene.getRoot().getChildrenUnmodifiable().get(0)));
    }
}
