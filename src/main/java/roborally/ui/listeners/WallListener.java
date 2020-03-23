package roborally.ui.listeners;

import roborally.ui.Layers;
import roborally.utilities.enums.TileName;
import roborally.utilities.tiledtranslator.TiledTranslator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WallListener {
    private HashMap<String, List<TileName>> mapOfWallNames;
    private TiledTranslator tiledTranslator;
    private Layers layers;

    public WallListener(Layers layers) {
        this.mapOfWallNames = makeWallMap();
        this.tiledTranslator = new TiledTranslator();
        this.layers = layers;
    }

    /**
     * A method that looks through the respective ID's from the tileset, for relevant walls for the robot as it
     * tries to move.
     *
     * @param x  the x position
     * @param y  the y position
     * @param dx steps taken in x-direction
     * @param dy steps taken in y-direction
     * @return True if it finds a wall that corresponds to a wall in x or y direction that blocks the robot.
     */
    public boolean checkForWall(int x, int y, int dx, int dy) {
        boolean wall = false;
        TileName wallName;
        if (layers.assertWallNotNull(x, y)) {
            wallName = tiledTranslator.getTileName(layers.getWallID(x, y));
            if (dy > 0)
                wall = mapOfWallNames.get("North").contains(wallName);
            else if (dy < 0)
                wall = mapOfWallNames.get("South").contains(wallName);
            else if (dx > 0)
                wall = mapOfWallNames.get("East").contains(wallName);
            else if (dx < 0)
                wall = mapOfWallNames.get("West").contains(wallName);
        }
        if (layers.assertWallNotNull(x + dx, y + dy) && !wall) {
            wallName = tiledTranslator.getTileName(layers.getWallID(x + dx, y + dy));
            if (dy > 0)
                return mapOfWallNames.get("South").contains(wallName);
            else if (dy < 0)
                return mapOfWallNames.get("North").contains(wallName);
            else if (dx > 0)
                return mapOfWallNames.get("West").contains(wallName);
            else if (dx < 0)
                return mapOfWallNames.get("East").contains(wallName);
        }
        return wall;
    }

    public HashMap<String, List<TileName>> makeWallMap() {
        HashMap<String, List<TileName>> mapOfWallNames = new HashMap<>();
        TileName[] tnNorth = {TileName.WALL_TOP, TileName.WALL_CORNER_TOP_LEFT, TileName.WALL_CORNER_TOP_RIGHT};
        TileName[] tnSouth = {TileName.WALL_BOTTOM, TileName.WALL_CORNER_BOTTOM_LEFT, TileName.WALL_CORNER_BOTTOM_RIGHT};
        TileName[] tnWest = {TileName.WALL_LEFT, TileName.WALL_CORNER_BOTTOM_LEFT, TileName.WALL_CORNER_TOP_LEFT};
        TileName[] tnEast = {TileName.WALL_RIGHT, TileName.WALL_CORNER_TOP_RIGHT, TileName.WALL_CORNER_BOTTOM_RIGHT};
        mapOfWallNames.put("North", (Arrays.asList(tnNorth)));
        mapOfWallNames.put("South", (Arrays.asList(tnSouth)));
        mapOfWallNames.put("East", (Arrays.asList(tnEast)));
        mapOfWallNames.put("West", (Arrays.asList(tnWest)));
        return mapOfWallNames;
    }
}
