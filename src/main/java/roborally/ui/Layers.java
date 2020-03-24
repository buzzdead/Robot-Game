package roborally.ui;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import roborally.utilities.AssetManagerUtil;
import roborally.utilities.enums.TileName;
import roborally.utilities.tiledtranslator.TiledTranslator;

import java.util.HashMap;

// Getters for various layers in the current TiledMap.
public class Layers {
    private TiledTranslator tiledTranslator;
    private HashMap<String, TiledMapTileLayer> layers;

    public Layers() {
        tiledTranslator = new TiledTranslator();
        layers = new HashMap<>(AssetManagerUtil.getLoadedLayers());
    }


    public boolean isLoaded() {
        return !layers.isEmpty();
    }


    public boolean contains(String key) {
        return layers.containsKey(key);
    }

    public boolean assertPusherNotNull(GridPoint2 pos) {
        return layers.get("Pusher").getCell(pos.x, pos.y) != null;
    }

    // Robots

    public TiledMapTileLayer getRobots() {
        return layers.get("Robot");
    }


    public TiledMapTileLayer.Cell getRobotCell(GridPoint2 pos) {
        if (!contains("Robot"))
            return null;
        return layers.get("Robot").getCell(pos.x, pos.y);
    }


    public void setRobotCell(GridPoint2 pos, TiledMapTileLayer.Cell cell) {
        if (contains("Robot"))
            layers.get("Robot").setCell(pos.x, pos.y, cell);
        else
            System.out.println("Robot layer does not exist");
    }


    public boolean assertRobotNotNull(GridPoint2 pos) {
        if (contains("Robot"))
            return layers.get("Robot").getCell(pos.x, pos.y) != null;
        System.out.println("Robot layer does not exist");
        return false;
    }

    // Walls
    
    public TiledMapTileLayer getWall() {
        return layers.get("Wall");
    }


    public TiledMapTileLayer.Cell getWallCell(GridPoint2 pos) {
        if (!contains("Wall"))
            return null;
        return layers.get("Wall").getCell(pos.x, pos.y);
    }


    public boolean assertWallNotNull(GridPoint2 pos) {
        if (contains("Wall"))
            return layers.get("Wall").getCell(pos.x, pos.y) != null;
        System.out.println("Wall layer does not exist");
        return false;
    }


    public int getWallID(GridPoint2 pos) {
        if (contains("Wall"))
            return layers.get("Wall").getCell(pos.x, pos.y).getTile().getId();
        else
            System.out.println("Wall layer does not exist");
        return -1;
    }

    // Floor
    
    public TiledMapTileLayer getFloor() {
        return layers.get("Floor");
    }


    public boolean assertFloorNotNull(GridPoint2 pos) {
        if (contains("Floor"))
            return layers.get("Floor").getCell(pos.x, pos.y) != null;
        System.out.println("Floor layer does not exist");
        return false;
    }

    
    public int getWidth() {
        return layers.get("Floor").getWidth();
    }

    
    public int getHeight() {
        return layers.get("Floor").getHeight();
    }

    // Holes
    
    public TiledMapTileLayer getHole() {
        return layers.get("Hole");
    }


    public boolean assertHoleNotNull(GridPoint2 pos) {
        if (contains("Hole"))
            return layers.get("Hole").getCell(pos.x, pos.y) != null;
        System.out.println("Hole layer does not exist");
        return false;
    }

    // Flags
    
    public TiledMapTileLayer getFlag() {
        return layers.get("Flag");
    }


    public boolean assertFlagNotNull(GridPoint2 pos) {
        if (contains("Flag"))
            return layers.get("Flag").getCell(pos.x, pos.y) != null;
        System.out.println("Flag layer does not exist");
        return false;
    }


    public int getFlagID(GridPoint2 pos) {
        if (contains("Flag"))
            return layers.get("Flag").getCell(pos.x, pos.y).getTile().getId();
        else
            System.out.println("Flag layer does not exist");
        return -1;
    }

    // Start positions
    
    public TiledMapTileLayer getStartPos() {
        return layers.get("startPositions");
    }


    public boolean assertStartPosNotNull(GridPoint2 pos) {
        if (contains("startPositions"))
            return layers.get("startPositions").getCell(pos.x, pos.y) != null;
        System.out.println("startPositions does not exist");
        return false;
    }

