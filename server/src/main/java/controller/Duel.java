package controller;

import controller.menus.DuelMenuController;
import controller.menus.MainMenuController;
import models.Board;
import models.Database;
import models.Deck;
import models.Player;
import models.cards.*;
import serverConection.ConsoleBasedMenus;
import serverConection.GameInputs;
import serverConection.Output;
import serverConection.ServerController;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;


public class Duel {
    private static Duel currentDuel = null;
    private static boolean isPhaseSkipped = false, isAttackNegated = false, isActivationNegated = false, isSummonNegated = false;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private Player onlinePlayer;
    private Player offlinePlayer;
    private int turn = 1;
    private Phases phase = Phases.DRAW;
    private Player winner;
    private Monster attackingMonster, targetMonster;
    private boolean isFirstTurn = true;
    private String ID;
    public static String randomTurn;

    public Duel(Player firstPlayer, Player secondPlayer, String ID) throws CloneNotSupportedException {
        firstPlayer.setHealth(8000);
        secondPlayer.setHealth(8000);
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.onlinePlayer = firstPlayer;
        this.offlinePlayer = secondPlayer;
        this.ID = ID;
        DuelMenuController.onlineDuels.put(ID, this);
        firstPlayer.setHealth(8000);
        secondPlayer.setHealth(8000);
        this.firstPlayer.setBoard(new Board(firstPlayer, secondPlayer, ID));
        this.secondPlayer.setBoard(new Board(secondPlayer, firstPlayer, ID));
        ActionJsonParser.getInstance().setDuel(this);
        currentDuel = this;
    }

    public static Duel getCurrentDuel() {
        return currentDuel;
    }

    public static void setPhaseSkip(boolean shouldBeSkipped) {
        isPhaseSkipped = shouldBeSkipped;
    }

    public Player getWinner() {
        return winner;
    }

    public Monster getAttackingMonster() {
        return attackingMonster;
    }

    public Monster getTargetMonster() {
        return targetMonster;
    }

    public Player getOfflinePlayer() {
        return offlinePlayer;
    }

    public Player getOnlinePlayer() {
        return onlinePlayer;
    }

    public void setOnlinePlayer(Player onlinePlayer) {
        this.onlinePlayer = onlinePlayer;
    }

    public Player getOpponent(Player player) {
        return (player == onlinePlayer) ? offlinePlayer : onlinePlayer;
    }

    public Phases getPhase() {
        return phase;
    }

    public void setPhase(Phases phase) {
        this.phase = phase;
    }

