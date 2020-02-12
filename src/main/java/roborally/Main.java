package roborally;

import roborally.ui.gameboard.UIGameBoard;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "RoboRally by CrawlingCrow";
        cfg.width = 600;
        cfg.height = 600;

        new LwjglApplication(new UIGameBoard(), cfg);
    }
}