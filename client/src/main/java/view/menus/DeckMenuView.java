package view.menus;

import controller.ClientController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.Card;
import model.Deck;
import view.Components.CardView;
import view.Prompt;
import view.PromptType;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class DeckMenuView {
    private static ImagePattern deckStyle;
    private static final ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    private ScrollPane mainDeck;
    @FXML
    private ScrollPane sideDeck;
    @FXML
    private ScrollPane inactiveCards;
    @FXML
    private ScrollPane decks;
    @FXML
    private Button deleteDeckButton;
    @FXML
    private Button addCardButton;
    @FXML
    private Button removeCardButton;
    @FXML
    private ToggleButton mainSwitch;
    @FXML
    private ToggleButton sideSwitch;
    @FXML
    private Button activateDeckButton;
    Deck selectedDeck;
    Card selectedCard;
    Boolean isCardInMain = false;
    private boolean cardIsInDeck;

    public void showDeckMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/DeckMenu.fxml")));
        WelcomeMenuView.mainStage.setScene(new Scene(root));
        deckStyle = new ImagePattern(new Image(Objects.requireNonNull(DeckMenuView.class.getResourceAsStream("image/backOfCard.jpg"))));
        mainDeck = (ScrollPane) root.getChildren().get(3);
        sideDeck = (ScrollPane) root.getChildren().get(4);
        inactiveCards = (ScrollPane) root.getChildren().get(5);
        decks = (ScrollPane) root.getChildren().get(6);
        deleteDeckButton = (Button) ((HBox) root.getChildren().get(7)).getChildren().get(2);
        addCardButton = (Button) ((HBox) root.getChildren().get(7)).getChildren().get(3);
        removeCardButton = (Button) ((HBox) root.getChildren().get(7)).getChildren().get(4);
        mainSwitch = (ToggleButton) ((HBox) root.getChildren().get(7)).getChildren().get(0);
        sideSwitch = (ToggleButton) ((HBox) root.getChildren().get(7)).getChildren().get(1);
        mainSwitch.setToggleGroup(toggleGroup);
        sideSwitch.setToggleGroup(toggleGroup);
        resetDecks();
    }

    public void backToMainMenu() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        WelcomeMenuView.mainStage.setScene(new Scene(root));
    }


    public void activateDeck(MouseEvent mouseEvent) throws IOException {
        try {
            ClientController.activateDeck(selectedDeck.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectedCard = null;
        resetButtons();
        resetMainDeck();
        resetDecks();
        resetSideDeck();
    }

    public VBox getCreateDeckButton(int i) {
        VBox vBox = new VBox();
        StackPane deckView = new StackPane();
        deckView.setMinSize(70, 100);
        Rectangle cardPicture = new Rectangle(70, 100, deckStyle);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.7);
        cardPicture.setEffect(colorAdjust);
        deckView.getChildren().add(cardPicture);
        deckView.getChildren().add(new Label("+"));
        deckView.getStyleClass().add("createDeckButton");
        Label deckName = new Label("Create Deck");
        deckName.setMaxWidth(70);
        deckView.getStyleClass().add("cardItems");
        vBox.getChildren().addAll(deckView, deckName);
        vBox.setMinSize(80, 130);
        vBox.setTranslateX(i * 80 + 10);
        vBox.setTranslateY(10);
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                createDeck();
            }
        });

        return vBox;
    }

    private void createDeck() {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Create Deck");
        inputDialog.setHeaderText("Create Deck");
        inputDialog.setContentText("Input name of new deck:");
        Optional<String> deckName = inputDialog.showAndWait();
        if (deckName.isPresent()) {
            try {
                if (deckName.get().isEmpty()) throw new Exception("this is not a valid deck name");
                ClientController.createDeck(deckName.get());
                resetButtons();
                resetMainDeck();
                resetDecks();
                resetSideDeck();
                Prompt.showMessage("deck created successfully!", PromptType.Success);
            } catch (Exception e) {
                Prompt.showMessage(e.getMessage(), PromptType.Error);
            }
        }

    }

    public void resetMainDeck() {
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setMaxWidth(580);
        int i = 0;
        if (selectedDeck != null)
            for (Card card : selectedDeck.getMainCards()) {
                pane.getChildren().add(getCardView(card, i++, true));
            }
        pane.setMinHeight(7 * 150);
        mainDeck.setContent(pane);
        mainDeck.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainDeck.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public void resetButtons() {
        deleteDeckButton.disableProperty().set(selectedDeck == null);
        addCardButton.disableProperty().set(selectedCard == null || selectedDeck == null || cardIsInDeck);
        removeCardButton.disableProperty().set(selectedCard == null || !cardIsInDeck);
        activateDeckButton.disableProperty().set(selectedDeck == null || selectedDeck.isActive);
    }

    public void resetSideDeck() {
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setMaxWidth(580);
        int i = 0;
        if (selectedDeck != null)
            for (Card card : selectedDeck.getSideCards()) {
                pane.getChildren().add(getCardView(card, i++, false));
            }
        pane.setMinHeight(7 * 150);
        sideDeck.setContent(pane);
        sideDeck.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sideDeck.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }


    public void resetDecks() throws IOException {
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setMinWidth(Integer.parseInt(ClientController.showMainDeck(selectedDeck.getName())) * 80);
        int i = 0;
        String[] allDecksNames = ClientController.showAllDecks().split(":");

        for (String deckName : allDecksNames) {
            Deck deck = new Deck(deckName);
            pane.getChildren().add(getDeckView(deck, i++));
        }
        pane.getChildren().add(getCreateDeckButton(i));
        decks.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        decks.setPannable(true);
        decks.setContent(pane);
    }

    public VBox getCardView(Card card, int i, Boolean position) {
        VBox vBox = new VBox();
        ImageView cardView = getCardRectangle(card);
        Label name = new Label(card.getName());
        name.setMaxWidth(70);
        cardView.getStyleClass().add("cardItems");
        vBox.getChildren().addAll(cardView, name);
        vBox.setTranslateX((i % 7) * 80 + 10);
        vBox.setTranslateY((i / 7) * 130 + 10);
        vBox.setMinSize(80, 130);
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    selectCard(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isCardInMain = position;
            }
        });
        return vBox;
    }

    public VBox getDeckView(Deck deck, int i) {
        VBox vBox = new VBox();
        Rectangle deckView = new Rectangle(70, 100, deckStyle);
        Label deckName = new Label(deck.getName());
        Label deckInfo = new Label(deck.getMainCards().size() + "/" + deck.getSideCards().size());
        deckName.setMaxWidth(70);
        deckInfo.setMaxWidth(70);
        deckView.getStyleClass().add("cardItems");
        vBox.getChildren().addAll(deckView, deckName, deckInfo);
        vBox.setMinSize(80, 130);
        vBox.setTranslateX(i * 80 + 10);
        vBox.setTranslateY(10);
        if (deck.isActive) vBox.getStyleClass().add("activeDeck");
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectDeck(deck);
            }
        });

        return vBox;
    }

    private void selectDeck(Deck deck) {
        selectedDeck = deck;
        resetButtons();
        resetMainDeck();
        resetSideDeck();
    }

    private void selectCard(Card card) throws IOException {
        selectedCard = card;
        resetButtons();
        resetMainDeck();
        resetDecks();
        resetSideDeck();
    }

    public void deleteDeck(MouseEvent mouseEvent) {
        try {
            ClientController.deleteDeck(selectedDeck.getName());
            selectedDeck = null;
            selectedCard = null;
            resetButtons();
            resetMainDeck();
            resetDecks();
            resetSideDeck();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CardView getCardRectangle(Card card) {
        CardView rectangle = new CardView();
        rectangle.setImage(card.getImage());
        rectangle.setFitHeight(200);
        rectangle.setFitWidth(90);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(1);
        dropShadow.setHeight(1);
        rectangle.setEffect(dropShadow);
        rectangle.setI(card.getI());
        rectangle.setJ(card.getJ());
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    selectCard(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return rectangle;
    }


}
