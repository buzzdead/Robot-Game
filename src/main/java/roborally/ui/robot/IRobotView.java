package roborally.ui.robot;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import roborally.utilities.enums.Direction;

public interface IRobotView {
    /**
     * Creates new WinTexture at given position.
     *
     * @param pos the position
     */
    void setWinTexture(GridPoint2 pos);

    /**
     * Creates new damageTaken/robotDestroyed texture at position.
     *
     * @param pos the position
     */
    void setLostTexture(GridPoint2 pos);

    /**
     * @return The normal robot texture.
     */
    TiledMapTileLayer.Cell getTexture();

    /**
     * Checks if it's possible to move robot.
     *
     * @return True if its made its move else false if its on the edge of the map.
     */
    boolean moveRobot(GridPoint2 pos, GridPoint2 move);

    /**
     * @param pos       the Position
     * @param direction The direction the robot is now facing.
     */
    void setDirection(GridPoint2 pos, Direction direction);

    void goToCheckPoint(GridPoint2 pos, GridPoint2 checkPoint);

    TextureRegion[][] getTextureRegion();

    /**
     * Gets texture region from AssetsManager and sets the starting position with this texture.
     *
     * @param robotID The id of the position of the cell texture
     */
    void setTextureRegion(int robotID);

    GridPoint2 getCurrentTexture();
}
