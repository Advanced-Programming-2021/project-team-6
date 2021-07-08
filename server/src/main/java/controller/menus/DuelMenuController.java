package controller.menus;

import controller.AI;
import controller.Duel;
import controller.ErrorChecker;
import models.Database;
import models.Player;
import models.cards.Card;
import serverConection.GameInputs;
import serverConection.Output;
import serverConection.ServerController;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class DuelMenuController {

    public static HashMap<String, Duel> onlineDuels = new HashMap<>();
    private static int duelID = 1;

    private static DuelMenuController instance;
    public static boolean debugBool = false;

    private DuelMenuController() {
    }

    public static DuelMenuController getInstance() {
        if (instance == null)
            instance = new DuelMenuController();
        return instance;
    }

    public String startGame(String firstUsername, String secondUsername, String round, boolean isAI , String response)
            throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Player firstPlayer = Database.getInstance().getPlayerByUsername(firstUsername);
        Player secondPlayer = Database.getInstance().getPlayerByUsername(secondUsername);

        if (firstPlayer.getActiveDeck() == null)
            return "Error: " + firstUsername + " has no active deck";

        if (secondPlayer.getActiveDeck() == null)
            return "Error: " + secondUsername + " has no active deck";

        if (!ErrorChecker.isDeckAllowed(firstPlayer.getActiveDeck()))
            return "Error: " + firstUsername + "'s deck is invalid";


        if (!ErrorChecker.isDeckAllowed(secondPlayer.getActiveDeck()))
            return "Error: " + secondUsername + "'s deck is invalid";


        if (!round.equals("1") && !round.equals("3"))
            return "Error: " + "number of rounds is not supported";


        int numberOfRound = Integer.parseInt(round);
        int numberOfWinPlayer1 = 0, numberOfWinPlayer2 = 0;
        ServerController.sendMessageToSocket(secondPlayer.getToken(), response + duelID , false);
        new Duel(firstPlayer, secondPlayer, String.valueOf(duelID));

        duelID++;
        return (duelID - 1) + "";

//        for (int i = 1; i <= numberOfRound; i++) {
//            Output.getInstance().showMessage("game on");
//
//            if (isAI) runSinglePlayer(firstPlayer, secondPlayer);
//            if (!isAI) runMultiplePlayer(firstPlayer, secondPlayer);
//        }
//            Player winner = Duel.getCurrentDuel().getWinner();
//            if (winner == null)
//                return;
//            if (Duel.getCurrentDuel().getWinner().getUsername().equals(firstPlayer.getUsername()))
//                numberOfWinPlayer1++;
//            if (Duel.getCurrentDuel().getWinner().getUsername().equals(secondPlayer.getUsername()))
//                numberOfWinPlayer2++;
//
//            if ((numberOfWinPlayer1 == 2 && numberOfWinPlayer2 == 0) ||
//                    (numberOfWinPlayer1 == 0 && numberOfWinPlayer2 == 2)) break;
//
//            if (i != numberOfRound) changeDeck(firstPlayer, secondPlayer, isAI);


//        if (numberOfWinPlayer1 > numberOfWinPlayer2)
//            Output.getInstance().showMessage(firstUsername + "won the whole match with score: " +
//                    firstPlayer.getScore() + "-" + secondPlayer.getScore());
//        else
//            Output.getInstance().showMessage(secondUsername + "won the whole match with score: " +
//                    secondPlayer.getScore() + "-" + firstPlayer.getScore());

    }

    private void runMultiplePlayer(Player firstPlayer, Player secondPlayer)
            throws InvocationTargetException, CloneNotSupportedException, NoSuchMethodException, IllegalAccessException {
        Duel duel;


        return;

    }

//    private void runSinglePlayer(Player firstPlayer, Player secondPlayer)
//            throws InvocationTargetException, CloneNotSupportedException, NoSuchMethodException, IllegalAccessException {
//        Duel duel;
//        //GameInputs.getInstance().setOnlineDuel(duel = new Duel(firstPlayer, secondPlayer));
//        AI aiPlayer = AI.getInstance();
//        aiPlayer.setOnlineDuel(duel);
//        aiPlayer.setSinglePlayer(firstPlayer);
//        aiPlayer.setAiPlayer(secondPlayer);
//        while (!duel.isGameOver(false)) {
//            if (duel.getOnlinePlayer().getUsername().equals(firstPlayer.getUsername()))
//                GameInputs.getInstance().runGamePlay();
//            if (duel.getOnlinePlayer().getUsername().equals(secondPlayer.getUsername()))
//                aiPlayer.action();
//        }
//
//    }

    private void changeDeck(Player firstPlayer, Player secondPlayer, boolean isAI) {
        Duel.getCurrentDuel().setOnlinePlayer(firstPlayer);
        Output.getInstance().showMessage("Transferred cards between sideDeck and mainDeck for " + firstPlayer.getUsername() + ":");
        GameInputs.getInstance().runChangeHand();
        Output.getInstance().showMessage("end changing!");

        Duel.getCurrentDuel().setOnlinePlayer(secondPlayer);
        if (!isAI) {
            Output.getInstance().showMessage("change cards between sideDeck and mainDeck for " + secondPlayer.getUsername() + ":");
            GameInputs.getInstance().runChangeHand();
            Output.getInstance().showMessage("end changing!");
        }

    }

    public void showMainDeck(Player player) {
        for (Card card : player.getBoard().getDeckZoneMainCards())
            Output.getInstance().showMessage(card.toString());

    }

    public static Duel getDuelById(String id) {
        return onlineDuels.get(id);
    }

    public void showSideDeck(Player player) {
        for (Card card : player.getBoard().getDeckZoneSideCards())
            Output.getInstance().showMessage(card.toString());

    }

    public void swapCard(Player player, String cardName1, String cardName2) {
        Card card1 = player.getBoard().getDeckZone().getCardByNameInMainDeck(cardName1);
        Card card2 = player.getBoard().getDeckZone().getCardByNameInSideDeck(cardName2);

        if (card1 == null || card2 == null) {
            System.out.println("card with this name dose not exist!");
            return;
        }
        player.getBoard().getDeckZone().removeCard(card1, true);
        player.getBoard().getDeckZone().removeCard(card2, false);

        player.getBoard().getDeckZone().mainCards.add(card2);
        player.getBoard().getDeckZone().sideCards.add(card1);

        System.out.println("card Transferred successfully.");

    }

}
