package roborally.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import roborally.objects.gameboard.Flag;
import roborally.objects.robot.Robot;
import roborally.ui.Layers;
import roborally.ui.gdx.events.Events;
import roborally.utilities.AssetManagerUtil;
import roborally.utilities.enums.Direction;
import roborally.utilities.enums.TileName;

import java.util.ArrayList;

public class Phase {
    private Layers layers;
    private Events events;
    private ArrayList<Robot> robots;

    public Phase(Layers layers, Events events, ArrayList<Robot> robots) {
        this.layers = layers;
        this.events = events;
        this.robots = robots;
    }

    public void moveAllConveyorBelts() {
        moveExpressConveyorBelts();
        moveNormalConveyorBelts();
    }

    public void runPhase(ArrayList<Flag> flags) {
        moveExpressConveyorBelts();
        moveAllConveyorBelts();
        moveCogs();
        registerFlagPositions(flags);
    }

    public void rotateConveyorBelts(ArrayList<Robot> rotateRobots) {
        TileName tileName;
        if (rotateRobots.isEmpty())
            return;
        for (Robot robot : rotateRobots) {
            if (layers.assertConveyorSlowNotNull(robot.getPosition()))
                tileName = layers.getConveyorSlowTileName(robot.getPosition());
            else if (layers.assertConveyorFastNotNull(robot.getPosition()))
                tileName = layers.getConveyorFastTileName(robot.getPosition());
            else
                return;
            if (tileName.toString().contains("COUNTER_CLOCKWISE"))
                robot.rotate(Direction.turnLeftFrom(robot.getLogic().getDirection()));
            else if (tileName.toString().contains("CLOCKWISE"))
                robot.rotate(Direction.turnRightFrom(robot.getLogic().getDirection()));
        }
    }

    public void moveNormalConveyorBelts() {
        ArrayList<Robot> rotateRobots = new ArrayList<>();
        for (Robot robot : robots) {
            GridPoint2 pos = robot.getPosition();
            if (layers.assertConveyorSlowNotNull(pos)) {
                TileName tileName = layers.getConveyorSlowTileName(pos);

                // Move in a special way so that no collision happens.
                if (tileName == TileName.CONVEYOR_RIGHT || tileName.toString().contains("TO_EAST") || tileName.toString().contains("JOIN_EAST"))
                    robot.moveRobot(new GridPoint2(1, 0));
                else if (tileName == TileName.CONVEYOR_NORTH || tileName.toString().contains("TO_NORTH") || tileName.toString().contains("JOIN_NORTH"))
                    robot.moveRobot(new GridPoint2(0, 1));
                else if (tileName == TileName.CONVEYOR_LEFT || tileName.toString().contains("TO_WEST") || tileName.toString().contains("JOIN_WEST"))
                    robot.moveRobot(new GridPoint2(-1, 0));
                else if (tileName == TileName.CONVEYOR_SOUTH || tileName.toString().contains("TO_SOUTH") || tileName.toString().contains("JOIN_SOUTH"))
                    robot.moveRobot(new GridPoint2(0, -1));
                rotateRobots.add(robot);
            }
        }
        rotateConveyorBelts(rotateRobots);
    }


    public void moveExpressConveyorBelts() {
        ArrayList<Robot> rotateRobots = new ArrayList<>();
        for (Robot robot : robots) {
            GridPoint2 pos = robot.getPosition();
            if (layers.assertConveyorFastNotNull(pos)) {
                TileName tileName = layers.getConveyorFastTileName(pos);

                // Move in a special way so that no collision happens.
                if (tileName == TileName.CONVEYOR_EXPRESS_EAST || tileName.toString().contains("TO_EAST") || tileName.toString().contains("JOIN_EAST"))
                    robot.moveRobot(new GridPoint2(1, 0));
                else if (tileName == TileName.CONVEYOR_EXPRESS_NORTH || tileName.toString().contains("TO_NORTH") || tileName.toString().contains("JOIN_NORTH"))
                    robot.moveRobot(new GridPoint2(0, 1));
                else if (tileName == TileName.CONVEYOR_EXPRESS_WEST || tileName.toString().contains("TO_WEST") || tileName.toString().contains("JOIN_WEST"))
                    robot.moveRobot(new GridPoint2(-1, 0));
                else if (tileName == TileName.CONVEYOR_EXPRESS_SOUTH || tileName.toString().contains("TO_SOUTH") || tileName.toString().contains("JOIN_SOUTH"))
                    robot.moveRobot(new GridPoint2(0, -1));
                rotateRobots.add(robot);
            }
        }
        rotateConveyorBelts(rotateRobots);
    }
    //endregion

    // Uncertain how to do this, maybe add the position of the gear into a list related to if its a rotate right
    // or rotate left gear?

    public void moveCogs() {
        for (Robot robot : robots) {
            GridPoint2 pos = robot.getPosition();
            if (layers.assertGearNotNull(pos)) {
                TileName tileName = layers.getGearTileName(pos);
                if (tileName == TileName.COG_CLOCKWISE)
                    robot.rotate(robot.rotate(Direction.turnLeftFrom(robot.getLogic().getDirection())));
                else if (tileName == TileName.COG_COUNTER_CLOCKWISE)
                    robot.rotate(robot.rotate(Direction.turnRightFrom(robot.getLogic().getDirection())));
            }
            robot.checkForLaser();
        }
    }


    public void fireLasers() {
        Sound sound = AssetManagerUtil.manager.get(AssetManagerUtil.SHOOT_LASER);
        sound.play((float) 0.08 * AssetManagerUtil.volume);
        for (Robot robot : robots) {
            robot.fireLaser();
            ArrayList<GridPoint2> coords = robot.getLaser().getCoords();
            if (!coords.isEmpty())
                events.createNewLaserEvent(robot.getPosition(), coords.get(coords.size() - 1));
        }
    }


    public void registerFlagPositions(ArrayList<Flag> flags) {
        System.out.println("\nChecking if any robots have currently arrived at their next flag position...");
        for (Flag flag : flags) {
            int flagX = flag.getPosition().x;
            int flagY = flag.getPosition().y;
            for (Robot robot : robots) {
                int robotX = robot.getPosition().x;
                int robotY = robot.getPosition().y;
                if (flagX == robotX && flagY == robotY) {
                    int nextFlag = robot.getNextFlag();
                    if (flag.getID() == nextFlag) {
                        robot.visitNextFlag();
                        robot.getLogic().setCheckPoint(new GridPoint2(flagX, flagY));
                        System.out.println("A flag has been visited");
                    }
                }
            }
        }
    }

    private void pushActivePushers() {

    }

    public void addPushers() {
        for (int i = 0; i < layers.getWidth(); i++) {
            for (int j = 0; j < layers.getWidth(); j++) {
            }
        }
    }
}
