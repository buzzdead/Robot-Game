package roborally.ui.gdx.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import roborally.game.objects.robot.Robot;
import roborally.utilities.AssetManagerUtil;
import roborally.utilities.enums.TileName;

import java.util.ArrayList;

public class LaserEvent {
    public static final float unitScale = 300 * 3f / 16f;
    public static final float yDiff = 675/825f;
    public static final float xDiff = 900f / 1150;
    private static float tileEdge = 10;
    private GridPoint2 laserPoint;
    private boolean laserEvent;
    private Image laserImage;
    private int factor;
    private int id;
    private boolean hitRobot;
    private Robot robot;

    public LaserEvent(int factor) {
        this.factor = factor;
    }

    public void laserImage(int id) {
        Image laserImage = new Image(AssetManagerUtil.getTileSets().getTile(id).getTextureRegion());
        laserImage.setSize(unitScale * xDiff, unitScale * yDiff);
        this.laserImage = laserImage;
    }

    public void laserEvent(GridPoint2 origin, GridPoint2 laserPoint) {
        this.laserPoint = laserPoint;
        if (laserPoint.y != origin.y) {
            this.id = TileName.LASER_VERTICAL.getTileID();
            this.laserImage(this.id);
            if (origin.y > laserPoint.y)
                this.factor = -this.factor;
        } else {
            this.id = TileName.LASER_HORIZONTAL.getTileID();
            this.laserImage(this.id);
            if (origin.x > laserPoint.x)
                this.factor = -this.factor;
        }
        System.out.println(origin);
        this.laserImage.setX(unitScale * (origin.x + 2.24f) * xDiff);
        if(this.id == TileName.LASER_VERTICAL.getTileID())
            this.laserImage.setX(unitScale * (origin.x + 2.24f) * xDiff);
        this.laserImage.setY(unitScale * (origin.y +1.3f) * yDiff);
        this.laserEvent = true;
    }

    /**
     * Draws an image through the batch from UI.
     *
     * @param batch  the SpriteBatch from UI.
     * @param robots The list of robots that's playing the game.
     */
    public void drawLaserEvent(SpriteBatch batch, ArrayList<Robot> robots) {
        float dt = (Gdx.graphics.getDeltaTime() * factor);
        if (this.id == TileName.LASER_HORIZONTAL.getTileID())
            drawLaserEventHorizontally(batch, robots, dt);
        else
            drawLaserEventVertically(batch, robots, dt);
    }

    public void drawLaserEventHorizontally(SpriteBatch batch, ArrayList<Robot> robots, float dt) {
        if (this.laserImage.getWidth() < tileEdge) {
            hitRobot(robots);
        }
        boolean negative = this.laserImage.getX() <= (this.laserPoint.x + 2.24) * unitScale * xDiff + tileEdge;
        boolean positive = this.laserImage.getX() >= (this.laserPoint.x + 2.24) * unitScale * xDiff - tileEdge;
        float oldWidth = this.laserImage.getWidth();
        float oldX = this.laserImage.getX();
        if (positive && factor > 0) {
            this.laserImage.setX(this.laserImage.getX() + dt / 3);
            this.laserImage.setWidth(oldWidth - (this.laserImage.getX() - oldX));
        }
        if (negative && factor < 0) {
            this.laserImage.setX(this.laserImage.getX() - dt / 3);
            this.laserImage.setWidth(oldWidth + (oldX - this.laserImage.getX()));
        } else if (!(positive && factor > 0))
            this.laserImage.setX(this.laserImage.getX() + dt);
        this.laserImage.draw(batch, 1);
    }

    public void drawLaserEventVertically(SpriteBatch batch, ArrayList<Robot> robots, float dt) {
        if (this.laserImage.getHeight() < tileEdge) {
            hitRobot(robots);
        }
        boolean negative = this.laserImage.getY() <= (this.laserPoint.y + 1.3f) * unitScale * yDiff + tileEdge;
        boolean positive = this.laserImage.getY() >= (this.laserPoint.y + 1.3f) * unitScale * yDiff - tileEdge;
        float oldHeight = this.laserImage.getHeight();
        float oldY = this.laserImage.getY();
        if (positive && factor > 0) {
            this.laserImage.setY(this.laserImage.getY() + dt / 3);
            this.laserImage.setHeight(oldHeight - (this.laserImage.getY() - oldY));
        }
        if (negative && factor < 0) {
            this.laserImage.setY(this.laserImage.getY() - dt / 3);
            this.laserImage.setHeight(oldHeight + (oldY - this.laserImage.getY()));
        } else if (!(positive && factor > 0))
            this.laserImage.setY(this.laserImage.getY() + dt);
        this.laserImage.draw(batch, 1);
    }

    // The robot that is hit takes damage, sound is played and event is stopped.
    private void hitRobot(ArrayList<Robot> robots) {

        for (Robot robot : robots)
            if (robot.getPosition().equals(laserPoint)) {
                hitRobot = true;
                this.robot = robot;
            }
        if (hitRobot) {
            this.robot.getLogic().takeDamage(1);
            Sound sound = AssetManagerUtil.manager.get(AssetManagerUtil.ROBOT_HIT);
            sound.play(0.035f * AssetManagerUtil.volume);
            hitRobot = false;
        }
        this.laserEvent = false;
    }

    public boolean hasLaserEvent() {
        return this.laserEvent;
    }

    public void setLaserSpeed(int factor) {
        this.factor = factor;
    }

    public Robot getRobot() {
        return this.robot;
    }
}
