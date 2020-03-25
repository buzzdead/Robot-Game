package roborally.ui.listeners;

import com.badlogic.gdx.math.GridPoint2;
import roborally.objects.laser.LaserRegister;
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

    public void listenWall(GridPoint2 pos, GridPoint2 move) {
        wallListener.checkForWall(pos, move);
    }

    public boolean listenCollision(GridPoint2 pos, GridPoint2 move) {
        return collisionListener.checkIfBlocked(pos, move);
    }

    public boolean listenLaser(GridPoint2 pos, String name, LaserRegister laserRegister) {
        return this.laserListener.checkForLasers(pos, name, laserRegister);
    }
}