    public String changePhase(String token) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Player player = Database.getInstance().getPlayerByToken(token);
        if (!player.getUsername().equals(onlinePlayer.getUsername())) return "Error: not your turn";
        String duelID = String.valueOf(player.getDuelID());
        showBoard();
        if (phase.equals(Phases.DRAW)) {
            phase = Phases.STANDBY;
            if (isPhaseSkipped) {
                isPhaseSkipped = false;
                return changePhase(token);
            }
            actionInStandbyPhase();
            ServerController.sendMessageToSocket(player.getBoard().getOpponent().getToken(),
                    "change phase: " + phase, false);
            return "change phase: " + phase;
        }
        if (phase.equals(Phases.STANDBY)) {
            phase = Phases.MAIN1;
            if (isPhaseSkipped) {
                isPhaseSkipped = false;
                return changePhase(token);
            }
            actionsInMainPhase();
            ServerController.sendMessageToSocket(player.getBoard().getOpponent().getToken(),
                    "change phase: " + phase, false);
            return "change phase: " + phase;
        }
        if (phase.equals(Phases.MAIN1)) {
            phase = Phases.BATTLE;
            if (isPhaseSkipped) {
                isPhaseSkipped = false;
                return changePhase(token);
            }
            actionsInBattlePhase();
            ServerController.sendMessageToSocket(player.getBoard().getOpponent().getToken(),
                    "change phase: " + phase, false);
            return "change phase: " + phase;
        }
        if (phase.equals(Phases.BATTLE)) {
            phase = Phases.MAIN2;
            if (isPhaseSkipped) {
                changePhase(token);
                isPhaseSkipped = false;
                return "skipped Main";
            }
            actionsInMainPhase();
            ServerController.sendMessageToSocket(player.getBoard().getOpponent().getToken(),
                    "change phase: " + phase, false);
            return "change phase: " + phase;
        }
        if (phase.equals(Phases.MAIN2)) {
            phase = Phases.END;
            EventHandler.triggerEndPhase(onlinePlayer);
            if (isPhaseSkipped) {
                changePhase(token);
                isPhaseSkipped = false;
                return "skipped End";
            }
            actionsInEndPhase();
            ServerController.sendMessageToSocket(player.getBoard().getOpponent().getToken(),
                    "change phase: " + phase, false);
            return "change phase: " + phase;
        }
        if (phase.equals(Phases.END)) {
            phase = Phases.DRAW;
            changeTurn();
            EventHandler.triggerDrawPhase(onlinePlayer);
            if (isPhaseSkipped) {
                changePhase(token);
                isPhaseSkipped = false;
                return "skipped Draw";
            }
            actionsInDrawPhase(duelID);
            ServerController.sendMessageToSocket(player.getBoard().getOpponent().getToken(),
                    "change phase: " + phase, false);
            return "change phase: " + phase;
        }
        return "";
    }

    public void actionsInDrawPhase(String duelID) {
        Output.getInstance().showMessage("its " + onlinePlayer.getNickname() + "'s turn");
        onlinePlayer.getBoard().drawCard(1, duelID);
    }

    public void actionInStandbyPhase() {
    }

    public void actionsInMainPhase() {
        onlinePlayer.getBoard().setSelectedCard(null);
        onlinePlayer.getBoard().setChangePositionInTurn(false);
        onlinePlayer.getBoard().setSummonedOrSetCardInTurn(false);

    }

    public void actionsInBattlePhase() {
        onlinePlayer.getBoard().setSelectedCard(null);
        for (Card card : onlinePlayer.getBoard().getMonsterZoneCards()) {
            if (card != null)
                ((Monster) card).setHaveBeenAttackedWithMonsterInTurn(false);
        }
    }

    public void actionsInEndPhase() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        doEndOfTurnActions();
        //setNumberOfCardInHand();
    }

    private void doEndOfTurnActions() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        for (Card card : onlinePlayer.getBoard().getMonsterZoneCards()) {
            Monster monster = (Monster) card;
            if (monster == null)
                continue;
            monster.endOfTurn();
        }
    }

    private void setNumberOfCardInHand() {
        while (onlinePlayer.getBoard().getHandZoneCards().size() > 6) {
            int address;
            try {
                address = Integer.parseInt(GameInputs.getInstance().getAddressForDeleteCard());
                onlinePlayer.getBoard().getHand().moveCardToForGame(onlinePlayer.getBoard().getGraveyardZone(),
                        onlinePlayer.getBoard().getHandZoneCards().get(address), true, true);
            } catch (Exception e) {
                Output.getInstance().showMessage("Please Input A Valid Number");
            }

        }
    }

    public boolean isGameOver(boolean isDebugMode) {
        if (isDebugMode)
            return true;
        if (chooseWinner(onlinePlayer, offlinePlayer)) return true;
        if (chooseWinner(offlinePlayer, onlinePlayer)) return true;
        return false;
    }

    private boolean chooseWinner(Player onlinePlayer, Player offlinePlayer) {
        if (onlinePlayer.getHealth() <= 0 || onlinePlayer.getBoard().getDeckZone().getMainCards().size() == 0) {
            setPrize(offlinePlayer, onlinePlayer);
            Output.getInstance().showMessage(offlinePlayer.getUsername() + " won the game and the score is: " +
                    offlinePlayer.getScore() + "-" + onlinePlayer.getScore());
            winner = offlinePlayer;
            return true;
        }
        return false;
    }

    private void setPrize(Player winner, Player loser) {
        winner.setScore(winner.getScore() + 1000);
        winner.setMoney(winner.getMoney() + winner.getHealth() + 1000);
        loser.setMoney(loser.getMoney() + 100);
    }

    public void changeTurn() {
        turn *= -1;
        if (turn == 1) {
            this.onlinePlayer = firstPlayer;
            this.offlinePlayer = secondPlayer;
        } else {
            this.onlinePlayer = secondPlayer;
            this.offlinePlayer = firstPlayer;
        }
        isFirstTurn = false;
    }


    public void changeDeckBasedOnClientCard(Card clientCard, Card card, String destination) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        changeDeck(clientCard, card, destination);
    }

    private void changeDeck(Card clientCard, Card card, String destination) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Deck cardDeck = card.getCurrentDeck();
        Player player = clientCard.getCurrentDeck().getOwner();
        Board board = cardDeck.getOwner().getBoard();
        switch (destination) {
            case "MZ":
                if (ErrorChecker.isMonsterCardZoneFull(player.getBoard().getMonsterZoneCards()))
                    return;
                break;
            case "OMZ":
                if (ErrorChecker.isMonsterCardZoneFull(getOpponent(player).getBoard().getMonsterZoneCards()))
                    return;
                break;
            case "SZ":
                if (!ErrorChecker.isSpellZoneFree(player.getBoard()))
                    return;
                break;
            case "OSZ":
                if (!ErrorChecker.isSpellZoneFree(getOpponent(player).getBoard()))
                    return;
                break;
        }
        switch (cardDeck.getName()) {
            case "MZ":
                board.removeFromMonsterZone(card);
                break;
            case "SZ":
                board.removeFromSpellZone(card);
                break;
            case "GY":
                board.removeFromGraveyard(card);
                break;
            case "FZ":
                board.removeFromFieldZone(card);
                break;
            case "HZ":
            case "H":
                board.removeFromHand(card);
                break;
            case "D":
                board.removeFromDeck(card);
                break;
        }

        switch (destination) {
            case "MZ":
                player.getBoard().putCardInMonsterZone(card);
                break;
            case "OMZ":
                getOpponent(player).getBoard().putCardInMonsterZone(card);
                break;
            case "SZ":
                player.getBoard().putCardInSpellZone(card);
                break;
            case "OSZ":
                getOpponent(player).getBoard().putCardInSpellZone(card);
                break;
            case "H":
                player.getBoard().putInHand(card);
                break;
            case "GY":
                if (card instanceof Spell)
                    ((Spell) card).die();
                if (card instanceof Monster)
                    ((Monster) card).die();
                player.getBoard().putInGraveyard(card);
                break;
            case "OGY":
                if (card instanceof Spell)
                    ((Spell) card).die();
                if (card instanceof Monster)
                    ((Monster) card).die();
                getOpponent(player).getBoard().putInGraveyard(card);
                break;
            case "F":
                player.getBoard().putCardInFieldZone(card);
                break;
        }
    }

    public void changeDeck(Card card, String destination) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        changeDeck(card, card, destination);
    }

    public String summon(String cardAddress, String token) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null)
            return "Error";

        if (!player.getUsername().equals(onlinePlayer.getUsername())) return "Error: not your turn";

        Card selectedCard = player.getBoard().getHandZoneCards().get(Integer.parseInt(cardAddress));

        ArrayList<Card> monsterZone = player.getBoard().getMonsterZoneCards();
        EventHandler.triggerOpponentMonsterSummon(selectedCard);
        EventHandler.triggerMonsterSummon(selectedCard);
        if (isSummonNegated) {
            isSummonNegated = false;
            return "Error: summon is negated";

        }
        if (!ErrorChecker.isCardInPlayerHand(selectedCard, player) ||
                !ErrorChecker.isMonsterCard(selectedCard))
            return "Error: you can't summon this card";

        if (!ErrorChecker.isMainPhase(phase))
            return "Error: this is not main phase";
        if (ErrorChecker.isMonsterCardZoneFull(monsterZone))
            return "Error: monster zone is full";
        if (player.getBoard().isSummonedOrSetCardInTurn())
            return "Error: you already summoned/set on this turn";
        if (selectedCard.getType() == CardType.ritual)
            return "Error: you can't normal summon a ritual monster";


