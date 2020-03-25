package roborally.objects.robot;

import com.badlogic.gdx.math.GridPoint2;
import roborally.utilities.enums.Direction;


public interface Programmable {

    /**
     * @return Name of programmable
     */
    String getName();

    /**
     * @return Position of programmable
     */
    GridPoint2 getPosition();

    /**
     * Sets the position of programmable
     *
     * @param newPosition New position
     */
    void setPosition(GridPoint2 newPosition);

    /**
     * Moves the programmable
     *
     * @param steps Number of steps to move
     * @return The RobotLogic move according to steps
     */
    GridPoint2 move(int steps);

    /**
     * Rotates the programmable in the new direction
     *
     * @return
     */
    Direction rotate(Direction direction);

    /**
     *
     */
    void backToCheckPoint();

    /**
     *
     * @return
     */
    RobotLogic getLogic();
}
