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
     * @param name          The name of the robot
     * @param laserRegister the register.
     */
    public boolean checkForLasers(GridPoint2 pos, String name, LaserRegister laserRegister) {
        int id;
        if (layers.assertLaserNotNull(pos)) {
            id = layers.getLaserID(pos);
            laserRegister.createLaser(id, pos, name);
            return true;
        } else
            laserRegister.updateLaser(name, pos);
        return false;
    }
}