    // Conveyor belts
    
    public TiledMapTileLayer getConveyorSlow() {
        return layers.get("slowConveyorBelt");
    }


    public boolean assertConveyorSlowNotNull(GridPoint2 pos) {
        if (contains("slowConveyorBelt"))
            return layers.get("slowConveyorBelt").getCell(pos.x, pos.y) != null;
        System.out.println("slowConveyorBelt does not exist");
        return false;
    }

    
    public TileName getConveyorSlowTileName(GridPoint2 pos) {
        return tiledTranslator.getTileName(layers.get("slowConveyorBelt").getCell(pos.x, pos.y).getTile().getId());
    }

    
    public TiledMapTileLayer getConveyorFast() {
        return layers.get("fastConveyorBelt");
    }


    public boolean assertConveyorFastNotNull(GridPoint2 pos) {
        if (contains("fastConveyorBelt"))
            return layers.get("fastConveyorBelt").getCell(pos.x, pos.y) != null;
        System.out.println("fastConveyorBelt does not exist");
        return false;
    }

    
    public TileName getConveyorFastTileName(GridPoint2 pos) {
        return tiledTranslator.getTileName(layers.get("fastConveyorBelt").getCell(pos.x, pos.y).getTile().getId());
    }

    // Wrenches
    
    public TiledMapTileLayer getWrench() {
        return layers.get("Wrench");
    }


    public boolean assertWrenchNotNull(GridPoint2 pos) {
        if (contains("Wrench"))
            return layers.get("Wrench").getCell(pos.x, pos.y) != null;
        System.out.println("Wrench layer does not exist");
        return false;
    }

    
    public TiledMapTileLayer getWrenchHammer() {
        return layers.get("wrenchHammer");
    }


    public boolean assertWrenchHammerNotNull(GridPoint2 pos) {
        if (contains("wrenchHammer"))
            return layers.get("wrenchHammer").getCell(pos.x, pos.y) != null;
        System.out.println("wrenchHammer layer does not exist");
        return false;
    }

    // Lasers
    
    public TiledMapTileLayer getLaser() {
        return layers.get("Laser");
    }


    public boolean assertLaserNotNull(GridPoint2 pos) {
        if (contains("Laser"))
            return layers.get("Laser").getCell(pos.x, pos.y) != null;
        System.out.println("Laser layer does not exist");
        return false;
    }


    public TiledMapTileLayer.Cell getLaserCell(GridPoint2 pos) {
        if (!contains("Laser"))
            return null;
        return layers.get("Laser").getCell(pos.x, pos.y);
    }


    public void setLaserCell(GridPoint2 pos, TiledMapTileLayer.Cell cell) {
        if (contains("Laser"))
            layers.get("Laser").setCell(pos.x, pos.y, cell);
        else
            System.out.println("Laser layer does not exist");
    }


    public int getLaserID(GridPoint2 pos) {
        if (contains("Laser"))
            return layers.get("Laser").getCell(pos.x, pos.y).getTile().getId();
        else
            System.out.println("Laser layer does not exist");
        return -1;
    }


    public int getLaserCannonID(GridPoint2 pos) {
        if (contains("laserCannon"))
            return layers.get("laserCannon").getCell(pos.x, pos.y).getTile().getId();
        else
            System.out.println("laserCannon layer does not exist");
        return -1;
    }


    public boolean assertLaserCannonNotNull(GridPoint2 pos) {
        if (contains("laserCannon"))
            return layers.get("laserCannon").getCell(pos.x, pos.y) != null;
        System.out.println("laserCannon layer does not exist");
        return false;
    }

    // Gear
    
    public boolean assertGearNotNull(GridPoint2 pos) {
        return layers.get("Gear").getCell(pos.x, pos.y) != null;
    }

    
    public TileName getGearTileName(GridPoint2 pos) {
        return tiledTranslator.getTileName(layers.get("Gear").getCell(pos.x, pos.y).getTile().getId());
    }

    // Bug
    // Returns the bug layer.

    public TiledMapTileLayer getBug() {
        return layers.get("bug");
    }


    public boolean assertBugNotNull(GridPoint2 pos) {
        return layers.get("bug").getCell(pos.x, pos.y) != null;
    }
}
