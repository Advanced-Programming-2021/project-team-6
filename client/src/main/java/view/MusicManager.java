package view;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class MusicManager {

    public static int isMusicOn = 1;
    public static MediaPlayer musicBackground;
    public static MediaPlayer gameMusic;
    public static MediaPlayer mouseClick;
    public static MediaPlayer LPSound;
    public static MediaPlayer winSound;

    static {
   /*     URL url = MusicManager.class.getResource("/music/Seven Days Walking (Day 5) CD 1 TRACK 6 (320).mp3");
        musicBackground = new MediaPlayer(new Media(url.toString()));
        url = MusicManager.class.getResource("/music/The_Dark_Knight_Original_Motion_Picture_Soundtrack_CD_1_TRACK_8.mp3");
        gameMusic = new MediaPlayer(new Media(url.toString()));
        url = MusicManager.class.getResource("/music/Mouse-Click.mp3");
        mouseClick = new MediaPlayer(new Media(url.toString()));
        url = MusicManager.class.getResource("/music/mixkit-tropical-forest-bird-chirp-22.wav");
        LPSound = new MediaPlayer(new Media(url.toString()));
        url = MusicManager.class.getResource("/music/TB7L64W-winning.mp3");
        LPSound = new MediaPlayer(new Media(url.toString()));
*/
    }

    public static void playMusic(MediaPlayer music, boolean isCycle) {
   /*     if(isMusicOn == -1) return;
        music.stop();
        music.setAutoPlay(true);
        if (isCycle)
            music.setCycleCount(-1);
        music.play();*/
    }


}
