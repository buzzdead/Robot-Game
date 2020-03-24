package roborally.ui.listeners;

import com.badlogic.gdx.math.GridPoint2;
import roborally.game.objects.robot.Robot;
import roborally.ui.Layers;
import roborally.utilities.AssetManagerUtil;

public class CollisionListener {
    private Layers layers;
    private WallListener wallListener;

    public CollisionListener(Layers layers) {
        this.layers = layers;
        this.wallListener = new WallListener(this.layers);
    }

    /**
     * Called from {@link #checkIfBlocked} if there is multiple robots in a line in the direction the robot is moving.
     * Iterates through all the robots to see if the foremost robot in the direction is blocked, moves robots
     * accordingly.
     *
     * @return True if there is a wall blocking the path.
     */
    public boolean robotNextToRobot(GridPoint2 pos, GridPoint2 move) {
        int width = layers.getWidth();
        int height = layers.getHeight();
        GridPoint2 nextPos = pos.cpy().add(move);
        boolean recursiveRobot = false;
        if (wallListener.checkForWall(pos, move))
            return true;
        if (nextPos.x >= 0 && nextPos.x < width && nextPos.y >= 0 && nextPos.y < height) {
            if (wallListener.checkForWall(pos, move))
                return true;
            if (layers.assertRobotNotNull(nextPos))
                recursiveRobot = robotNextToRobot(nextPos, move);
            for (Robot robot : AssetManagerUtil.getRobots())
                if (robot.getPosition().x == pos.x && robot.getPosition().y == pos.y && !recursiveRobot) {
                    System.out.println("\nPushing robot...");
                    robot.moveRobot(move);
                    robot.checkForLaser();
                    System.out.println("Pushing robot complete");
                }
        }
        // RobotPresenter "deletion"
        else
            for (Robot robot : AssetManagerUtil.getRobots())
                if (robot.getPosition().equals(pos)) {
                    layers.setRobotCell(pos, null);
                    robot.setPosition(new GridPoint2(-1, -1));
                    robot.clearRegister();
                }
        return recursiveRobot;
    }

    /**
     * Called from {@link #checkIfBlocked} if the robot collides with another robot.
     * Moves the robots accordingly.
     */
    public void findCollidingRobot(GridPoint2 pos, GridPoint2 move) {
        int width = layers.getRobots().getWidth();
        int height = layers.getRobots().getHeight();
        GridPoint2 nextPos = pos.cpy().add(move);
        for (Robot robot : AssetManagerUtil.getRobots()) {
            if (robot != null) {
                GridPoint2 bumpedPos = robot.getPosition();
                if (bumpedPos.equals(pos) && (nextPos.x >= width || nextPos.x < 0 || nextPos.y >= height || nextPos.y < 0)) {
                    // RobotPresenter "deletion".
                    robot.setPosition(new GridPoint2(-1, -1));
                    robot.clearRegister();
                    layers.setRobotCell(pos, null);
                } else if (bumpedPos.equals(pos)) {
                    System.out.println("\nPushing... ");
                    robot.moveRobot(move);
                    robot.checkForLaser();
                    System.out.println("Pushing complete... ");
                    if (layers.assertFlagNotNull(nextPos))  //Checks if the robot got bumped into a flag.
                        robot.setWinTexture();
                    else if (layers.assertHoleNotNull(nextPos)) //Checks if the robot got bumped into a hole.
                        robot.setLostTexture();
                }
            }
        }
    }

    /**
     * Checks if the robot is blocked by another robot, true if that robot is blocked by a wall. If not,
     * inspects further with {@link #robotNextToRobot} and collides with {@link #findCollidingRobot}
     *
     * @return True if the robot or any of the robots in front of it is found in {@link #robotNextToRobot} is blocked.
     */
    public boolean checkIfBlocked(GridPoint2 pos, GridPoint2 move) {
        if (wallListener.checkForWall(pos, move))
            return true;
        GridPoint2 newPos = pos.cpy().add(move);
        // There is no RobotPresenter on the next position.
        if (!layers.assertRobotNotNull(newPos))
            return false;
        else {
            if (wallListener.checkForWall(newPos, move))
                return true;
            if (layers.assertRobotNotNull(newPos) && robotNextToRobot(newPos, move))
                return true;
        }
        findCollidingRobot(newPos, move);
        return false;
    }
}
