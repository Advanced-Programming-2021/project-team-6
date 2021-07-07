package controller.menus;

import controller.ErrorChecker;
import models.Database;
import models.Deck;
import models.cards.Card;
import serverConection.Output;

import java.util.Objects;

public class DeckMenuController {

    private static DeckMenuController instance = null;

    private DeckMenuController() {
    }

    public static DeckMenuController getInstance() {
        return Objects.requireNonNullElseGet(instance, () -> (instance = new DeckMenuController()));
    }

    public String createDeck(String name, String token) {
        if (!ErrorChecker.isDeckNameUnique(name))
            return "Error";

        new Deck(name, MainMenuController.getInstance().loggedInUsers.get(token), true, true);
        return ("deck created successfully!");
    }

    public String deleteDeck(String name) {
        if (ErrorChecker.isDeckNameUnique(name))
            return "Error";

        Database.removeDeck(name);
        return ("deck deleted successfully!");
    }


    public String setActiveDeck(String name, String token) {
        Deck deck = null;
        boolean isPermitted = ErrorChecker.doesDeckExist(name)
                && ErrorChecker.doesDeckBelongToPlayer(deck = Database.getInstance().getDeckByName(name), MainMenuController.getInstance().loggedInUsers.get(token));
        if (isPermitted) {
            MainMenuController.getInstance().loggedInUsers.get(token).setActiveDeck(deck);

            return ("deck activated successfully!");
        }
        return "Error";
    }

    public String addCardToDeck(String cardName, String deckName, String token, boolean isMain) {
        Card card = null;
        Deck deck = null;
        boolean isPermitted = ErrorChecker.doesCardExist(cardName)
                && ErrorChecker.doesDeckExist(deckName)
                && ErrorChecker.doesDeckBelongToPlayer(deck = Database.getInstance().getDeckByName(deckName), MainMenuController.getInstance().loggedInUsers.get(token))
                && ((isMain) ? ErrorChecker.doesDeckHaveSpace(deck) : ErrorChecker.doesSideDeckHaveSpace(deck))
                && ErrorChecker.isNumberOfCardsInDeckLessThanFour(deck, card = Database.getInstance().getCardByName(cardName))
                && ErrorChecker.doesPlayerHaveEnoughCards(card, MainMenuController.getInstance().loggedInUsers.get(token));
        if (isPermitted) {
            MainMenuController.getInstance().loggedInUsers.get(token).getAllPlayerCard().moveCardTo(deck, card, true, isMain);
            return ("card added to deck successfully!");
        }
        return "Error";
    }

    public String removeCardFromDeck(String cardName, String deckName, String token, boolean isMain) {
        Card card;
        Deck deck = null;
        boolean isPermitted = ErrorChecker.doesCardExist(cardName)
                && ErrorChecker.doesDeckExist(deckName)
                && ErrorChecker.doesDeckBelongToPlayer(deck = Database.getInstance().getDeckByName(deckName), MainMenuController.getInstance().loggedInUsers.get(token));
        if (isPermitted) {
            card = Database.getInstance().getCardByName(cardName);
            deck.moveCardTo(MainMenuController.getInstance().loggedInUsers.get(token).getAllPlayerCard(), card, isMain, true);
            Output.getInstance().showMessage("card removed from deck successfully!");
        }
        return "Error";
    }

    public String showAllDecks(String token) {
        StringBuilder string = new StringBuilder();
        for (Deck deck : MainMenuController.getInstance().loggedInUsers.get(token).getAllDeck())
            string.append(deck.getName()).append(":");
        return String.valueOf(string);
    }

    public String showDeck(String name, String token, boolean isMain) {
        if (!ErrorChecker.doesDeckExist(name))
            return "Error";
        Deck deck = Database.getInstance().getDeckByName(name);
        if (!ErrorChecker.doesDeckBelongToPlayer(deck, MainMenuController.getInstance().loggedInUsers.get(token)))
            return "Error";
        return (deck.toString(isMain));
    }
}
