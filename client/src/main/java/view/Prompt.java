package view;


import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;

public class Prompt {

    public static void showMessage(String message, PromptType type) {
        Stage stage = new Stage();
        URL url = Prompt.class.getResource("/image/main10.jpg");
        ImageView background = new ImageView(new Image(url.toString()));

        Label label = new Label();
        Button button = new Button("OK");
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.alignmentProperty().set(Pos.CENTER);
        vBox.getChildren().add(label);
        vBox.getChildren().add(button);
        button.setOnMouseClicked(mouseEvent -> stage.close());
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(background);
        vBox.setLayoutX(65);
        vBox.setLayoutY(30);
        anchorPane.getChildren().add(vBox);

        Scene scene = new Scene(anchorPane);
        scene.setCursor(new ImageCursor(new Image(Prompt.class.getResource("/image/mouse.jpg").toString())));

        if (type.equals(PromptType.Error))
            scene.getStylesheets().add(Prompt.class.getResource("/CSS/Error.css").toExternalForm());
        if (type.equals(PromptType.Success))
            scene.getStylesheets().add(Prompt.class.getResource("/CSS/Success.css").toExternalForm());
        if (type.equals(PromptType.Message))
            scene.getStylesheets().add(Prompt.class.getResource("/CSS/Message.css").toExternalForm());
        label.setText(type + " : " + message);
        stage.setScene(scene);
        stage.setWidth(480);
        stage.setHeight(200);
        stage.show();
    }
}