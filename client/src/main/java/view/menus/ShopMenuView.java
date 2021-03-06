package view.menus;

import controller.ClientController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import model.Card;
import view.Components.CardView;
import view.MusicManager;
import view.Prompt;
import view.PromptType;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ShopMenuView {
    @FXML
    static ScrollPane scrollPane;
    static Card selectedCard;
    static Text cardDescriptionText;
    static HashMap<Card, Integer> boughtCards = new HashMap<>();
    public ImageView backButton;
    @FXML
    public Button buyButton;
    @FXML
    public Button increaseMoney;
    @FXML
    Text cardDescription;
    @FXML
    ImageView imageOfSelectedCard;
    private Button staticBuyButton;
    private Card[][] board;
    private CardView[][] cards;

    public void backToMainMenu() throws IOException {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        WelcomeMenuView.mainStage.setScene(scene);
    }

    public void showShop() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/ShopMenu.fxml"));
        Scene scene = new Scene(root);
        scene.setCursor(new ImageCursor(new Image(getClass().getResource("/image/mouse.jpg").toString())));
        scrollPane = (ScrollPane) ((AnchorPane) root.getChildren().get(0)).getChildren().get(3);
        imageOfSelectedCard = ((ImageView) ((HBox) ((Pane) ((AnchorPane) root.getChildren().get(0)).getChildren().get(4)).getChildren().get(0)).getChildren().get(0));
        cardDescriptionText = ((Text) ((HBox) ((Pane) ((AnchorPane) root.getChildren().get(0)).getChildren().get(4)).getChildren().get(0)).getChildren().get(1));
        staticBuyButton = (Button) ((AnchorPane) root.getChildren().get(0)).getChildren().get(5);
        //increaseMoney = (Button) ((AnchorPane) root.getChildren().get(0)).getChildren().get(6);
        setGameBoardCards();
        showCards();
        WelcomeMenuView.mainStage.setScene(scene);
    }

    private void showCards() {
        cards = new CardView[board.length][board[0].length];
        GridPane gameBoard = new GridPane();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boughtCards.putIfAbsent(board[i][j], 20);
                CardView rectangle = getCardRectangle(board[i][j]);
                gameBoard.add(getCardVbox(board[i][j], rectangle), i, j);
                cards[i][j] = rectangle;
            }
        }
        scrollPane.setContent(gameBoard);
        scrollPane.pannableProperty().set(true);
        scrollPane.vbarPolicyProperty().set(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public void setGameBoardCards() {
        this.board = new Card[5][13];
        fillBoard();
    }

    private void fillBoard() {
        String[] labels = new String[]{
                "image/Cards/Monsters/Alexandrite Dragon.jpg",
                "image/Cards/Monsters/Axe Raider.jpg",
                "image/Cards/Monsters/Baby Dragon.jpg",
                "image/Cards/Monsters/Battle OX.jpg",
                "image/Cards/Monsters/Battle Warrior.jpg",
                "image/Cards/Monsters/Beast King Barbaros.jpeg",
                "image/Cards/Monsters/Bitron.jpg",
                "image/Cards/Monsters/Blue-Eyes White Dragon.jpg",
                "image/Cards/Monsters/Command Knight.jpg",
                "image/Cards/Monsters/Crab Turtle.jpg",
                "image/Cards/Monsters/Crawling Dragon.jpg",
                "image/Cards/Monsters/Curtain Of The Dark Ones.jpg",
                "image/Cards/Monsters/Dark Blade.jpg",
                "image/Cards/Monsters/Dark Magician.jpg",
                "image/Cards/Monsters/Exploder Dragon.jpg",
                "image/Cards/Monsters/Feral Imp.jpg",
                "image/Cards/Monsters/Fireyarou.jpg",
                "image/Cards/Monsters/Flame Manipulator.jpg",
                "image/Cards/Monsters/Gate Guardian.jpg",
                "image/Cards/Monsters/Haniwa.jpg",
                "image/Cards/Monsters/Herald Of Creation.jpg",
                "image/Cards/Monsters/Hero Of The East.jpg",
                "image/Cards/Monsters/Horn Imp.jpg",
                "image/Cards/Monsters/Leotron.jpg",
                "image/Cards/Monsters/Man-Eater Bug.jpg",
                "image/Cards/Monsters/Marshmallon.jpg",
                "image/Cards/Monsters/Mirage Dragon.jpg",
                "image/Cards/Monsters/Silver Fang.jpg",
                "image/Cards/Monsters/Skull Guardian.jpg",
                "image/Cards/Monsters/Slot Machine.jpg",
                "image/Cards/Monsters/Spiral Serpent.jpg",
                "image/Cards/Monsters/Suijin.jpg",
                "image/Cards/Monsters/Terratiger The Empowered Warrior.jpg",
                "image/Cards/Monsters/The Tricky.jpg",
                "image/Cards/Monsters/Warrior Dai Grepher.jpg",
                "image/Cards/Monsters/Wattaildragon.jpg",
                "image/Cards/Monsters/Wattkid.jpg",
                "image/Cards/Monsters/Yomi Ship.jpg",
                "image/Cards/Spells/Black Pendant.jpg",
                "image/Cards/Spells/Change Of Heart.jpg",
                "image/Cards/Spells/Closed Forest.jpg",
                "image/Cards/Spells/Dark Hole.jpg",
                "image/Cards/Spells/Forest.jpg",
                "image/Cards/Spells/Harpie's Feather Duster.jpg",
                "image/Cards/Spells/Magnum Shield.jpg",
                "image/Cards/Spells/Monster Reborn.jpg",
                "image/Cards/Spells/Mystical Space Typhoon.jpg",
                "image/Cards/Spells/Pot Of Greed.jpg",
                "image/Cards/Spells/Raigeki.jpg",
                "image/Cards/Spells/Ring Of Defense.jpg",
                "image/Cards/Spells/Spell Absorption.jpg",
                "image/Cards/Spells/Supply Squad.jpg",
                "image/Cards/Spells/Sword Of Dark Destruction.png",
                "image/Cards/Spells/Terraforming.jpg",
                "image/Cards/Spells/Twin Twisters.jpg",
                "image/Cards/Spells/Umiiruka.jpg",
                "image/Cards/Spells/United We Stand.jpg",
                "image/Cards/Spells/Yami.jpg",
                "image/Cards/Traps/Call Of The Haunted.jpg",
                "image/Cards/Traps/Magic Cylinder.jpg",
                "image/Cards/Traps/Magic Jammer.jpg",
                "image/Cards/Traps/Mirror Force.jpg",
                "image/Cards/Traps/Negate Attack.jpg",
                "image/Cards/Traps/Time Seal.jpg",
                "image/Cards/Traps/Torrential Tribute.jpg",
                "image/Cards/Traps/Trap Hole.jpg",
        };
        List<String> fronts = Arrays.asList(labels);
        int cnt = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = new Card(fronts.get(cnt).split("/")[3].split("\\.")[0], getClass().getResource("/" + fronts.get(cnt++)).toExternalForm(), i, j);
            }
        }
    }

    public Card[][] getBoard() {
        return board;
    }

    public CardView getCardRectangle(Card card) {
        CardView rectangle = new CardView();
        rectangle.setImage(card.getImage());
        rectangle.setFitHeight(200);
        rectangle.setFitWidth(95);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(10);
        dropShadow.setHeight(10);
        rectangle.setEffect(dropShadow);
        rectangle.setI(card.getI());
        rectangle.setJ(card.getJ());
        return rectangle;
    }

    public VBox getCardVbox(Card card, CardView rectangle) {
        VBox vBox = new VBox();
        Label name = new Label(card.getName());
        name.setMaxWidth(80);
        Label count;
        if(boughtCards.containsKey(card)) {
            count = new Label(boughtCards.get(card).toString());
        }
        else count = new Label("0");
        vBox.getChildren().addAll(rectangle, name, count);
        vBox.setMinSize(120, 180);
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    selectCard(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return vBox;
    }

    private void selectCard(Card card) throws IOException {
        selectedCard = card;
        showDetails();
    }

    private void showDetails() throws IOException {
        imageOfSelectedCard.setImage(getCardRectangle(selectedCard).getImage());
        cardDescriptionText.setText(ClientController.getDescription(selectedCard.getName()));
        String result = ClientController.buyCard(selectedCard.getName(), true);
        staticBuyButton.disableProperty().set(result.split(":")[1].equals("not enough money"));

    }

    public void buy() {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        try {
            String result = ClientController.buyCard(selectedCard.getName(), false);
            if (!result.split(":")[0].equals("not enough money")) {
                Prompt.showMessage("Card Purchased Successfully", PromptType.Success);
                Integer counter = boughtCards.get(selectedCard);
                boughtCards.replace(selectedCard , counter, counter-1);
            } else
                Prompt.showMessage("You Don't Have Enough Money", PromptType.Error);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void increaseMoney() {
        MusicManager.playMusic(MusicManager.mouseClick, false);
        try {
            String result = ClientController.increaseMoney("1000");
            if (result.split(":")[0].equals("Success")) {
                Prompt.showMessage("Money Increased!", PromptType.Success);
            } else
                Prompt.showMessage("Error", PromptType.Error);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
