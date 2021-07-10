package model;

import controller.ClientController;

import java.io.IOException;
import java.util.ArrayList;

public class Deck {
    public ArrayList<Card> mainCards = new ArrayList<>();
    public ArrayList<Card> sideCards = new ArrayList<>();
    public Boolean isActive = false;
    String name;
    Boolean IsValid;

    public Deck(String name) throws IOException {
        this.name = name;
        String[] main = ClientController.showMainDeck(name).split(":");
        String[] side = ClientController.showSideDeck(name).split(":");
        for(String string : main){
            mainCards.add(new Card(string,"/image/Cards/" + string + ".jpg" ));
        }
        for(String string : side){
            sideCards.add(new Card(string,"/image/Cards/" + string + ".jpg" ));
        }
    }

    public ArrayList<Card> getMainCards() {
        if (mainCards == null)
            return (mainCards = new ArrayList<>());
        return mainCards;
    }

    public void setMainCards(ArrayList<Card> mainCards) {
        this.mainCards = mainCards;
    }

    public ArrayList<Card> getSideCards() {
        return sideCards;
    }

    public void setSideCards(ArrayList<Card> sideCards) {
        this.sideCards = sideCards;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActivation(Boolean active) {
        isActive = active;
    }

    public void setValid(Boolean valid) {
        IsValid = valid;
    }

    public void addCard(Card card, boolean shouldBeAddedToMain) {
        if (shouldBeAddedToMain)
            mainCards.add(card);
        else
            sideCards.add(card);
    }

    public void moveCardTo(Deck destination, Card card, boolean isMainForOrigin, boolean isMainForDestination) {
        removeCard(card, isMainForOrigin);
        destination.addCard(card, isMainForDestination);
    }

    public void moveCardToForGame(Deck destination, Card card, boolean isMainForOrigin, boolean isMainForDestination) {
        removeCardForGame(card, isMainForOrigin);
        destination.addCard(card, isMainForDestination);
    }

    public boolean hasCard(Card card, boolean isMain) {
        if (isMain) {
            for (Card cardInMain : mainCards)
                if (cardInMain.getName().equals(card.getName()))
                    return true;

        } else
            for (Card cardInSide : sideCards)
                if (cardInSide.getName().equals(card.getName())) {
                    return true;
                }
        return false;
    }

    public void removeCard(Card card, boolean shouldBeRemovedFromMain) {
        if (shouldBeRemovedFromMain) {
            for (Card cardInMain : mainCards)
                if (cardInMain.getName().equals(card.getName())) {
                    mainCards.remove(cardInMain);
                    return;
                }
        } else
            for (Card cardInSide : sideCards)
                if (cardInSide.getName().equals(card.getName())) {
                    sideCards.remove(cardInSide);
                    return;
                }
    }

    public void removeCardForGame(Card card, boolean shouldBeRemovedFromMain) {
        if (shouldBeRemovedFromMain)
            mainCards.remove(card);
        else
            sideCards.remove(card);
    }

    public int getNumberOfCardsInDeck(Card card) {
        int count = 0;
        for (Card cardInDeck : mainCards) {
            if (cardInDeck.getName().equals(card.getName()))
                count++;
        }
        for (Card cardInDeck : sideCards) {
            if (cardInDeck.getName().equals(card.getName()))
                count++;
        }
        return count;
    }

    public int getNumberOfCardsInMainDeck() {
        return mainCards.size();
    }

    public int getNumberOfCardsInSideDeck() {
        return sideCards.size();
    }

    public Card getCardByNameInMainDeck(String name) {
        for (Card card : mainCards) {
            if (card.getName().equals(name)) return card;
        }

        return null;
    }

    public Card getCardByNameInSideDeck(String name) {
        for (Card card : sideCards) {
            if (card.getName().equals(name)) return card;
        }

        return null;
    }
}
