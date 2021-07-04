package view.menus;

import controller.ClientController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;

public class ScoreBoardView {
    
    public StackPane stackPane;
    public ImageView backButton;


    public void backToMainMenu() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        WelcomeMenuView.mainStage.setScene(new Scene(root));
    }

    public void showScoreboard() throws IOException {
        String result = ClientController.scoreboard();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/ScoreBoard.fxml"));
        
        Text text = new Text();
        text.setText(result);
        text.setFont(new Font("NPIOuj", 24));
        text.setLayoutY(406);
        text.setLayoutX(577);
        text.setFill(new Color(0.5,0.25,0.25,1));

        root.getChildren().add(text);

        WelcomeMenuView.mainStage.setScene(new Scene(root));
    }




}
