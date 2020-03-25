package roborally.objects.robot;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RobotLogicTest {
    private GridPoint2 initialStartPosition;
    private int posX;
    private int posY;
    private RobotLogic robotLogic;
    private String robotName;

    @Before
    public void setUp() {
        robotName = "TestRobot";
        robotLogic = new RobotLogic(robotName);
        // For setting direct x and y coordinates in RobotPresenter Position
        posX = 12;
        posY = 10;
        initialStartPosition = new GridPoint2(0,0);
    }

    @Test
    public void verifyThatRobotHasInitialPosition() {
        assertEquals(initialStartPosition, robotLogic.getPosition());
    }

    @Test
    public void verifyThatRobotCanSetAndGetPosition() {
        robotLogic.setPosition(new GridPoint2(posX, posY));
        assertEquals(new GridPoint2(posX, posY), robotLogic.getPosition());
    }

    @Test
    public void verifyThatRobotNameIsCorrect() {
        assertEquals(robotName, robotLogic.getName());
    }
}