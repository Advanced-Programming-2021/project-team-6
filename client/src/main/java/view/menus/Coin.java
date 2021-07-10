package view.menus;

import controller.ClientController;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.IOException;


public class Coin {


    public ImageView coinCircle;
    public ImageView tossButton;
    private boolean isForGame = false;
    public static String flip;


    Image imageTail = new Image(getClass().getResource("/image/tail.jpg").toString());
    Image imageHead = new Image(getClass().getResource("/image/head.jpg").toString());


    public void showCoin() throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/CoinPage.fxml"));
        Scene scene = new Scene(root);

        Text username1 = new Text(ClientController.username);
        username1.setFont(new Font(18));
        username1.setEffect(new Glow());
        username1.setTextAlignment(TextAlignment.CENTER);
        username1.setLayoutX(636);
        username1.setLayoutY(202);

        Text username2 = new Text(ClientController.opponentUsername);
        username2.setFont(new Font(18));
        username2.setEffect(new Glow());
        username2.setTextAlignment(TextAlignment.CENTER);
        username2.setLayoutX(636);
        username2.setLayoutY(277);

        root.getChildren().add(username1);
        root.getChildren().add(username2);

        WelcomeMenuView.mainStage.setScene(scene);

    }

    public void start() {

        RotateTransition rotator = createRotator(coinCircle);
        rotator.play();
        rotator.setOnFinished(event -> {
            if (Coin.flip.equals("head")) {
                coinCircle.setImage(imageHead);
                //go to game
            } else {
                coinCircle.setImage(imageTail);
                //go to game
                try {
                    ClientController.changeTurn();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ScaleTransition scaleTransition = createScaleTransition();
            scaleTransition.play();

        });

    }


    private ScaleTransition createScaleTransition() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(400), coinCircle);
        scaleTransition.setByY(0.5);
        scaleTransition.setByX(0.5);

        return scaleTransition;
    }

    private RotateTransition createRotator(Node card) {
        RotateTransition rotator = new RotateTransition(Duration.millis(400), card);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(360);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(10);

        return rotator;
    }


}