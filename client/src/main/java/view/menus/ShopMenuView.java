package view.menus;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Card;
import view.Components.CardView;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ShopMenuView {
    public GridPane gameBoard;
    public Button backButton;
    public BorderPane borderPane;
    private Card[][] board;
    private CardView[][] cards;
    public Button[][] buyButtons;
    public Button exitGameButton;
    public void backToMainMenu() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        WelcomeMenuView.mainStage.setScene(new Scene(root));
    }
    public void showShop(){
        Scene scene = backButton.getScene();
        setGameBoardCards();
        showCards();
    }

    private void showCards() {
        cards = new CardView[board.length][board[0].length];
        for (int i = 0 ; i < board.length ; i++) {
            for (int j = 0 ; j < board[i].length ; j++) {
                CardView rectangle = getCardRectangle(board[i][j]);
                gameBoard.add(rectangle , i , j);
                cards[i][j] = rectangle;
            }
        }
    }

    public void setGameBoardCards() {
        this.board = new Card[13][5];
        this.buyButtons = new Button[13][5];
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
                "image/Cards/Monsters/Horn-Imp.jpg",
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
        List<String> fronts =  Arrays.asList(labels);
        int cnt = 0;
        for (int i = 0 ; i < board.length ; i++) {
            for (int j = 0 ; j < board[0].length ; j++)
                board[i][j] = new Card(Objects.requireNonNull(getClass().getResource(fronts.get(cnt++))).toExternalForm(), i, j);
        }
    }

    public Card[][] getBoard() {
        return board;
    }

    private CardView getCardRectangle(Card card) {
        CardView rectangle = new CardView();
        rectangle.setFill(card.getImage());
        rectangle.setHeight(105.6);
        rectangle.setWidth(69.1);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(0);
        dropShadow.setHeight(0);
        rectangle.setEffect(dropShadow);
        rectangle.setI(card.getI());
        rectangle.setJ(card.getJ());
        return rectangle;
    }


}
