package roborally.ui.listeners;

import com.badlogic.gdx.math.GridPoint2;
import roborally.game.objects.laser.LaserRegister;
import roborally.ui.Layers;

public class LaserListener {
    private Layers layers;

    public LaserListener(Layers layers) {
        this.layers = layers;
    }

    /**
     * Checks the laser the robot has stepped in against the register to update, remove or add the laser.
     *
     * @param x             The x-coordinate the robot is moving to
     * @param y             The y-coordinate the robot is moving to
     * @param name          The name of the robot
     * @param laserRegister the register.
     */
    public boolean checkForLasers(int x, int y, String name, LaserRegister laserRegister) {
        GridPoint2 pos = new GridPoint2(x, y);
        int id;
        if (layers.assertLaserNotNull(x, y)) {
            id = layers.getLaserID(x, y);
            laserRegister.createLaser(id, pos, name);
            return true;
        } else
            laserRegister.updateLaser(name, pos);
        return false;
    }
}
