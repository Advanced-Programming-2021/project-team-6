package view.menus;

import controller.ClientController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import model.User;
import view.Components.CardView;
import view.Prompt;
import view.PromptType;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class DeckMenuView {
    private static final ToggleGroup toggleGroup = new ToggleGroup();
    private static ImagePattern deckStyle;
    @FXML private ScrollPane mainDeck;
    @FXML private ScrollPane sideDeck;
    @FXML private ScrollPane inactiveCards;
    @FXML private ScrollPane decks;
    @FXML private Button deleteDeckButton;
    @FXML private Button addCardButton;
    @FXML private Button removeCardButton;
    @FXML private ToggleButton mainSwitch;
    @FXML private ToggleButton sideSwitch;
    @FXML private Button activateDeckButton;
    private Deck selectedDeck;
    private Card selectedCard;
    private Boolean isCardInMain = false;
    private Boolean isMain;
    private boolean cardIsInDeck;

    public void showDeckMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/DeckMenu.fxml")));
        Scene scene = new Scene(root);
        WelcomeMenuView.mainStage.setScene(scene);

        deckStyle = new ImagePattern(new Image(Objects.requireNonNull(DeckMenuView.class.getResourceAsStream("/image/backOfCard.jpg"))));
        mainDeck = (ScrollPane) ((AnchorPane) root.getChildren().get(0)).getChildren().get(3);
        sideDeck = (ScrollPane) ((AnchorPane) root.getChildren().get(0)).getChildren().get(4);
        inactiveCards = (ScrollPane) ((AnchorPane) root.getChildren().get(0)).getChildren().get(5);
        decks = (ScrollPane) ((AnchorPane) root.getChildren().get(0)).getChildren().get(6);
        deleteDeckButton = (Button) ((HBox) ((AnchorPane) root.getChildren().get(0)).getChildren().get(7)).getChildren().get(2);
        addCardButton = (Button) ((HBox) ((AnchorPane) root.getChildren().get(0)).getChildren().get(7)).getChildren().get(3);
        removeCardButton = (Button) ((HBox) ((AnchorPane) root.getChildren().get(0)).getChildren().get(7)).getChildren().get(4);
        mainSwitch = (ToggleButton) ((HBox) ((AnchorPane) root.getChildren().get(0)).getChildren().get(7)).getChildren().get(0);
        sideSwitch = (ToggleButton) ((HBox) ((AnchorPane) root.getChildren().get(0)).getChildren().get(7)).getChildren().get(1);
        activateDeckButton = (Button) ((HBox) ((AnchorPane) root.getChildren().get(0)).getChildren().get(7)).getChildren().get(5);
        mainSwitch.setToggleGroup(toggleGroup);
        sideSwitch.setToggleGroup(toggleGroup);
        resetDecks();
    }

    public void backToMainMenu() throws IOException {
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        WelcomeMenuView.mainStage.setScene(new Scene(root));
    }

    public void addCard() {

        if (isMain == null) try {
            Prompt.showMessage("please select main deck or side deck to add card to", PromptType.Error);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            if (selectedDeck == null) Prompt.showMessage("please select a deck", PromptType.Error);
            if (isMain) ClientController.addCardToMainDeck(selectedDeck.getName(), selectedCard.getName());
            else ClientController.addCardToSideDeck(selectedDeck.getName(), selectedCard.getName());
            //selectedCard = null;
            resetButtons();
            resetDecks();
            resetSideDeck();
            resetMainDeck();
            resetInactiveCards();
        } catch (Exception e) {
            Prompt.showMessage("Error", PromptType.Error);
        }
    }

    public void removeCard(MouseEvent mouseEvent) throws Exception {
        try {
            if (selectedDeck == null) Prompt.showMessage("Please select a deck to add card to first" , PromptType.Error);
            if (selectedCard == null) Prompt.showMessage("please select a card to remove", PromptType.Error);
            if (isMain) ClientController.removeCardFromMainDeck(selectedDeck.getName(), selectedCard.getName());
            else ClientController.removeCardFromSideDeck(selectedDeck.getName(), selectedCard.getName());
            //selectedCard = null;
            resetButtons();
            resetDecks();
            resetSideDeck();
            resetMainDeck();
            resetInactiveCards();
        } catch (IOException e) {
            Prompt.showMessage("Error", PromptType.Error);
        }
    }


    public void activateDeck(MouseEvent mouseEvent) throws IOException {
        try {
            if(selectedDeck == null) return;
            ClientController.activateDeck(selectedDeck.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //selectedCard = null;
        resetButtons();
        resetMainDeck();
        resetDecks();
        resetSideDeck();
        resetInactiveCards();
    }

    public VBox getCreateDeckButton(int i) {
        VBox vBox = new VBox();
        StackPane deckView = new StackPane();
        deckView.setMinSize(70, 100);
        Rectangle cardPicture = new Rectangle(70, 100, deckStyle);
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
                resetInactiveCards();
                Prompt.showMessage("deck created successfully!", PromptType.Success);
            } catch (Exception e) {
                Prompt.showMessage(e.getMessage(), PromptType.Error);
            }
        }

    }

    public void resetInactiveCards() {
        VBox outside = new VBox();
        outside.setSpacing(10);
        outside.alignmentProperty().set(Pos.CENTER);
        for (Card card : User.inactiveCards) {
            outside.getChildren().add(getInactiveCardView(card));
        }
        inactiveCards.setContent(outside);
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

    public VBox getInactiveCardView(Card card) {
        VBox vBox = new VBox();
        CardView cardView = getCardRectangle(card);
        Label name = new Label(card.getName());
        name.setMaxWidth(70);
        cardView.getStyleClass().add("cardItems");
        vBox.getChildren().addAll(cardView, name);
        vBox.setMinSize(70, 100);
        vBox.setTranslateX(10);
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
        if (selectedDeck != null) {
            pane.setMinWidth((ClientController.showMainDeck(selectedDeck.getName()).split(":").length) * 80);
        }
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
        resetInactiveCards();
    }

    private void selectCard(Card card) throws IOException {
        selectedCard = card;
        resetButtons();
        resetMainDeck();
        resetDecks();
        resetSideDeck();
        resetInactiveCards();
    }

    public void deleteDeck(MouseEvent mouseEvent) {
        try {
            ClientController.deleteDeck(selectedDeck.getName());
            //selectedDeck = null;
            //selectedCard = null;
            resetButtons();
            resetMainDeck();
            resetDecks();
            resetSideDeck();
            resetInactiveCards();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

