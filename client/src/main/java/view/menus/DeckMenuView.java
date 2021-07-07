package view.menus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class DeckMenuView{
        @FXML

        public void backToMainMenu() throws IOException {
            Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
            WelcomeMenuView.mainStage.setScene(new Scene(root));
        }

}
