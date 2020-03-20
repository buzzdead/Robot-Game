package roborally.game;

import roborally.game.objects.gameboard.IFlag;
import roborally.game.objects.laser.LaserRegister;
import roborally.game.objects.robot.Robot;
import roborally.ui.ILayers;
import roborally.utilities.AssetManagerUtil;

import java.util.ArrayList;

public class GameOptions {
    private boolean menu;

    public GameOptions() {
        this.menu = false;
    }

    public void enterMenu() {
        this.menu = !this.menu;
    }

    public void enterMenu(boolean value) {
        this.menu = value;
    }

    public boolean getMenu() {
        return this.menu;
    }

    public ArrayList<Robot> funMode(ILayers layers, ArrayList<IFlag> flags, LaserRegister laserRegister) {
        ArrayList<Robot> robots = new ArrayList<>();
        for (int x = 0; x < layers.getWidth(); x++) {
            for (int y = 0; y < layers.getHeight(); y++) {
                robots.add(new Robot(x, y, y % 8, laserRegister));
            }
        }
        AssetManagerUtil.setRobots(robots);
        return robots;
    }

    public ArrayList<Robot> makeRobots(ILayers layers, LaserRegister laserRegister) {
        ArrayList<Robot> robots = new ArrayList<>();
        int cell = 0;
        for (int i = 0; i < layers.getWidth(); i++) {
            for (int j = 0; j < layers.getHeight(); j++) {
                if (layers.assertStartPosNotNull(i, j)) {
                    robots.add(new Robot(i, j, cell % 8, laserRegister));
                    cell++;
                }
            }
        }
        AssetManagerUtil.setRobots(robots);
        return robots;
    }

}
