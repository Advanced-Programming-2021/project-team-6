package view.menus;

import controller.ClientController;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import view.MusicManager;
import view.Prompt;
import view.PromptType;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class ProfileMenuView {

    public TextField newPassword;
    public TextField newNickname;
    public TextField oldPassword;

    public void backToMainMenu() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick,false);
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));

        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }

    public void showProfile() throws IOException, URISyntaxException {
        String result = ClientController.profile();
        String[] data = result.split(" ");

        Pane root = FXMLLoader.load(getClass().getResource("/fxml/ProfileMenu.fxml"));

        Text username = new Text();
        username.setText(data[1]);
        username.setFont(new Font("NPIOuj", 35));
        username.setLayoutY(106);
        username.setLayoutX(532);
        username.setFill(new Color(0.854, 0.639, 0.639, 1));
        root.getChildren().add(username);

        Text nickname = new Text();
        nickname.setText(data[2]);
        nickname.setFont(new Font("NPIOuj", 35));
        nickname.setLayoutY(198);
        nickname.setLayoutX(532);
        nickname.setFill(new Color(0.854, 0.639, 0.639, 1));
        root.getChildren().add(nickname);

        URL url = getClass().getResource("/image/profilePicture/" + data[3] + ".jpg");
        ImageView photo = new ImageView(new Image(url.toString()));
        photo.setLayoutX(100);
        photo.setLayoutY(50);
        photo.setFitHeight(200);
        photo.setFitWidth(200);
        root.getChildren().add(photo);

        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);

    }

    public void changePassword() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick,false);

        if (newPassword != null && oldPassword != null) {
            String result = ClientController.changePassword(oldPassword.getText(), newPassword.getText());
            if (result.startsWith("Error"))
                Prompt.showMessage(result.substring(7), PromptType.Error);
            else
                Prompt.showMessage(result.substring(9), PromptType.Success);
        }

    }

    public void changeNickname() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick,false);

        if (newNickname != null) {
            String result = ClientController.changeNickname(newNickname.getText());
            if (result.startsWith("Error"))
                Prompt.showMessage(result.substring(7), PromptType.Error);
            else
                Prompt.showMessage(result.substring(9), PromptType.Success);
        }

    }

}
