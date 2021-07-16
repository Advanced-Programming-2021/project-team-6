package view.menus;

import controller.ClientController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import model.User;
import view.MusicManager;

import java.io.IOException;

public class ScoreBoardView {

    public StackPane stackPane;
    public ImageView backButton;

    private TableView table  = new TableView();
    @FXML ScrollPane scrollPane;


    public void backToMainMenu() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick,false);
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }
    public void scoreBoard() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/ScoreBoard.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
        scrollPane = (ScrollPane) root.getChildren().get(4);
        showScoreboard();
    }

    public void showScoreboard() throws IOException {
        table = new TableView();
        User.Users.clear();
        String result = ClientController.scoreboard();
        String[] players = result.split("\n");
        for(String string : players){
            new User(Integer.parseInt(string.split("\\.")[0]) , string.split("\\.")[1] , Integer.parseInt(string.split("\\.")[2]));
        }
        table.setPrefHeight(800);
        table.setPrefWidth(500);
        TableColumn index = new TableColumn("Index");
        index.setCellValueFactory(new PropertyValueFactory<>("index"));
        TableColumn nickname = new TableColumn("Nickname");
        nickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        TableColumn point = new TableColumn("Point");
        point.setCellValueFactory(new PropertyValueFactory<>("point"));
        point.setSortable(false);
        index.setSortable(false);
        nickname.setSortable(false);
        table.getColumns().addAll(index ,nickname, point);
        table.getSortOrder().add(point);
        for (User user1 : User.Users) {
            table.getItems().add(user1);
        }
        table.sort();
        table.setEditable(false);
        scrollPane.setContent(table);
        scrollPane.pannableProperty().set(true);
        scrollPane.vbarPolicyProperty().set(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    }
}