//        if (((Monster) selectedCard).getLEVEL() == 5 || ((Monster) selectedCard).getLEVEL() == 6) {
//            if (!ErrorChecker.isThereOneMonsterForTribute(monsterZone))
//                return "Error: you can't summon this card";
//            String addressString = GameInputs.getInstance().getAddressForTribute();
//            if (addressString.equals("")) return;
//            int address = setCardAddressInMyBoard(Integer.parseInt(addressString));
//            if (!ErrorChecker.isThereCardInAddress(monsterZone, address)) return;
//            player.getBoard().removeFromMonsterZone(monsterZone.get(address));
//            player.getBoard().removeFromHand(selectedCard);
//        }

//        if (((Monster) selectedCard).getLEVEL() == 7 || ((Monster) selectedCard).getLEVEL() == 8) {
//            if (!ErrorChecker.isThereTwoMonsterForTribute(player.getBoard().getMonsterZoneCards()))
//                return "Error: you can't summon this card";
//            String addressString1 = GameInputs.getInstance().getAddressForTribute();
//            if (addressString1.equals("surrender")) return;
//            String addressString2 = GameInputs.getInstance().getAddressForTribute();
//            if (addressString2.equals("surrender")) return;
//            int address1 = setCardAddressInMyBoard(Integer.parseInt(addressString1));
//            int address2 = setCardAddressInMyBoard(Integer.parseInt(addressString2));
//            if (!ErrorChecker.isThereCardInAddress(player.getBoard().getMonsterZoneCards(), address1)) return;
//            if (!ErrorChecker.isThereCardInAddress(player.getBoard().getMonsterZoneCards(), address2)) return;
//            player.getBoard().removeFromMonsterZone(monsterZone.get(address1));
//            player.getBoard().removeFromMonsterZone(monsterZone.get(address2));
//        }

        selectedCard.setCardPlacement(CardPlacement.faceUp);
        ((Monster) selectedCard).setMonsterMode(MonsterMode.attack);
        int place = player.getBoard().putCardInMonsterZone(selectedCard);
        ((Monster) selectedCard).summon();
        player.getBoard().setSummonedOrSetCardInTurn(true);
        player.getBoard().removeFromHand(selectedCard);
        player.getBoard().setSelectedCard(null);

        char prefix = Board.getPrefix(selectedCard);
        ServerController.sendMessageToSocket(player.getBoard().getOpponent().getToken(), "monster summon " + prefix + ":" + selectedCard.getName() + " in " + place, false);
        return "Success: " + prefix + ":" + selectedCard.getName() + " summon monster zone in " + place;

    }

    public String setMonster(String cardAddress, String token) {
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null) return "Error";
        if (!player.getUsername().equals(onlinePlayer.getUsername())) return "Error: not your turn";

        Card selectedCard = player.getBoard().getHandZoneCards().get(Integer.parseInt(cardAddress));

        if (!(selectedCard instanceof Monster))
            return "Error: you can't set this card in this zone";
        ArrayList<Card> monsterZone = player.getBoard().getMonsterZoneCards();

        if (!ErrorChecker.isMainPhase(phase))
            return "Error: you can't set card in this phase";
        if (ErrorChecker.isMonsterCardZoneFull(monsterZone))
            return "Error: monster zone is full";
        if (player.getBoard().isSummonedOrSetCardInTurn())
            return "Error: you already summoned/set on this turn";

        selectedCard.setCardPlacement(CardPlacement.faceDown);
        ((Monster) selectedCard).setMonsterMode(MonsterMode.defence);
        int place = player.getBoard().putCardInMonsterZone(selectedCard);
        player.getBoard().setSummonedOrSetCardInTurn(true);
        player.getBoard().removeFromHand(selectedCard);
        char prefix = Board.getPrefix(selectedCard);
        ServerController.sendMessageToSocket(player.getBoard().getOpponent().getToken(), "monster set " + prefix + ":" + selectedCard.getName() + " in " + place, false);
        return "Success: " + prefix + ":" + selectedCard.getName() + " set in monster zone" + place;
    }

    public void activateSpellCard() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (ErrorChecker.isAbleToBeActive(selectedCard, phase, onlinePlayer.getBoard())) {
            if (selectedCard instanceof Spell) {
                EventHandler.triggerSpellActivation(selectedCard);
                EventHandler.triggerOpponentSpellActivation(selectedCard);
                EventHandler.triggerSpellTrapActivation(selectedCard);
                EventHandler.triggerOpponentSpellTrapActivation(selectedCard);
            }
            if (selectedCard instanceof Trap) {
                EventHandler.triggerTrapActivation(selectedCard);
                EventHandler.triggerOpponentTrapActivation(selectedCard);
                EventHandler.triggerSpellTrapActivation(selectedCard);
                EventHandler.triggerOpponentSpellTrapActivation(selectedCard);
            }
            if (!isActivationNegated) {
                if (selectedCard.getCardPlacement() == null) {
                    int index = onlinePlayer.getBoard().getFirstFreeSpellZoneIndex();
                    onlinePlayer.getBoard().removeFromHand(selectedCard);
                    onlinePlayer.getBoard().getSpellZone().mainCards.set(index, selectedCard);
                }

                if (selectedCard instanceof Spell)
                    ((Spell) selectedCard).activate();
                if (selectedCard instanceof Trap)
                    ((Trap) selectedCard).activate();
                Output.getInstance().showMessage(selectedCard.getTypeCard() + " activated");
            } else {
                Output.getInstance().showMessage(selectedCard.getName() + " activation negated");
            }

            selectedCard.setCardPlacement(CardPlacement.faceUp);
            onlinePlayer.getBoard().setSelectedCard(null);
        }
    }

    public String setSpellAndTrap(String cardAddress, String token) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null) return "Error";

        if (!player.getUsername().equals(onlinePlayer.getUsername())) return "Error: not your turn";

        Card selectedCard = player.getBoard().getHandZoneCards().get(Integer.parseInt(cardAddress));

        if (!(selectedCard instanceof Spell || selectedCard instanceof Trap))
            return "Error: you can't set this card in this zone";

        if (!ErrorChecker.isMainPhase(phase)) return "Error: you can't set card in this phase";

        if (player.getBoard().isSpellZoneFull())
            return "Error: spell card zone is full";

        if (((Spell) selectedCard).getProperty().equals(SpellProperty.field))
            return setFieldCard(player, selectedCard);

        selectedCard.setCardPlacement(CardPlacement.faceDown);
        int place = player.getBoard().putCardInSpellZone(selectedCard);
        player.getBoard().removeFromHand(selectedCard);
        player.getBoard().setSelectedCard(null);

        ServerController.sendMessageToSocket(player.getBoard().getOpponent().getToken(), "spell set " + selectedCard.getName() + " in " + place, false);
        return "Success: " + selectedCard.getName() + " set in spell zone" + place;
    }

    private String setFieldCard(Player onlinePlayer, Card selectedCard) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if (onlinePlayer.getBoard().getFieldZone() != null) {
            ((Spell) onlinePlayer.getBoard().getFieldZone()).die();
            onlinePlayer.getBoard().putInGraveyard(onlinePlayer.getBoard().getFieldZone());
        }
        onlinePlayer.getBoard().putCardInFieldZone(selectedCard);
        onlinePlayer.getBoard().removeFromHand(selectedCard);
        onlinePlayer.getBoard().setSelectedCard(null);
        return "Success";
    }

    public void activationOfSpellInOpponentTurn() throws
            InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        turn *= -1;
        if (turn == 1)
            Output.getInstance().showMessage("now it will be " + firstPlayer.getUsername() + "'s turn");
        else
            Output.getInstance().showMessage("now it will be " + secondPlayer.getUsername() + "'s turn");
        Output.getInstance().showMessage(onlinePlayer.getBoard().toString(onlinePlayer));
        Output.getInstance().showMessage("do you want to activate your trap and spell?");
        if (!GameInputs.getInstance().yesOrNoQuestion()) {
            turn *= -1;
            if (turn == 1)
                Output.getInstance().showMessage("now it will be " + firstPlayer.getUsername() + "'s turn");
            else
                Output.getInstance().showMessage("now it will be " + secondPlayer.getUsername() + "'s turn");
        } else {
            Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
            if (ErrorChecker.isAbleToBeActive(selectedCard, phase, onlinePlayer.getBoard())) {
                activateSpellCard();
                Output.getInstance().showMessage("spell/trap activated");
            } else {
                Output.getInstance().showMessage("it's not your turn to play this kind of moves");
            }
        }
    }

    public String setPosition(String mode, String token, String address) {

        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null) return "Error";

        if (!player.getUsername().equals(onlinePlayer.getUsername())) return "Error: not your turn";

        Card selectedCard = player.getBoard().getHandZoneCards().get(Integer.parseInt(address));

        MonsterMode newMonsterMode = null;
        if (mode.equals("attack")) newMonsterMode = MonsterMode.attack;
        if (mode.equals("defence")) newMonsterMode = MonsterMode.defence;

        if (!player.getBoard().isInMonsterZone(selectedCard))
            return "Error: you can't set this card";


        if (!ErrorChecker.isMainPhase(phase))
            return "Error: you can't set card in this phase";
        //i will edit it
        if (!ErrorChecker.isNewMonsterMode(selectedCard, newMonsterMode)) return "Error";

        if (player.getBoard().isChangePositionInTurn())
            return "Error: you already changed this card position in this turn";

        ((Monster) selectedCard).setMonsterMode(newMonsterMode);
        player.getBoard().setChangePositionInTurn(true);
        player.getBoard().setSelectedCard(null);
        return "Success";
    }

    public void flipSummon() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (!onlinePlayer.getBoard().isInMonsterZone(selectedCard)) {
            Output.getInstance().showMessage("you can't summon this card");
            return;
        }
        if (!ErrorChecker.isMainPhase(phase)) return;
        if (((Monster) selectedCard).getMonsterMode().equals(MonsterMode.attack) ||
                selectedCard.getCardPlacement().equals(CardPlacement.faceUp)) {
            Output.getInstance().showMessage("you can't flip summon this card");
            return;
        }
        ((Monster) selectedCard).setMonsterMode(MonsterMode.attack);
        ((Monster) selectedCard).flip();
        onlinePlayer.getBoard().setSelectedCard(null);
        Output.getInstance().showMessage("flip Summoned successfully");
    }

    public void negateAttack() {
        isAttackNegated = true;
    }

    public void negateActivation() {
        isActivationNegated = true;
    }

    public void ritualSummon() {
        Output.getInstance().showMessage("Select a Ritual Monster");
        Card monster = onlinePlayer.getBoard().getSelectedCard();
        if (!ErrorChecker.isCardInPlayerHand(monster, onlinePlayer) ||
                !ErrorChecker.isMonsterCard(monster)) {
            Output.getInstance().showMessage("you can't summon" + monster.getName() + "card");
            return;
        }
        Output.getInstance().showMessage("Select your tributes");
        Deck selectedCards = Deck.getSelectionDeckFrom(onlinePlayer.getBoard().getMonsterZone(), -1);
        ArrayList<Card> monsterZone = onlinePlayer.getBoard().getMonsterZoneCards();
        if (!(onlinePlayer.getBoard().isThereRitualInHand() && onlinePlayer.getBoard().isCardsLevelsEnoughForTribute())) {
            Output.getInstance().showMessage("there is no way you could ritual summon a monster");
        }
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (!ErrorChecker.isMainPhase(phase)) return;
        if (ErrorChecker.isMonsterCardZoneFull(monsterZone)) return;
        if (onlinePlayer.getBoard().isSummonedOrSetCardInTurn()) {
            Output.getInstance().showMessage("you already summoned/set on this turn");
            return;
        }
        Output.getInstance().showMessage("select position of your ritual monster : attack or defence");
        String position = ConsoleBasedMenus.scanner.nextLine();
        //setPosition(position);
        onlinePlayer.getBoard().putCardInMonsterZone(monster);
        for (Card monster1 : selectedCards.mainCards) {
            onlinePlayer.getBoard().getMonsterZone().moveCardToForGame(onlinePlayer.getBoard().getGraveyardZone(), monster1, true, true);
        }
        Card spell = onlinePlayer.getBoard().getSelectedCard();
        while (!(spell instanceof Spell) || !(spell.getType() == CardType.ritual)) {
            Output.getInstance().showMessage("this is not ritual select another one");
            spell = onlinePlayer.getBoard().getSelectedCard();
        }
        onlinePlayer.getBoard().getSpellZone().moveCardToForGame(onlinePlayer.getBoard().getGraveyardZone(), spell, true, true);
        onlinePlayer.getBoard().setSelectedCard(null);
        Output.getInstance().showMessage("Ritual Summoned successfully");
    }

    public String attack(String myCardAddress, String addressForAttack, String token) throws
            InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        onlinePlayer = player;
        offlinePlayer = player.getBoard().getOpponent();

        if (player == null)
            return "Error";

        if (!player.getUsername().equals(onlinePlayer.getUsername())) return "Error: not your turn";
        if (isFirstTurn)
            return "Error: this is first turn and you can't attack the opponent!";

        Card selectedCard = player.getBoard().getHandZoneCards().get(Integer.parseInt(myCardAddress));

        if (!onlinePlayer.getBoard().isInMonsterZone(selectedCard))
            return "Error: you can't attack with this card";
        if (((Monster) selectedCard).getMonsterMode().equals(MonsterMode.defence))
            return "Error: This model is a defense card";
        if (!ErrorChecker.isBattlePhase(phase))
            return "Error: this is not battle phase";
        if (((Monster) selectedCard).isHaveBeenAttackedWithMonsterInTurn())
            return "Error: this card already attacked";

        attackingMonster = (Monster) selectedCard;

        ((Monster) selectedCard).setHaveBeenAttackedWithMonsterInTurn(true);
        return runAttack(Integer.parseInt(addressForAttack), (Monster) selectedCard,token);


    }


    public String runAttack(int address, Monster selectedCard, String token) throws
            InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        EventHandler.triggerMonsterAttack(selectedCard);
        EventHandler.triggerOpponentMonsterAttack(selectedCard);
        if (!isAttackNegated) {
            Player offlinePlayer = MainMenuController.getInstance().loggedInUsers.get(token);
            targetMonster = (Monster) offlinePlayer.getBoard().getMonsterZoneCards().get(address);
            MonsterMode monsterMode = targetMonster.getMonsterMode();
            CardPlacement monsterPlacement = targetMonster.getCardPlacement();
            if (!targetMonster.isAttackAble())
                return "you can't attack " + targetMonster.getName() + " because of its effect";

            targetMonster.getRaid();
            if (monsterPlacement.equals(CardPlacement.faceUp) && monsterMode.equals(MonsterMode.attack))
                return monsterAttackToAttack(targetMonster, selectedCard);

            else if (monsterPlacement.equals(CardPlacement.faceUp) && monsterMode.equals(MonsterMode.defence))
                return monsterAttackToDefenseFaceUp(targetMonster, selectedCard);

            else if (monsterPlacement.equals(CardPlacement.faceDown) && monsterMode.equals(MonsterMode.defence))
                return monsterAttackToDefenseFaceDown(targetMonster, selectedCard);
            isAttackNegated = false;
        } else
            return selectedCard.getName() + " attack negated";

        return "";
    }

    private String monsterAttackToAttack(Monster targetMonster, Monster selectedCard) throws
            InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (selectedCard.getTotalAttackPower() > targetMonster.getTotalAttackPower()) {
            int damage = selectedCard.getTotalAttackPower() - targetMonster.getTotalAttackPower();
            offlinePlayer.setHealth(offlinePlayer.getHealth() - damage);
            kill(offlinePlayer, targetMonster);
            return "your opponent's monster is destroyed and your opponent receives " +
                    damage + " battle damage";
        }
        if (selectedCard.getTotalAttackPower() == targetMonster.getTotalAttackPower()) {
            kill(offlinePlayer, targetMonster);
            kill(onlinePlayer, selectedCard);
            targetMonster.die();
            attackingMonster.die();
            return "both you and your opponent monster cards are destroyed and no one receives damage";
        }
        if (selectedCard.getTotalAttackPower() < targetMonster.getTotalAttackPower()) {
            kill(onlinePlayer, selectedCard);
            int damage = targetMonster.getTotalAttackPower() - selectedCard.getTotalAttackPower();
            onlinePlayer.setHealth(offlinePlayer.getHealth() - damage);
            attackingMonster.die();
            return "your monster card is destroyed and you received " + damage + " battle damage";
        }
        return "";
    }

    private String monsterAttackToDefenseFaceUp(Monster targetMonster, Monster selectedCard) {
        targetMonster.setCardPlacement(CardPlacement.faceUp);
        if (selectedCard.getTotalAttackPower() > targetMonster.getTotalDefencePower()) {
            offlinePlayer.getBoard().putInGraveyard(targetMonster);
            offlinePlayer.getBoard().removeFromMonsterZone(targetMonster);
            return "the defense position monster is destroyed";
        }
        if (selectedCard.getTotalAttackPower() == targetMonster.getTotalDefencePower())
            return "no card destroyed";

        if (selectedCard.getTotalAttackPower() < targetMonster.getTotalDefencePower()) {
            int damage = targetMonster.getTotalDefencePower() - selectedCard.getTotalAttackPower();
            onlinePlayer.setHealth(offlinePlayer.getHealth() - damage);
            return "no card is destroyed and you received " + damage + " battle damage";
        }

        return "";
    }

    public void kill(Player player, Monster monster) throws
            InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        player.getBoard().putInGraveyard(monster);
        player.getBoard().removeFromMonsterZone(monster);
        monster.die();
    }

    public void kill(Monster monster) throws
            InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Player player = monster.getCurrentDeck().getOwner();
        player.getBoard().putInGraveyard(monster);
        player.getBoard().removeFromMonsterZone(monster);
        monster.die();
    }

    private String monsterAttackToDefenseFaceDown(Monster targetMonster, Monster selectedCard) {
        targetMonster.setCardPlacement(CardPlacement.faceUp);
        if (selectedCard.getAttackPower() > targetMonster.getDefencePower()) {
            offlinePlayer.getBoard().putInGraveyard(targetMonster);
            offlinePlayer.getBoard().removeFromMonsterZone(targetMonster);
            return "opponent's monster card was " + targetMonster.getName() +
                    "and the defense position monster is destroyed";
        }
        if (selectedCard.getAttackPower() == targetMonster.getDefencePower())
            return "opponent's monster card was " + targetMonster.getName() +
                    "and no card destroyed";

        if (selectedCard.getAttackPower() < targetMonster.getDefencePower()) {
            int damage = targetMonster.getDefencePower() - selectedCard.getAttackPower();
            onlinePlayer.setHealth(offlinePlayer.getHealth() - damage);
            return "opponent's monster card was " + targetMonster.getName() +
                    "and no card is destroyed and you received " + damage + " battle damage";
        }
        return "";
    }

    public String attackDirect(String myCardAddress,String token) {
        onlinePlayer = MainMenuController.getInstance().loggedInUsers.get(token);
        offlinePlayer = onlinePlayer.getBoard().getOpponent();

        if (onlinePlayer == null)
            return "Error";

        if (!onlinePlayer.getUsername().equals(onlinePlayer.getUsername())) return "Error: not your turn";
        if (isFirstTurn)
            return "Error: this is first turn and you can't attack the opponent!";

        Card selectedCard = onlinePlayer.getBoard().getHandZoneCards().get(Integer.parseInt(myCardAddress));

        if (isFirstTurn)
            return "this is first turn and you can't attack the opponent directly!";

        if (!onlinePlayer.getBoard().isInMonsterZone(selectedCard))
            return "Error: you can't attack with this card";
        if (((Monster) selectedCard).getMonsterMode().equals(MonsterMode.defence))
            return "Error: This model is a defense card";
        if (!ErrorChecker.isBattlePhase(phase))
            return "Error: this is not battle phase";
        if (!ErrorChecker.isMonsterZoneEmpty(offlinePlayer.getBoard().getMonsterZoneCards()))
            return "you can't attack the opponent directly";


        if (((Monster) selectedCard).isHaveBeenAttackedWithMonsterInTurn())
            return "this card already attacked";

        offlinePlayer.setHealth(offlinePlayer.getHealth() - ((Monster) selectedCard).getAttackPower());
        ((Monster) selectedCard).setHaveBeenAttackedWithMonsterInTurn(true);
        return "you opponent receives " + ((Monster) selectedCard).getAttackPower() + " battle damage";
    }

    public void showGraveyard() {
        ArrayList<Card> graveyard = onlinePlayer.getBoard().getGraveyardZone().mainCards;
        StringBuilder show = new StringBuilder();
        if (graveyard.size() != 0) {
            for (Card card : graveyard) {
                show.append(card.getName()).append(":").append(card.getDescription()).append("\n");
            }
            Output.getInstance().showMessage(show.toString());
        } else Output.getInstance().showMessage("graveyard empty");
        while (true) {
            if (GameInputs.getInstance().backQ()) {
                Duel.getCurrentDuel().showBoard();
                return;
            }
        }
    }

    public void showCard() {
        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        boolean status = false;
        for (Card card : offlinePlayer.getAllPlayerCard().getMainCards()) {
            if (Objects.equals(card, selectedCard)) {
                status = true;
                break;
            }
        }
        if (ErrorChecker.isCardVisible(selectedCard, status)) {
            Output.getInstance().showMessage(selectedCard.toString());
        }
    }


    private int setCardAddressInMyBoard(int address) {
        if (address == 5) return 0;
        if (address == 3) return 1;
        if (address == 1) return 2;
        if (address == 2) return 3;
        if (address == 4) return 4;
        return -1;
    }

    private int setCardAddressInOpponentBoard(int address) {
        if (address == 4) return 0;
        if (address == 2) return 1;
        if (address == 1) return 2;
        if (address == 3) return 3;
        if (address == 5) return 4;
        return -1;
    }

    public String increaseLP(String myToken) {
        Player onlinePlayer = MainMenuController.getInstance().loggedInUsers.get(myToken);
        if (onlinePlayer == null) return "Error";

        onlinePlayer.setHealth(onlinePlayer.getHealth() + 1);
        ServerController.sendMessageToSocket(onlinePlayer.getBoard().getOpponent().getToken(), "increase LP -o", false);
        System.out.println(1);
        return String.valueOf(onlinePlayer.getHealth());
    }

    public String cheatForWinGame(String token, boolean isSubmission) {
        Player player = Database.getInstance().getPlayerByToken(token);
        Player opponent = player.getBoard().getOpponent();
        DuelMenuController.onlineDuels.remove(player.getDuelID());
        if (!isSubmission) {
            setPrize(player, opponent);

            ServerController.sendMessageToSocket(onlinePlayer.getBoard().getOpponent().getToken(), "winner is " + player.getUsername()
                    + "\n loser is " + opponent.getUsername(), false);
            return "winner is " + player.getUsername();
        } else {
            setPrize(opponent, player);

            ServerController.sendMessageToSocket(onlinePlayer.getBoard().getOpponent().getToken(), "winner is " + opponent.getUsername()
                    + "\n loser is " + player.getUsername(), false);
            return "winner is " + opponent.getUsername();
        }

    }

    public void showBoard() {
        Output.getInstance().showMessage(offlinePlayer.getBoard().toString(onlinePlayer) + "\n--------------------------\n" +
                onlinePlayer.getBoard().toString(onlinePlayer));
    }

    public void showTurn() {
        Output.getInstance().showMessage(onlinePlayer.getNickname());
    }

    public void negateSummon() {
        isSummonNegated = true;
    }

    public String randomTurn() {
        int face = (int) (Math.random() * 2);
        if (face == 0) {
            return "head";
        }
        return "tail";
    }

    public String setSecondPlayerTurn() {
        onlinePlayer = secondPlayer;
        offlinePlayer = firstPlayer;
        return "Success";
    }

    public static void flip() {
        int face = (int) (Math.random() * 2);
        if (face == 0)
            randomTurn = "head";

        randomTurn = "tail";

    }

}
