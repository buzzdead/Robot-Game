package roborally.ui.listeners;

import com.badlogic.gdx.math.GridPoint2;
import roborally.game.objects.laser.LaserRegister;
import roborally.ui.Layers;

public class Listener {
    private WallListener wallListener;
    private CollisionListener collisionListener;
    private LaserListener laserListener;

    // Makes a new Listener to listen to the different listeners.
    public Listener(Layers layers) {
        this.wallListener = new WallListener(layers);
        this.collisionListener = new CollisionListener(layers);
        this.laserListener = new LaserListener(layers);
    }

    public void listenMove(GridPoint2 pos, int dx, int dy, String name, LaserRegister laserRegister) {
        // Adding a move listener to listen to them all in one swoop.
    }

    public void listenWall(int x, int y, int dx, int dy) {
        wallListener.checkForWall(x, y, dx, dy);
    }

    public boolean listenCollision(int x, int y, int dx, int dy) {
        return collisionListener.checkIfBlocked(x, y, dx, dy);
    }

    public boolean listenLaser(int x, int y, String name, LaserRegister laserRegister) {
        return this.laserListener.checkForLasers(x, y, name, laserRegister);
    }
}
