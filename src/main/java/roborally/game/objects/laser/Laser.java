package roborally.game.objects.laser;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import roborally.ui.ILayers;
import roborally.ui.Layers;
import roborally.ui.listeners.WallListener;
import roborally.utilities.AssetManagerUtil;
import roborally.utilities.enums.TileName;
import roborally.utilities.tiledtranslator.ITiledTranslator;
import roborally.utilities.tiledtranslator.TiledTranslator;

import java.util.ArrayList;

public class Laser {
    //region Field variables
    private int laserTileID;
    private int cannonTileID;
    private boolean removeLaser;

    private ITiledTranslator tiledTranslator;
    private ILayers layers;
    private WallListener wallListener;

    //region GridPoint2 positions
    private GridPoint2 robotsOrigin;
    private GridPoint2 cannonPos;
    private ArrayList<GridPoint2> laserEndPositions;
    //endregion

    //region Laser types
    private TiledMapTileLayer.Cell storedLaserCell;
    private TiledMapTileLayer.Cell crossLaser;
    private TiledMapTileLayer.Cell horizontalLaser;
    private TiledMapTileLayer.Cell verticalLaser;
    //endregion

    //endregion

    /**
     * Constructs a new laser.
     *
     * @param laserTileID Horizontal or Vertical laser.
     */
    public Laser(int laserTileID) {
        this.tiledTranslator = new TiledTranslator();
        this.layers = new Layers(); // FIXME: Isn't this created in GameBoard?
        this.laserTileID = laserTileID;
        this.laserEndPositions = new ArrayList<>();
        this.removeLaser = false;
        this.cannonPos = new GridPoint2();
        this.wallListener = new WallListener(layers);
    }

    public void clearLaser() {
        clearStoredCoordinates();
    }

    /**
     * Shoots a laser until it hits a wall or a robot. Stores the cells for clearing them after.
     * {@link #setDirection(int robotDirection)} to figure out the iterative values to shoot with.
     *
     * @param robotPosition  The position of the robot that is shooting the laser.
     * @param robotDirection The robotDirection the robot is looking.
     */
    public void fireLaser(GridPoint2 robotPosition, int robotDirection) {
        clearLaser();
        this.laserEndPositions.clear();
        this.storedLaserCell = getLaser(robotDirection);
        int[] direction = setDirection(robotDirection);
        int x = robotPosition.x + direction[0];
        int y = robotPosition.y + direction[1];

        if (this.wallListener.checkForWall(robotPosition.x, robotPosition.y, direction[0], direction[1]))
            return;
        while (x >= 0 && x < layers.getWidth() && y >= 0 && y < this.layers.getHeight()) {
            this.laserEndPositions.add(new GridPoint2(x, y));
            if (this.wallListener.checkForWall(x, y, direction[0], direction[1]) || this.layers.assertRobotNotNull(x, y)) {
                break;
            }
            x = x + direction[0];
            y = y + direction[1];
        }
    }

    /**
     * Determines which robotDirection the robot is firing it's laser.
     *
     * @param robotDirection The robotDirection the robot is facing
     * @return an array with values that determines which robotDirection the laser is being fired.
     */
    public int[] setDirection(int robotDirection) {
        int dx = 0;
        int dy = 0;
        if (robotDirection == 0)
            dy = 1;
        else if (robotDirection == 1)
            dx = -1;
        else if (robotDirection == 2)
            dy = -1;
        else if (robotDirection == 3)
            dx = 1;
        return new int[]{dx, dy};
    }

    /**
     * Finds the laser cannon and adds the coordinates of its projected laser.
     *
     * @param robotsOrigin The position the robot is or was currently in.
     */
    public void findLaser(GridPoint2 robotsOrigin) {
        int cannonId = 0;
        this.robotsOrigin = robotsOrigin;

        TileName laserTileName = tiledTranslator.getTileName(laserTileID);
        if (laserTileName == TileName.LASER_HORIZONTAL) {
            storedLaserCell = getLaser(1);
            cannonId = findHorizontal();
        } else if (laserTileName == TileName.LASER_VERTICAL) {
            storedLaserCell = getLaser(2);
            cannonId = findVertical();
        }
        laserEndPositions.add(robotsOrigin);
        this.cannonTileID = cannonId;
    }

    /**
     * Finds the cannon projecting a horizontal laser, and stores the cells of the laser.
     * Calls {@link #findCannon} with values where the laser cells end in a horizontal line.
     *
     * @return cannonId
     */
    public int findHorizontal() {
        // FIXME: Generic names, needs to be more specific
        int i = robotsOrigin.x + 1;
        int j = robotsOrigin.x - 1;
        int k = robotsOrigin.y;

        while (i < layers.getWidth() && layers.assertLaserNotNull(i, k)) i++;
        while (j >= 0 && layers.assertLaserNotNull(j, k)) j--;

        cannonTileID = findCannon(i, j, k);

        if (cannonTileID != 0) {
            int dx;
            TileName cannonTileName = tiledTranslator.getTileName(cannonTileID);
            if (cannonTileName == TileName.WALL_CANNON_RIGHT)
                dx = -1;
            else {
                dx = 1;
                i = j;
            }
            this.cannonPos.set(i + dx, k);
            do {
                laserEndPositions.add(new GridPoint2(i += dx, k));
            } while (!wallListener.checkForWall(i, k, dx, 0) && i >= 0 && i <= layers.getWidth());
        }
        return cannonTileID;
    }

