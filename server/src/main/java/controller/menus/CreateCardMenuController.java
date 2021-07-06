package controller.menus;

import controller.FileWorker;
import models.Database;
import models.Player;
import models.cards.Monster;

public class CreateCardMenuController {

    private CreateCardMenuController() {
    }

    private static CreateCardMenuController instance;

    public static CreateCardMenuController getInstance() {
        if (instance == null)
            instance = new CreateCardMenuController();
        return instance;
    }

    public String createCard(String name, String attackPower, String defencePower, String level,
                             String description, String token) {

        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null) return "";

        if (Database.getInstance().getCardByName(name) != null)
            return "Error: card with this name is already exist!";
        if (Integer.parseInt(level) < 0 || Integer.parseInt(level) > 8)
            return "Error: level is invalid";
        Monster monster = new Monster(name, attackPower, defencePower, description, level);
        Database.allCards.add(monster);
        Database.allMonsters.add(monster);
        FileWorker.getInstance().writeFileTo("./src/main/resources/Database/card-information/monsters/" + monster.getName() + ".json", monster);
        return "Success: card create successfully!";
    }
}
