package roborally.game.objects.robot;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import roborally.game.objects.cards.IProgramCards;
import roborally.game.objects.laser.Laser;
import roborally.game.objects.laser.LaserRegister;
import roborally.ui.Layers;
import roborally.ui.listeners.Listener;
import roborally.ui.robot.IRobotView;
import roborally.ui.robot.RobotView;
import roborally.utilities.AssetManagerUtil;
import roborally.utilities.SettingsUtil;
import roborally.utilities.enums.Direction;

public class Robot implements Programmable {
    private IRobotView robotView;
    private RobotLogic robotLogic;
    private boolean[] visitedFlags;
    private Laser laser;
    private Layers layers;
    private Listener listener;
    private LaserRegister laserRegister;

    // Constructor for testing the robot model.
    public Robot(RobotLogic robotLogic) {
        this.robotLogic = robotLogic;
    }

    public Robot(int x, int y, int robotID, LaserRegister laserRegister) {
        this.visitedFlags = new boolean[3];
        RobotLogic robotModel = new RobotLogic(AssetManagerUtil.getRobotName());
        IRobotView robotView = new RobotView(x, y);
        this.robotLogic = robotModel;
        this.robotView = robotView;
        setPosition(new GridPoint2(x, y));
        this.setTextureRegion(robotID);
        this.robotLogic.setCheckPoint(x, y);
        this.layers = new Layers();
        laser = new Laser(0, layers);
        this.listener = new Listener(layers);
        listener.listenLaser(x, y, getName(), laserRegister);
        this.laserRegister = laserRegister;
    }

    @Override
    public String getName() {
        return this.robotLogic.getName();
    }

    //region Position
    @Override
    public GridPoint2 getPosition() {
        return this.robotLogic.getPosition();
    }

    @Override
    public void setPosition(GridPoint2 newPosition) {
        this.robotLogic.setPosition(newPosition);
    }
    //endregion

    //region Movement
    @Override
    public int[] move(int steps) {
        int x = getPosition().x;
        int y = getPosition().y;
        int[] moveValues = robotLogic.move(steps);
        for (int i = 0; i < Math.abs(steps); i++)
            moveRobot(moveValues[0], moveValues[1]);
        Sound sound;
        if (getPosition().dst(x, y) == 1) {
            sound = AssetManagerUtil.manager.get(AssetManagerUtil.STEP1);
            sound.play(0.25f * AssetManagerUtil.volume);
        } else if (getPosition().dst(x, y) == 2) {
            sound = AssetManagerUtil.manager.get(AssetManagerUtil.STEP2);
            sound.play(0.25f * AssetManagerUtil.volume);
        } else if (getPosition().dst(x, y) == 3) {
            sound = AssetManagerUtil.manager.get(AssetManagerUtil.STEP3);
            sound.play(0.25f * AssetManagerUtil.volume);
        }
        if (listener.listenLaser(getPosition().x, getPosition().y, getName(), laserRegister))
            robotLogic.takeDamage(1);
        return this.robotLogic.move(steps);
    }

    @Override
    public Direction rotate(Direction direction) {
        this.robotLogic.rotate(direction);
        this.robotView.setDirection(getPosition(), direction);
        return direction;
    }

    public void moveRobot(int dx, int dy) {
        GridPoint2 pos = this.getPosition();
        GridPoint2 newPos = new GridPoint2(pos.x + dx, pos.y + dy);
        System.out.println("Old position: " + pos);
        // Checks for robots in its path before moving.
        if (!listener.listenCollision(pos.x, pos.y, dx, dy)) {
            if (this.robotView.moveRobot(pos.x, pos.y, dx, dy)) {
                this.setPosition(newPos);
                System.out.println("New position: " + newPos);
                if (layers.assertHoleNotNull(newPos.x, newPos.y)) {
                    takeDamage(10);
                    this.setLostTexture();
                }
                this.robotView.setDirection(getPosition(), robotLogic.getDirection());
            }
        } else
            System.out.println("New position: " + pos);
    }
    //endregion

    public void takeDamage(int dmg) {
        if(getLogic().takeDamage(dmg))
            backToCheckPoint();
    }

    public void backToCheckPoint() {
        robotView.goToCheckPoint(this.getPosition(), robotLogic.getCheckPoint());
        this.robotLogic.backToCheckPoint();
    }

    public RobotLogic getLogic() {
        return this.robotLogic.getLogic();
    }


    public void fireLaser() {
        laser.fireLaser(getPosition(), this.robotLogic.getDirectionID());
    }

    public Laser getLaser() {
        return this.laser;
    }

    //region Texture
    public void setTextureRegion(int i) {
        this.robotView.setTextureRegion(i);
    }

    public TextureRegion[][] getTexture() {
        return this.robotView.getTextureRegion();
    }

    public void setWinTexture() {
        this.robotView.setWinTexture(getPosition());
    }

    public void setLostTexture() {
        this.robotView.setLostTexture(getPosition());

    }

    public void deleteRobot() {
        this.layers.setRobotCell(getPosition().x, getPosition().y, null);
        this.setPosition(SettingsUtil.GRAVEYARD);
        clearRegister();
    }
    //endregion

    public void checkForLaser() {
        if (listener.listenLaser(getPosition().x, getPosition().y, getName(), laserRegister))
            robotLogic.takeDamage(1);
    }

    public void playNextCard() {
        IProgramCards.Card card = getLogic().getNextCard();
        if (card == null)
            return;
        if (card.getCardType() == IProgramCards.CardTypes.MOVE_1)
            move(1);
        else if (card.getCardType() == IProgramCards.CardTypes.MOVE_2)
            move(2);
        else if (card.getCardType() == IProgramCards.CardTypes.MOVE_3)
            move(3);
        else if (card.getCardType() == IProgramCards.CardTypes.ROTATE_LEFT)
            rotate(Direction.turnLeftFrom(robotLogic.getDirection()));
        else if (card.getCardType() == IProgramCards.CardTypes.ROTATE_RIGHT)
            rotate(Direction.turnRightFrom(robotLogic.getDirection()));
        else if (card.getCardType() == IProgramCards.CardTypes.U_TURN) {
            rotate(Direction.turnLeftFrom(robotLogic.getDirection()));
            rotate(Direction.turnLeftFrom(robotLogic.getDirection()));
        } else if (card.getCardType() == IProgramCards.CardTypes.BACKUP)
            move(-1);
    }

    // TODO: Refactor to RobotModel, make it correlate with GameBoard(GameModel).

    public boolean hasVisitedAllFlags() {
        boolean visitedAll = true;
        for (boolean visitedFlag : visitedFlags) {
            visitedAll = visitedAll && visitedFlag;
        }
        return visitedAll;
    }

    // TODO: Refactor to RobotModel.

    public int getNextFlag() {
        for (int i = 0; i < visitedFlags.length; i++) {
            if (!visitedFlags[i]) {
                return i + 1;
            }
        }
        return -1;
    }

    // TODO: Refactor to RobotModel

    public void visitNextFlag() {
        this.setWinTexture();
        this.robotView.setDirection(getPosition(), robotLogic.getDirection());
        System.out.println("updated flag visited");
        int nextFlag = getNextFlag();
        visitedFlags[nextFlag - 1] = true;
        if (nextFlag == visitedFlags.length)
            System.out.println("Congratulations you have collected all the flags, press 'W' to win the game.");
        else
            System.out.println("Next flag to visit: " + (nextFlag + 1));
    }

    public void clearRegister() {
        laserRegister.updateLaser(getName(), getPosition());
    }
}
