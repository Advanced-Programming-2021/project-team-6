package view.menus;

import controller.ClientController;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ChatView {

    private TableView table  = new TableView();
    public TextArea message;
    public Pane root;

    public void backToMainMenu() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }

    public void sendMessage() throws IOException {
        if (message == null) return;

        String content = message.getText();
        ClientController.sendNewMessage(content);

        //add message to chat box

    }

    public void showChat() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/ChatBox.fxml"));
        root.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        TableColumn username = new TableColumn("Username");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn content = new TableColumn("Content");
        content.setCellValueFactory(new PropertyValueFactory<>("content"));
        content.setSortable(false);
        username.setSortable(false);
        table.getColumns().addAll(username,content);
        root.getChildren().add(table);

        WelcomeMenuView.mainStage.setScene(new Scene(root));
    }

    public void addNewMessage(String username,String content){

    }



}
