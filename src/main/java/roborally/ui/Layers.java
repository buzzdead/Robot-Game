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
    
    // Robots
    
    public TiledMapTileLayer getRobots() {
        return layers.get("Robot");
    }

    
    public TiledMapTileLayer.Cell getRobotCell(int x, int y) {
        if (!contains("Robot"))
            return null;
        return layers.get("Robot").getCell(x, y);
    }

    
    public void setRobotCell(int x, int y, TiledMapTileLayer.Cell cell) {
        if (contains("Robot"))
            layers.get("Robot").setCell(x, y, cell);
        else
            System.out.println("Robot layer does not exist");
    }

    
    public boolean assertRobotNotNull(int x, int y) {
        if (contains("Robot"))
            return layers.get("Robot").getCell(x, y) != null;
        System.out.println("Robot layer does not exist");
        return false;
    }

    // Walls
    
    public TiledMapTileLayer getWall() {
        return layers.get("Wall");
    }

    
    public TiledMapTileLayer.Cell getWallCell(int x, int y) {
        if (!contains("Wall"))
            return null;
        return layers.get("Wall").getCell(x, y);
    }

    
    public boolean assertWallNotNull(int x, int y) {
        if (contains("Wall"))
            return layers.get("Wall").getCell(x, y) != null;
        System.out.println("Wall layer does not exist");
        return false;
    }

    
    public int getWallID(int x, int y) {
        if (contains("Wall"))
            return layers.get("Wall").getCell(x, y).getTile().getId();
        else
            System.out.println("Wall layer does not exist");
        return -1;
    }

    // Floor
    
    public TiledMapTileLayer getFloor() {
        return layers.get("Floor");
    }

    
    public boolean assertFloorNotNull(int x, int y) {
        if (contains("Floor"))
            return layers.get("Floor").getCell(x, y) != null;
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

    
    public boolean assertHoleNotNull(int x, int y) {
        if (contains("Hole"))
            return layers.get("Hole").getCell(x, y) != null;
        System.out.println("Hole layer does not exist");
        return false;
    }

    // Flags
    
    public TiledMapTileLayer getFlag() {
        return layers.get("Flag");
    }

    
    public boolean assertFlagNotNull(int x, int y) {
        if (contains("Flag"))
            return layers.get("Flag").getCell(x, y) != null;
        System.out.println("Flag layer does not exist");
        return false;
    }

    
    public int getFlagID(int x, int y) {
        if (contains("Flag"))
            return layers.get("Flag").getCell(x, y).getTile().getId();
        else
            System.out.println("Flag layer does not exist");
        return -1;
    }

    // Start positions
    
    public TiledMapTileLayer getStartPos() {
        return layers.get("startPositions");
    }

    
    public boolean assertStartPosNotNull(int x, int y) {
        if (contains("startPositions"))
            return layers.get("startPositions").getCell(x, y) != null;
        System.out.println("startPositions does not exist");
        return false;
    }

    // Conveyor belts
    
    public TiledMapTileLayer getConveyorSlow() {
        return layers.get("slowConveyorBelt");
    }

    
    public boolean assertConveyorSlowNotNull(int x, int y) {
        if (contains("slowConveyorBelt"))
            return layers.get("slowConveyorBelt").getCell(x, y) != null;
        System.out.println("slowConveyorBelt does not exist");
        return false;
    }

    
    public TileName getConveyorSlowTileName(GridPoint2 pos) {
        return tiledTranslator.getTileName(layers.get("slowConveyorBelt").getCell(pos.x, pos.y).getTile().getId());
    }

    
    public TiledMapTileLayer getConveyorFast() {
        return layers.get("fastConveyorBelt");
    }

    
    public boolean assertConveyorFastNotNull(int x, int y) {
        if (contains("fastConveyorBelt"))
            return layers.get("fastConveyorBelt").getCell(x, y) != null;
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

    
    public boolean assertWrenchNotNull(int x, int y) {
        if (contains("Wrench"))
            return layers.get("Wrench").getCell(x, y) != null;
        System.out.println("Wrench layer does not exist");
        return false;
    }

    
    public TiledMapTileLayer getWrenchHammer() {
        return layers.get("wrenchHammer");
    }

    
    public boolean assertWrenchHammerNotNull(int x, int y) {
        if (contains("wrenchHammer"))
            return layers.get("wrenchHammer").getCell(x, y) != null;
        System.out.println("wrenchHammer layer does not exist");
        return false;
    }

    // Lasers
    
    public TiledMapTileLayer getLaser() {
        return layers.get("Laser");
    }

    
    public boolean assertLaserNotNull(int x, int y) {
        if (contains("Laser"))
            return layers.get("Laser").getCell(x, y) != null;
        System.out.println("Laser layer does not exist");
        return false;
    }

    
    public TiledMapTileLayer.Cell getLaserCell(int x, int y) {
        if (!contains("Laser"))
            return null;
        return layers.get("Laser").getCell(x, y);
    }

    
    public void setLaserCell(int x, int y, TiledMapTileLayer.Cell cell) {
        if (contains("Laser"))
            layers.get("Laser").setCell(x, y, cell);
        else
            System.out.println("Laser layer does not exist");
    }

    
    public int getLaserID(int x, int y) {
        if (contains("Laser"))
            return layers.get("Laser").getCell(x, y).getTile().getId();
        else
            System.out.println("Laser layer does not exist");
        return -1;
    }

    
    public int getLaserCannonID(int x, int y) {
        if (contains("laserCannon"))
            return layers.get("laserCannon").getCell(x, y).getTile().getId();
        else
            System.out.println("laserCannon layer does not exist");
        return -1;
    }

    
    public boolean assertLaserCannonNotNull(int x, int y) {
        if (contains("laserCannon"))
            return layers.get("laserCannon").getCell(x, y) != null;
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

    
    public boolean assertBugNotNull(int x, int y) {
        return layers.get("bug").getCell(x, y) != null;
    }
}
