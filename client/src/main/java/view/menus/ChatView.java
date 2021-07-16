package view.menus;

import controller.ClientController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Message;
import view.Prompt;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatView {

    public ScrollPane scrollPane;
    private TableView table;
    public TextArea message;


    public void backToMainMenu() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }

    public void initializeChat() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/ChatBox.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        scrollPane = (ScrollPane) root.getChildren().get(6);
        WelcomeMenuView.mainStage.setScene(scene);
        showChat();

    }

    public void sendMessage() throws IOException {
        if (message == null) return;

        String content = message.getText();
        ClientController.sendNewMessage(content);

    }


    public void showChat() throws IOException {
        Message.allMessages.clear();
        System.out.println(Message.allMessages.size());

        table = new TableView();


        String result = ClientController.getAllMessages();
        if (!result.equals("null")) {
            String[] messages = result.split("\n");
            for (String s : messages)
                new Message(s.split("\":\"")[0], s.split("\":\"")[1]);
        }

        table.setPrefHeight(450);
        table.setPrefWidth(369);

        TableColumn username = new TableColumn("Username");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn content = new TableColumn("Content");
        content.setCellValueFactory(new PropertyValueFactory<>("content"));
        content.setMaxWidth(1000);
        content.setMinWidth(1000);
        content.setSortable(false);
        username.setSortable(false);
        table.getColumns().addAll(username, content);

        for (Message message : Message.allMessages)
            table.getItems().add(message);

        table.setEditable(false);
        scrollPane.setContent(table);
        scrollPane.pannableProperty().set(true);
        scrollPane.vbarPolicyProperty().set(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    }

    public void addNewMessage(String messageFromServer) {
        Matcher messageMatcher = findMatcher(messageFromServer, "^new message (?<username>\\S+):" +
                "(?<content>.+) false$");
        if (!messageMatcher.find()) return;
        String username = messageMatcher.group("username");
        String content = messageMatcher.group("content");
        Message message = new Message(username, content);
        table.getItems().add(message);

    }

    private Matcher findMatcher(String input, String regex) {

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }


}
