package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import view.MusicManager;
import view.Prompt;
import view.PromptType;
import view.menus.Coin;
import view.menus.Game;
import view.menus.MainMenuView;
import view.menus.WelcomeMenuView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ServerMessageHandler {

    public static boolean isFirstDraw = true;

    public static String getServerMessage(String message) throws IOException, InterruptedException {
        System.out.println("A Message Came From Central Server : " + message);
        String[] params = message.split(" ");
        if (message.startsWith("GameOn")) {
            return MainMenuView.startNewGame(params[1], params[3], params[2], params[4], params[5], params[6], Integer.parseInt(params[7]), true);
        } else if (message.startsWith("draw-p")) {
            int howManyCards = Integer.parseInt(params[1]);
            for (int i = 0; i < howManyCards; i++) {
                String[] cardNames = message.substring(9).split(",");
                Game.drawCardForPlayer(cardNames[i], ((isFirstDraw) ? 20 : 0) + i * 0.7);
            }
        } else if (message.startsWith("draw-o")) {
            int howManyCards = Integer.parseInt(params[1]);
            for (int i = 0; i < howManyCards; i++)
                Game.drawCardForOpponent(((isFirstDraw) ? 12 : 0) + i * 0.7);
        } else if (message.startsWith("increase LP")) {
            Game.cheatLPOpponent();
        } else if (message.startsWith("spell set")) {
            //graphics for set spell
        }else if (message.startsWith("change phase")){
            Game.changePhaseGraphically(message.split(": ")[1].split(" ")[0]);

        } else if (message.startsWith("winner is")) {
            Prompt.showMessage(message, PromptType.Message);
            MusicManager.playMusic(MusicManager.winSound, false);
            TimeUnit.SECONDS.sleep(3);
            Parent root = FXMLLoader.load(ServerMessageHandler.class.getResource("/fxml/MainMenu.fxml"));
            Scene scene = new Scene(root);
            scene.setCursor(new ImageCursor(new Image(ServerMessageHandler.class.getResource("/image/mouse.jpg").toString())));
            WelcomeMenuView.mainStage.setScene(scene);
        }
        else if(message.startsWith("monster set")) {
            params = message.split(" ");
            Game.setMonsterForOpponent(params[2].split(":")[1] , params[2].split(":")[0] );
        }
        else if(message.startsWith("monster summon")) {
            params = message.split(" ");
            Game.summonForOpponent(params[2].split(":")[1] , params[2].split(":")[0] );
        }
        return "";
    }
}

