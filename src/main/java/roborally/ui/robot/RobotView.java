package roborally.ui.robot;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.GridPoint2;
import roborally.ui.Layers;
import roborally.ui.gdx.UI;
import roborally.utilities.AssetManagerUtil;
import roborally.utilities.enums.Direction;

public class RobotView implements IRobotView {

    private TiledMapTileLayer.Cell robotWonCellTexture;
    private TiledMapTileLayer.Cell robotLostCellTexture;
    private TextureRegion[][] robotTextureRegion;
    private TiledMapTileLayer.Cell robotDefaultCellTexture;
    private Layers layers;
    private GridPoint2 pos;
    private int height;
    private int width;

    public RobotView(int x, int y) {
        this.pos = new GridPoint2(x, y);
        this.layers = new Layers();
        this.height = layers.getHeight();
        this.width = layers.getWidth();
    }

    @Override
    public void setWinTexture(GridPoint2 pos) {
    }

    @Override
    public void setLostTexture(GridPoint2 pos) {
        }

    @Override
    public TiledMapTileLayer.Cell getTexture() {
        if (this.robotDefaultCellTexture == null) {
            this.robotDefaultCellTexture = new TiledMapTileLayer.Cell();
            this.robotDefaultCellTexture.setTile(new StaticTiledMapTile(robotTextureRegion[1][1]));
        }
        return this.robotDefaultCellTexture;
    }

    @Override
    public TextureRegion[][] getTextureRegion() {
        return this.robotTextureRegion;
    }

    @Override
    public void setTextureRegion(int robotID) {
        this.robotTextureRegion = TextureRegion.split(AssetManagerUtil.getRobotTexture(robotID), 250, 300);
        layers.setRobotCell(this.pos.x, this.pos.y, getTexture());
    }

    @Override
    public boolean moveRobot(int x, int y, int dx, int dy) {
        int newX = x + dx;
        int newY = y + dy;
        if ((newX >= 0) && (newY >= 0) && (newX < width) && (newY < height)) {
            layers.setRobotCell(newX, newY, getTexture());
            layers.setRobotCell(x, y, null);
            return true;
        }
        return false;
    }

    @Override
    public void goToCheckPoint(GridPoint2 pos, GridPoint2 checkPoint) {
        layers.setRobotCell(checkPoint.x, checkPoint.y, getTexture());
        layers.getRobotCell(checkPoint.x, checkPoint.y).setRotation(0);
        if (!pos.equals(checkPoint))
            layers.setRobotCell(pos.x, pos.y, null);
    }

    @Override
    public void setDirection(GridPoint2 pos, Direction direction) {
        if (layers.assertRobotNotNull(pos.x, pos.y))
            if(direction==Direction.North)
                this.robotDefaultCellTexture.setTile(new StaticTiledMapTile(robotTextureRegion[1][1]));
            else if(direction==Direction.West)
                this.robotDefaultCellTexture.setTile(new StaticTiledMapTile(robotTextureRegion[1][0]));
            else if(direction==Direction.South)
                this.robotDefaultCellTexture.setTile(new StaticTiledMapTile(robotTextureRegion[0][0]));
            else
                this.robotDefaultCellTexture.setTile(new StaticTiledMapTile(robotTextureRegion[0][1]));
    }
}
