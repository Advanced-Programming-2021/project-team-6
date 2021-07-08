package view.menus;

import controller.ClientController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import view.MusicManager;

import java.io.IOException;

public class ScoreBoardView {

    public StackPane stackPane;
    public ImageView backButton;


    public void backToMainMenu() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick,false);
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }

    public void showScoreboard() throws IOException {
        String result = ClientController.scoreboard();
        String[] players = result.split("\n");

        Pane root = FXMLLoader.load(getClass().getResource("/fxml/ScoreBoard.fxml"));

        int y = 143;
        for (int i = 0; i<5 ; i++) {

            Text text = new Text();
            text.setText(players[i]);
            text.setFont(new Font("NPIOuj", 24));
            text.setLayoutY(y + i*107 + 77/2);
            text.setLayoutX(915);
            text.setFill(new Color(0.75, 0.3, 0.3, 1));
            if (players[i].split(" ")[1].equals(ClientController.username+":"))
                text.setFill(new Color(0, 0.3, 0.6, 1));
            root.getChildren().add(text);
        }

        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }




}
