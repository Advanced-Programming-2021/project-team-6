package view.menus;

import controller.ClientController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import view.MusicManager;
import view.Prompt;
import view.PromptType;

import java.io.IOException;

public class RegisterMenuView {
    public ImageView backButton;
    public StackPane stackPane;
    public TextField registerUsernameInput;
    public TextField registerPasswordInput;
    public TextField registerNicknameInput;

    public void openFirstPage() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick,false);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomeMenu.fxml"));
        Scene scene = backButton.getScene();
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        stackPane = (StackPane) scene.getRoot();
        root.translateXProperty().set(+1950);
        stackPane.getChildren().add(root);
        Timeline animationTimeLine = new Timeline();
        Timeline currentPageAnimationTimeLine = new Timeline();
        KeyValue nextPageKeyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame nextPageKeyFrame = new KeyFrame(Duration.seconds(1), nextPageKeyValue);
        KeyValue currentPageKeyValue = new KeyValue(scene.getRoot().getChildrenUnmodifiable().get(0).translateXProperty(), -1950, Interpolator.EASE_IN);
        KeyFrame currentPageKeyFrame = new KeyFrame(Duration.seconds(1), currentPageKeyValue);
        animationTimeLine.getKeyFrames().add(nextPageKeyFrame);
        currentPageAnimationTimeLine.getKeyFrames().add(currentPageKeyFrame);
        animationTimeLine.play();
        currentPageAnimationTimeLine.play();
        animationTimeLine.setOnFinished(actionEvent -> {
            stackPane.getChildren().remove(0);
            stackPane.getChildren().remove(0);
            scene.setRoot(root);
        });

    }

    public void registerUser() throws Exception {
        String username = registerUsernameInput.getText();
        String password = registerPasswordInput.getText();
        String nickname = registerNicknameInput.getText();
        if (password.equals("") || username.equals("") || nickname.equals(""))
            return;
        String result = ClientController.createUser(username,password,nickname);
        if (result.startsWith("Error"))
            Prompt.showMessage(result.substring(6), PromptType.Error);
        if (result.startsWith("Success"))
            Prompt.showMessage(result.substring(9), PromptType.Success);
    }
}