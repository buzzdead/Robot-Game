package roborally.game.objects.robot;

import com.badlogic.gdx.math.GridPoint2;

public class RobotModel implements IRobotModel {
    private String name;
    private GridPoint2 robotPosition;
    private GridPoint2 checkPoint;
    private int health = 10;
    private int reboots = 3;

    public RobotModel(String name) {
        this.name = name;
        this.robotPosition = new GridPoint2();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public GridPoint2 getPosition() {
        return this.robotPosition;
    }

    @Override
    public void setPosition(GridPoint2 pos) {
        this.robotPosition = pos;
    }

    @Override
    public GridPoint2 getCheckPoint() {
        return this.checkPoint;
    }

    @Override
    public int getHealth() { return health; }

    @Override
    public int getReboots() { return reboots; }

    @Override
    public void setCheckPoint(int x, int y) {
        this.checkPoint = new GridPoint2(x, y);
    }
}