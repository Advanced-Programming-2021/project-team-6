package view.menus;

import controller.ClientController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import view.Prompt;
import view.PromptType;

import java.io.IOException;
import java.net.URL;

public class CreateCardMenuView {
    public TextField cardName;
    public TextField attackPower;
    public TextField defencePower;
    public TextField description;
    public TextField pictureName;
    public TextField level;
    public Text cost;
    public ImageView levelPicture;
    public Text attackedView;
    public Text defenceView;
    public ImageView pictureView;
    Pane root;
    Scene scene;

    public void backToMainMenu() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        WelcomeMenuView.mainStage.setScene(new Scene(root));
    }

    public void showCreateCardMenu() throws IOException {
        root = FXMLLoader.load(getClass().getResource("/fxml/CreateCardMenu.fxml"));
        scene = new Scene(root);
        WelcomeMenuView.mainStage.setScene(scene);
    }


    public void calculateCost() {
        if (defencePower == null || attackPower == null || level == null
                || cardName == null || pictureName == null || description == null) return;

        String costValue = String.valueOf((Integer.parseInt(defencePower.getText()) + Integer.parseInt(attackPower.getText())) * Integer.parseInt(level.getText()) / 2);


        cost.setText(costValue);
        defenceView.setText(defencePower.getText());
        attackedView.setText(attackPower.getText());

        //for (int i = 0; i<Integer.parseInt(level.getText()); i++)

        URL url = getClass().getResource("/image/profilePicture/" + pictureName.getText() + ".jpg");
        pictureView.setImage(new Image(url.toString()));
    }

    public void createCard() throws IOException {

        String result = ClientController.createCard(cardName.getText(), attackPower.getText(), defencePower.getText(),
                description.getText(),level.getText());

        if (result.startsWith("Error"))
            Prompt.showMessage(result.substring(7), PromptType.Error);
        if(result.startsWith("Success"))
            Prompt.showMessage(result.substring(10), PromptType.Success);

    }
}
