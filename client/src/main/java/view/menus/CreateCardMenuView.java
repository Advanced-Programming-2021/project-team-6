package view.menus;

import controller.ClientController;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import view.MusicManager;
import view.Prompt;
import view.PromptType;

import java.io.IOException;
import java.net.URL;

public class CreateCardMenuView {
    public TextField cardMonsterName;
    public TextField attackPower;
    public TextField defencePower;

    public TextField pictureNameMonster;
    public TextField levelMonster;
    public Text costMonster;
    public ImageView levelPictureMonster;
    public Text attackedView;
    public Text defenceView;
    public ImageView pictureViewMonster;
    public TextField actionMonster;
    public TextArea descriptionMonster;

    public TextField cardSpellName;
    public TextField actionSpell;
    public TextField pictureNameSpell;
    public ImageView pictureViewSpell;
    public TextArea descriptionSpell;
    public Text costSpell;
    Pane root;
    Scene scene;

    public void backToMainMenu() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }

    public void showCreateCardMenu() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        root = FXMLLoader.load(getClass().getResource("/fxml/CreateCardMenu.fxml"));
        scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }

    public void showCreateMonsterCardMenu() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        root = FXMLLoader.load(getClass().getResource("/fxml/CreateMonsterCardMenu.fxml"));
        scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }

    public void showCreateSpellCardMenu() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        root = FXMLLoader.load(getClass().getResource("/fxml/CreateSpellCardMenu.fxml"));
        scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }


    public void calculateMonsterCost() {
        if (defencePower == null || attackPower == null || levelMonster == null
                || cardMonsterName == null || pictureNameMonster == null || descriptionMonster == null) return;

        String costValue = String.valueOf((Integer.parseInt(defencePower.getText()) + Integer.parseInt(attackPower.getText())) * Integer.parseInt(levelMonster.getText()) / 2);


        costMonster.setText(costValue);
        defenceView.setText(defencePower.getText());
        attackedView.setText(attackPower.getText());

        //for (int i = 0; i<Integer.parseInt(level.getText()); i++)

        URL url = getClass().getResource("/image/profilePicture/" + pictureNameMonster.getText() + ".jpg");
        pictureViewMonster.setImage(new Image(url.toString()));
    }

    public void createMonsterCard() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        String price = String.valueOf((Integer.parseInt(defencePower.getText()) + Integer.parseInt(attackPower.getText())) * Integer.parseInt(levelMonster.getText()) / 2);
        String result = ClientController.createMonsterCard(cardMonsterName.getText(), attackPower.getText(), defencePower.getText(),
                descriptionMonster.getText(), levelMonster.getText(), actionMonster.getText(), price);


        if (result.startsWith("Error"))
            Prompt.showMessage(result.substring(7), PromptType.Error);
        if (result.startsWith("Success"))
            Prompt.showMessage(result.substring(10), PromptType.Success);

    }

    public void calculateSpellCost() {
        if (cardSpellName == null || pictureNameSpell == null || descriptionSpell == null) return;

        String costValue = String.valueOf(numberOfChar(actionSpell.getText(), '/') * 1000);

        costSpell.setText(costValue);

        //for (int i = 0; i<Integer.parseInt(level.getText()); i++)

        URL url = getClass().getResource("/image/profilePicture/" + pictureNameSpell.getText() + ".jpg");
        pictureViewSpell.setImage(new Image(url.toString()));
    }

    public void createSpellCard() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        String price = String.valueOf((numberOfChar(actionSpell.getText(), '/') + 1) * 1000);
        String result = ClientController.createSpellCard(cardSpellName.getText(),
                descriptionSpell.getText(), actionSpell.getText(), price);

        if (result.startsWith("Error"))
            Prompt.showMessage(result.substring(7), PromptType.Error);
        if (result.startsWith("Success"))
            Prompt.showMessage(result.substring(10), PromptType.Success);

    }

    private int numberOfChar(String line, char character) {
        String replaceLine = line;
        return replaceLine.length() - line.replace("/", "").length();
    }
}
