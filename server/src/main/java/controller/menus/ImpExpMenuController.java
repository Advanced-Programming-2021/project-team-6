package controller.menus;

import controller.FileWorker;
import models.Database;
import models.Player;
import models.cards.Card;
import models.cards.Monster;
import models.cards.Spell;
import models.cards.Trap;
import serverConection.Output;

public class ImpExpMenuController {

    private final String importDataBase = "./src/main/resources/Database/ImpExp/";


    private ImpExpMenuController() {
    }

    private static ImpExpMenuController instance;

    public static ImpExpMenuController getInstance() {
        if (instance == null)
            instance = new ImpExpMenuController();
        return instance;
    }


    public String importFromFile(String cardName, String token) {
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null) return "Error";

        String fileAddress = importDataBase + cardName + ".json";

        try {
            Monster monsterCard = FileWorker.getInstance().readMonsterJSON(fileAddress);
            if (monsterCard.getTypeCard().equals("Monster")) {
                Database.allMonsters.add(monsterCard);
                Database.allCards.add(monsterCard);
                return "Success: card imported!";
            }
        } catch (NullPointerException e) {
        }

        try {
            Spell spellCard = FileWorker.getInstance().readSpellJSON(fileAddress);
            if (spellCard.getTypeCard().equals("Spell")) {
                Database.allSpells.add(spellCard);
                Database.allCards.add(spellCard);
                return "Success: card imported!";
            }
        } catch (NullPointerException e) {
        }

        try {
            Trap trapCard = FileWorker.getInstance().readTrapJSON(fileAddress);
            if (trapCard.getTypeCard().equals("Trap")) {
                Database.allTraps.add(trapCard);
                Database.allCards.add(trapCard);
                return "Success: card imported!";
            }
        } catch (NullPointerException e) {
        }

        return "Error: card with this name dose not exist";

    }

    public String exportToFile(String cardName, String token) {
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null) return "Error";

        String fileAddress = importDataBase + cardName + ".json";
        Card card = Database.getInstance().getCardByName(cardName);

        if (card == null)
            return "Error: card is not exist!";


        if (card.getTypeCard().equals("Monster")) {
            Monster monster = (Monster) card;
            FileWorker.getInstance().writeFileTo(fileAddress, monster);
        }
        if (card.getTypeCard().equals("Spell")) {
            Spell spell = (Spell) card;

            FileWorker.getInstance().writeFileTo(fileAddress, spell);
        }
        if (card.getTypeCard().equals("Trap")) {
            Trap trap = (Trap) card;

            FileWorker.getInstance().writeFileTo(fileAddress, trap);
        }

        return "Success: card exported!";

    }
}
