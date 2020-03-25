package roborally.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import roborally.objects.gameboard.Flag;
import roborally.objects.gameboard.GameBoard;
import roborally.objects.laser.LaserRegister;
import roborally.objects.robot.Robot;
import roborally.ui.Layers;
import roborally.ui.gdx.events.Events;
import roborally.utilities.AssetManagerUtil;

import java.util.ArrayList;

public class Game {
    private GameBoard gameBoard;
    private Layers layers;
    private ArrayList<Robot> robots;
    private ArrayList<Flag> flags;
    private LaserRegister laserRegister;
    private int currentRobotID;
    private Events events;
    private GameOptions gameOptions;
    private Phase phase;
    private Round round;

    public Game(Events events) {
        currentRobotID = 0;
        this.events = events;
        this.gameOptions = new GameOptions();
    }

    public Game(boolean runAIGame) {
        assert (runAIGame);
        gameBoard = new GameBoard();
        layers = gameBoard.getLayers();
    }


    public void startUp() {
        gameBoard = new GameBoard();
        layers = gameBoard.getLayers();
        flags = gameBoard.findAllFlags();
        this.laserRegister = new LaserRegister(layers);
        this.robots = gameOptions.makeRobots(layers, laserRegister);
        this.phase = new Phase(layers, events, robots);
    }


    public void funMode() {
        gameBoard = new GameBoard();
        layers = gameBoard.getLayers();
        flags = gameBoard.findAllFlags();
        this.laserRegister = new LaserRegister(layers);
        robots = gameOptions.funMode(layers, flags, laserRegister);
        this.events.setGameSpeed("fastest");
        round.setFun();
    }


    public void checkForDestroyedRobots() {
        for (Robot robot : robots) {
            if (("Destroyed").equals(robot.getLogic().getStatus())) {
                removeFromUI(robot, true);
            }
        }
    }

    private void removeFromUI(Robot robot, boolean fade) {
        events.fadeRobot(robot.getPosition(), robot.getTexture(), robot.getCurrentTexture());
        robot.deleteRobot();
        this.events.setFadeRobot(fade);
    }


    public Layers getLayers() {
        return this.layers;
    }

    //region Robots

    public Robot getFirstRobot() {

        if (this.currentRobotID == robots.size()) {
            this.currentRobotID = 0;
        }
        checkForDestroyedRobots();
        return robots.get(0);
    }


    public ArrayList<Robot> getRobots() {
        return this.robots;
    }

    public void setRobots(ArrayList<Robot> newRobots) {
        this.robots = newRobots;
    }

    public void restartGame() {
        System.out.println("Restarting game...");
        for (Robot robot : robots) {
            removeFromUI(robot, false);
        }
        setRobots(gameOptions.makeRobots(layers, laserRegister));
    }

    public GameOptions getGameOptions() {
        return this.gameOptions;
    }


    public void fireLaser() {
        Sound sound = AssetManagerUtil.manager.get(AssetManagerUtil.SHOOT_LASER);
        sound.play((float) 0.08 * AssetManagerUtil.volume);
        robots.get(0).fireLaser();
        ArrayList<GridPoint2> coords = robots.get(0).getLaser().getCoords();
        if (!coords.isEmpty())
            events.createNewLaserEvent(robots.get(0).getPosition(), coords.get(coords.size() - 1));
    }

    public void newRound() {
        this.round = new Round(robots, events, this, flags, layers);
    }

    public Round getRound() {
        return this.round;
    }

    public void testEndPhase() {
        phase.runPhase(flags);
    }

    public void shuffleTheRobotsCards(int[] order) {
        robots.get(0).getLogic().arrangeCards(order);
    }

    public void endGame() {
        for (Robot robot : robots) {
            layers.setRobotCell(robot.getPosition(), null);
            removeFromUI(robot, true);
        }
        robots.clear();
    }


    public void exitGame() {
        Gdx.app.exit();
    }
}
