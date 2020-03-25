package roborally.ui.gdx.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import roborally.game.Game;
import roborally.objects.robot.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Events {
    public static final float unitScale = 300 * 3f / 16f;
    public static final float yDiff = 675 / 825f;
    public static final float xDiff = 900f / 1150;
    private boolean waitEvent;
    private int pauseCount;
    private float dt;
    private boolean robotFadeOrder;
    private ArrayList<Alpha> fadeableRobots;
    private int fadeCounter;
    private ArrayList<LaserEvent> laserEvents;
    private int registerPhase;
    private double gameSpeed;
    private int factor;

    public Events() {
        this.waitEvent = false;
        this.dt = 0f;
        this.pauseCount = 1;
        this.robotFadeOrder = false;
        this.fadeableRobots = new ArrayList<>();
        this.fadeCounter = 0;
        this.laserEvents = new ArrayList<>();
        registerPhase = 1;
        this.gameSpeed = 0.2;
        this.setLaserSpeed("normal");

    }

    public void setGameSpeed(String gameSpeed) {
        if ("fastest".equals(gameSpeed))
            this.gameSpeed = 0.01;
        else if ("fast".equals(gameSpeed))
            this.gameSpeed = 0.2;
        else if ("normal".equals(gameSpeed))
            this.gameSpeed = 0.5;
    }

    public void setLaserSpeed(String laserSpeed) {
        if ("slow".equals(laserSpeed))
            this.factor = 400;
        else if ("normal".equals(laserSpeed))
            this.factor = 800;
        else if ("fast".equals(laserSpeed))
            this.factor = 1200;
    }

    /**
     * Adds the time lapsed between frames to the variable, plays the next card when it reaches the gameSpeed.
     *
     * @param dt   Time lapsed between frames.
     * @param game The game.
     */
    public void waitMoveEvent(float dt, Game game) {
        this.dt += dt;
        if (this.dt >= gameSpeed) {
            game.getRound().playNextCard();
            this.dt = 0;
            this.pauseCount++;
        }
        if (pauseCount / registerPhase == game.getRobots().size()) {
            game.getRound().fireLasers();
            registerPhase++;
        }
        if (pauseCount == 5 * game.getRobots().size()) {
            this.dt = 0f;
            this.pauseCount = 1;
            this.registerPhase = 1;
            setPauseEvent(false);
        }
    }

    public boolean hasWaitEvent() {
        return waitEvent;
    }

    // Lets the UI know wether or not there are robots ready to move.
    public void setPauseEvent(boolean value) {
        this.waitEvent = value;
    }

    /**
     * Replaces the robots texture with an image and fades it.
     *
     * @param pos     The robots position.
     * @param texture The robots texture.
     */
    public void fadeRobot(GridPoint2 pos, TextureRegion[][] texture, GridPoint2 currentTexture) {
        Image image = new Image(texture[currentTexture.x][currentTexture.y]);
        image.setX((pos.x + 2.24f) * unitScale * xDiff);
        image.setY((pos.y + 1.3f) * unitScale * yDiff);
        image.setSize(unitScale * xDiff, unitScale * yDiff);
        this.fadeableRobots.add(new Alpha(1f, image));
    }

    // Returns true if there are robots to be faded.
    public boolean getFadeRobot() {
        return this.robotFadeOrder;
    }

    public void setFadeRobot(boolean value) {
        this.robotFadeOrder = value;
    }

    // Fades the robots.
    public void fadeRobots(SpriteBatch batch) {

        for (Alpha alpha : this.fadeableRobots) {
            alpha.image.draw(batch, alpha.update(Gdx.graphics.getDeltaTime()));
            if (alpha.dt <= 0)
                fadeCounter++;
        }

        if (fadeCounter == this.fadeableRobots.size()) {
            this.fadeableRobots.clear();
            setFadeRobot(false);
        }
        fadeCounter = 0;
    }


    // Returns true if there are lasers on the screen.
    public boolean hasLaserEvent() {
        return !this.laserEvents.isEmpty();
    }

    // Removes lasers that have served their purpose. Returns and sets the list excluding these lasers.
    public ArrayList<LaserEvent> getLaserEvents() {
        List<LaserEvent> temp = this.laserEvents.stream()
                .filter(LaserEvent::hasLaserEvent)
                .collect(Collectors.toList());
        this.laserEvents.removeAll(temp);
        for (LaserEvent laserEvent : this.laserEvents) {
            if (laserEvent.getRobot() != null) {
                if (("Destroyed".equals(laserEvent.getRobot().getLogic().getStatus()))) {
                    GridPoint2 pos = laserEvent.getRobot().getPosition();
                    fadeRobot(pos, laserEvent.getRobot().getTexture(), laserEvent.getRobot().getCurrentTexture());
                    laserEvent.getRobot().deleteRobot();
                    setFadeRobot(true);
                } else {
                    laserEvent.getRobot().setLostTexture();
                }
            }
        }
        this.laserEvents = (ArrayList<LaserEvent>) temp;
        return this.laserEvents;
    }

    /**
     * Creates a new Laser event for the laser that has been fired.
     *
     * @param origin The position the laser was fired from.
     * @param pos    The endpoint of the laser.
     */
    public void createNewLaserEvent(GridPoint2 origin, GridPoint2 pos) {
        this.laserEvents.add(new LaserEvent(factor));
        this.laserEvents.get(this.laserEvents.size() - 1).laserEvent(origin, pos);
    }

    public void drawRobots(ArrayList<Robot> robots, SpriteBatch batch) {
        for (Robot robot : robots) {
            GridPoint2 currentTexture = robot.getCurrentTexture();
            GridPoint2 pos = robot.getPosition();
            Image image = new Image(robot.getTexture()[currentTexture.x][currentTexture.y]);
            image.setX((pos.x + 2.24f) * unitScale * xDiff);
            image.setY((pos.y + 1.3f) * unitScale * yDiff);
            image.setSize(unitScale * xDiff, unitScale * yDiff);
            image.draw(batch, 1);
        }
    }

    private static class Alpha {
        private float dt;
        private Image image;

        private Alpha(float dt, Image image) {
            this.dt = dt;
            this.image = image;
        }

        private float update(float dt) {
            this.dt -= (0.5f * dt);
            return this.dt;
        }
    }

    public void dispose() {
        this.laserEvents.clear();
        this.fadeableRobots.clear();
    }

}
