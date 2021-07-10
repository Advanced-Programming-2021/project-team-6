package controller.menus;

import controller.FileWorker;
import models.Database;
import models.Player;
import models.cards.Monster;
import models.cards.Spell;

import java.util.HashMap;

public class CreateCardMenuController {

    private static final HashMap<String, String> monsterActions = new HashMap<>();
    private static final HashMap<String, String> spellActions = new HashMap<>();

    static {
        monsterActions.put("Command Knight", "->summon-time:*?*collect<MZ>[-Any-];increase-attack-power{400};->flip-time:*?*collect<MZ>[-Any-];increase-attack-power{400};->death-time:**cancel{summon-time.flip-time};");
        monsterActions.put("Exploder Dragon", "->death-time:**kill-offender");
        monsterActions.put("Gate Guardian", "->flip-time:**collect<OMZ>[-Any-];select{1};kill;");
        monsterActions.put("Man-Eater Bug", "->flip-time:**collect<OMZ>[-Any-];select{1};kill;");
        monsterActions.put("Suijin", "->getting_raid:*?*collect<AT>[--];set-attack-power{0};consume-effect;->end-of-turn:**cancel;");
        monsterActions.put("Yomi Ship", "->death-time:**kill-offender");
    }

    static {
        spellActions.put("Black Pendant", "->activation-time:**collect<MZ.OMZ>[--];select{1};increase-attack-power{500};make-horcrux->death-time:**cancel{activation-time};");
        spellActions.put("Change Of Heart", "->activation-time:**collect<OMZ>[--];select{1};send-to{OMZ};");
        spellActions.put("Closed Forest", "->activation-time:**collect<MZ.OMZ>[--!MonsterType!=6&MonsterType!=13];increase-attack-power{200};increase-defence-power{200};->death-time:**cancel{activation-time}");
        spellActions.put("Dark Hole", "->activation-time:**collect<MZ.OMZ>[--];kill;die;");
        spellActions.put("Forest", "->activation-time:**collect<MZ.OMZ>[--!MonsterType!=6&MonsterType!=8&MonsterType!=13];increase-attack-power{200};increase-defence-power{200};->death-time:**cancel{activation-time}");
        spellActions.put("Harpie's Feather Duster", "->activation-time:**collect<OSZ>[--];send-to{OGY};die;");
        spellActions.put("Magnum Shield", "->activation-time:**collect<MZ.OMZ>[--!MonsterType!=2&MonsterType!=6];select{1};increase-attack-power{DEF};make-horcrux->death-time:**cancel{activation-time};");
        spellActions.put("Monster Reborn", "->activation-time:**collect<GY.OGY>[-Monster-];select{1};get-in{MZ};die;");
        spellActions.put("Mystical Space Typhoon", "->activation-time:**collect<OSZ>[--];select{1};send-to{OGY};die;");
        spellActions.put("Pot Of Greed", "->activation-time:**draw{2};die;");
        spellActions.put("Raigeki", "->activation-time:**collect<OMZ>[--];kill;die;");
        spellActions.put("Sword Of Dark Destruction", "->activation-time:**collect<MZ.OMZ>[--!MonsterType!=10&MonsterType!=1];select{1};increase-attack-power{400};increase-defence-power{-200};make-horcrux->death-time:**cancel{activation-time};");
        spellActions.put("Terraforming", "->activation-time:**collect<D>[-Spell-SpellProperty=4];select{1};send-to{H}");
        spellActions.put("Umiiruka", "->activation-time:**collect<MZ.OMZ>[--MonsterType=12];increase-attack-power{GYN×100};increase-defence-power{-400};->death-time:**cancel{activation-time}");
        spellActions.put("United We Stand", "->activation-time:**collect<MZ.OMZ>[--];select{1};collect<MZ>[--];increase-attack-power{collected×800};make-horcrux->death-time:**cancel{activation-time};");
        spellActions.put("Twin Twisters", "->activation-time:**collect<H>[--];select{1};send-to{GY};collect<OSZ>[--];select{2};send-to{OGY};die;");
        spellActions.put("Yami", "->activation-time:**collect<MZ.OMZ>[--!MonsterType!=10&MonsterType!=1];increase-attack-power{200};increase-defence-power{200};->activation-time:**collect<MZ.OMZ>[--MonsterType=14];increase-attack-power{-200};increase-defence-power{-200};->death-time:**cancel{activation-time}");
        spellActions.put("Call Of The Haunted", "->activation-time:**collect<GY>[-Monster-];select{1};send-to{MZ};make-horcrux;be-horcrux;");
    }

    public static String createCard(String name, String attackPower, String defencePower, String level,
                                           String description, String action, String token,String price, boolean isMonster) {
        Player player = MainMenuController.getInstance().loggedInUsers.get(token);
        if (player == null) return "";

        if (Database.getInstance().getCardByName(name) != null)
            return "Error: card with this name is already exist!";
        if (isMonster &&(Integer.parseInt(level) < 0 || Integer.parseInt(level) > 8))
            return "Error: level is invalid";

        String[] actionsName = action.split("/");

        if (isMonster) {
            String allActions = monsterActions.get(actionsName[0]);
            for (int i = 1; i < actionsName.length; i++)
                allActions += monsterActions.get(actionsName[i]).substring(13);
            System.out.println(allActions);
            Monster monster = new Monster(name, attackPower, defencePower, description, level, allActions);
            monster.setPrice(Integer.parseInt(price));
            Database.allCards.add(monster);
            Database.allMonsters.add(monster);
            FileWorker.getInstance().writeFileTo("./src/main/resources/Database/card-information/monsters/" + monster.getName() + ".json", monster);
        }else {
            String allActions = spellActions.get(actionsName[0]);
            for (int i = 1; i < actionsName.length; i++)
                allActions += spellActions.get(actionsName[i]).substring(18);
            Spell spell = new Spell(name, description, allActions);
            spell.setPrice(Integer.parseInt(price));
            Database.allCards.add(spell);
            Database.allSpells.add(spell);
            FileWorker.getInstance().writeFileTo("./src/main/resources/Database/card-information/spells/" + spell.getName() + ".json",spell);
        }
        player.setMoney(player.getMoney() - Integer.parseInt(price)/10);
        return "Success: card create successfully!";
    }


}