    /**
     * Finds the cannon projecting a vertical laser, and stores laser cells.
     * Calls {@link #findCannon} with values where the laser cells end in a vertical line.
     *
     * @return cannonId
     */
    public int findVertical() {
        // FIXME: Generic names, needs to be more specific
        int i = robotsOrigin.y + 1;
        int j = robotsOrigin.y - 1;
        int k = robotsOrigin.x;

        while (i < layers.getHeight() && layers.assertLaserNotNull(k, i)) i++;
        while (j >= 0 && layers.assertLaserNotNull(k, j)) j--;
        cannonTileID = findCannon(i, j, k);
        if (cannonTileID != 0) {
            int dy;
            TileName cannonTileName = tiledTranslator.getTileName(cannonTileID);
            if (cannonTileName == TileName.WALL_CANNON_BOTTOM)
                dy = 1;
            else {
                dy = -1;
                j = i;
            }
            this.cannonPos.set(k, j + dy);
            do {
                laserEndPositions.add(new GridPoint2(k, j += dy));
            } while (!wallListener.checkForWall(k, j, 0, dy) && j >= 0 && j <= layers.getHeight());
        }
        return cannonTileID;
    }

    /**
     * Updates the cannon when there is activity, and continues to do so until it is registered in Lasers
     * that no robot is no longer active in the laser. Calls {@link #identifyLaser} to handle cross-lasers.
     */
    public void update() {
        for (GridPoint2 pos : laserEndPositions) {
            if (identifyLaser(pos.x, pos.y, false))
                layers.setLaserCell(pos.x, pos.y, null);
        }
        if (removeLaser)
            return;
        for (GridPoint2 pos : laserEndPositions) {
            if (identifyLaser(pos.x, pos.y, true))
                layers.setLaserCell(pos.x, pos.y, this.storedLaserCell);
            if (layers.assertRobotNotNull(pos.x, pos.y))
                break;
        }
    }

    /**
     * Calculates where the laser is coming from.
     *
     * @param i The negative sides endpoint (down or left) which the laser might be coming from.
     * @param j The positive sides endpoint (up or right) which the laser might be coming from.
     * @param k The static x or y coordinate.
     * @return cannonId
     */
    private int findCannon(int i, int j, int k) {
        TileName laserTileName = tiledTranslator.getTileName(laserTileID);
        if (laserTileName == TileName.LASER_VERTICAL) {
            if (layers.assertLaserCannonNotNull(k, i - 1))
                return layers.getLaserCannonID(k, i - 1);
            if (layers.assertLaserCannonNotNull(k, j + 1))
                return layers.getLaserCannonID(k, j + 1);
        } else {
            if (layers.assertLaserCannonNotNull(i - 1, k))
                return layers.getLaserCannonID(i - 1, k);
            if (layers.assertLaserCannonNotNull(j + 1, k))
                return layers.getLaserCannonID(j + 1, k);
        }
        return 0;
    }

    /**
     * Returns true if the robot is currently in a laser instance
     */
    public boolean gotPos(GridPoint2 pos) {
        return laserEndPositions.contains(pos);
    }

    public void clearStoredCoordinates() {
        laserEndPositions.clear();
    }

    public ArrayList<GridPoint2> getCoords() {
        return this.laserEndPositions;
    }

    /**
     * Identifies when to different laser cells intersect, creates a cross laser.
     *
     * @param i the x-position of the cell
     * @param j the y-position of the cell
     * @return false if there is a cross-laser present, or if logic determines there to be one.
     */
    public boolean identifyLaser(int i, int j, boolean create) {
        if (layers.assertLaserNotNull(i, j)) {
            if (layers.getLaserID(i, j) == crossLaser.getTile().getId() && !create) {
                int storedLaserCellID = storedLaserCell.getTile().getId();

                TileName laserTileName = tiledTranslator.getTileName(storedLaserCellID);
                if (laserTileName == TileName.LASER_VERTICAL)
                    layers.setLaserCell(i, j, horizontalLaser);
                else
                    layers.setLaserCell(i, j, verticalLaser);
                return false;
            } else if (layers.getLaserID(i, j) != storedLaserCell.getTile().getId() && create) {
                layers.setLaserCell(i, j, crossLaser);
                return false;
            } else return layers.getLaserID(i, j) == storedLaserCell.getTile().getId() || create;
        }
        return true;
    }

    public int getID() {
        return this.cannonTileID;
    }

    public GridPoint2 getPosition() {
        return this.cannonPos;
    }

    /**
     * Create laser tiles if they are not created.
     *
     * @param laserDirection the direction the laser is going
     * @return the laser cell.
     */
    public TiledMapTileLayer.Cell getLaser(int laserDirection) {
        if (storedLaserCell == null) {
            horizontalLaser = new TiledMapTileLayer.Cell();
            horizontalLaser.setTile(AssetManagerUtil.getTileSets().getTile(TileName.LASER_HORIZONTAL.getTileID()));

            verticalLaser = new TiledMapTileLayer.Cell();
            verticalLaser.setTile(AssetManagerUtil.getTileSets().getTile(TileName.LASER_VERTICAL.getTileID()));

            crossLaser = new TiledMapTileLayer.Cell();
            crossLaser.setTile(AssetManagerUtil.getTileSets().getTile(TileName.LASER_CROSS.getTileID()));
        }
        if (laserDirection == 0 || laserDirection == 2)
            return verticalLaser;
        else
            return horizontalLaser;
    }
}