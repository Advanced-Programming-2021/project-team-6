package view.menus;

import controller.ClientController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import view.Prompt;
import view.PromptType;

import java.io.IOException;

public class ImpExpMenuView {

    public TextField importCardName;
    public TextField exportCardName;

    public void backToMainMenu() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        WelcomeMenuView.mainStage.setScene(new Scene(root));
    }

    public void showImpExpMenu() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/ImpExpMenu.fxml"));
        WelcomeMenuView.mainStage.setScene(new Scene(root));
    }

    public void importCard() throws IOException {
        if (importCardName != null){
            String result = ClientController.importCard(importCardName.getText());

            if (result.startsWith("Success"))
                Prompt.showMessage(result.substring(10), PromptType.Success);
            if (result.startsWith("Error"))
                Prompt.showMessage(result.substring(7),PromptType.Error);

        }

    }

    public void exportCard() throws IOException {
        if (exportCardName != null){
            String result = ClientController.exportCard(exportCardName.getText());

            if (result.startsWith("Success"))
                Prompt.showMessage(result.substring(10), PromptType.Success);
            if (result.startsWith("Error"))
                Prompt.showMessage(result.substring(7),PromptType.Error);
        }
    }
}
