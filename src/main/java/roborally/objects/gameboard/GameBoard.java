package roborally.objects.gameboard;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import roborally.ui.Layers;
import roborally.utilities.enums.TileName;
import roborally.utilities.tiledtranslator.TiledTranslator;

import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard {

    private Layers layers;
    private HashMap<TileName, Integer> flagIdMap;

    private TiledTranslator tiledTranslator;

    public GameBoard() {
        tiledTranslator = new TiledTranslator();

        this.flagIdMap = new HashMap<>();
        this.flagIdMap.put(TileName.FLAG_1, 1);
        this.flagIdMap.put(TileName.FLAG_2, 2);
        this.flagIdMap.put(TileName.FLAG_3, 3);
        this.flagIdMap.put(TileName.FLAG_4, 4);
        this.layers = new Layers();
    }
    
    public ArrayList<Flag> findAllFlags() {
        ArrayList<Flag> flags = new ArrayList<>();
        TiledMapTileLayer flagLayer = layers.getFlag();

        for (int x = 0; x < flagLayer.getWidth(); x++) {
            for (int y = 0; y < flagLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = flagLayer.getCell(x, y);
                if (cell != null) {
                    int tileID = cell.getTile().getId();
                    TileName tileName = tiledTranslator.getTileName(tileID);
                    int flagId = flagIdMap.get(tileName);

                    flags.add(new Flag (flagId, new GridPoint2(x, y)));
                }
            }
        }
        return flags;
    }
    
    public Layers getLayers() {
        return this.layers;
    }
}
