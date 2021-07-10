package controller.menus;

import controller.ErrorChecker;
import models.Database;
import models.Player;
import models.cards.Card;
import serverConection.Output;

import java.util.Arrays;
import java.util.Comparator;

public class ShoppingMenuController {
    private ShoppingMenuController() {
    }

    private static ShoppingMenuController instance;

    public static ShoppingMenuController getInstance() {
        if (instance == null)
            instance = new ShoppingMenuController();
        return instance;
    }

    public String buyCard(String token, String cardName , boolean justWantToCheck) {
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null) return "Error";

        Card card = Database.getInstance().getCardByName(cardName);
        if (card == null) {
            return ("Error:there is no card with this name");
        }
        if (ErrorChecker.doseNotHaveEnoughMoney(player, card.getPrice())) {
            return ("Error:not enough money");
        }

        if (!justWantToCheck) {
            player.addCardToAllPlayerCard(card);
            player.setMoney(player.getMoney() - card.getPrice());
        }
        return ("Success:Card purchased");
    }

    public String showAllCard() {
        Card[] sortedCards = Database.allCards.toArray(new Card[0]);
        Arrays.sort(sortedCards, Comparator.comparing(Card::getName));
        StringBuilder cards = new StringBuilder();
        for (Card card : sortedCards) {
            cards.append(card.getName());
            cards.append(".jpg,");
        }
        return String.valueOf(cards);
    }

    public String showMoney(String token) {
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null) return "Error";
        return ("money:" + MainMenuController.getInstance().loggedInUsers.get(token).getMoney());
    }

    public String increaseMoney(String token, int amount) {
        Player player = Database.getInstance().getPlayerByToken(token);
        if (player == null) return "Error";
        player.setMoney(player.getMoney() + amount);
        return "Success";
    }


}
