package roborally.ui.robot;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.GridPoint2;
import roborally.ui.Layers;
import roborally.utilities.AssetManagerUtil;
import roborally.utilities.enums.Direction;

public class RobotView implements IRobotView {

    private TiledMapTileLayer.Cell robotWonCellTexture;
    private TiledMapTileLayer.Cell robotLostCellTexture;
    private TextureRegion[][] robotTextureRegion;
    private TextureRegion[][] tr;
    private TiledMapTileLayer.Cell robotDefaultCellTexture;
    private Layers layers;
    private GridPoint2 pos;
    private int height;
    private int width;
    private GridPoint2 currentTexture;
    private int robotID;

    public RobotView(GridPoint2 pos) {
        this.pos = pos;
        this.layers = new Layers();
        this.height = layers.getHeight();
        this.width = layers.getWidth();
        currentTexture = new GridPoint2(0, 0);
    }

    @Override
    public void setWinTexture(GridPoint2 pos) {
    }

    @Override
    public void setLostTexture(GridPoint2 pos) {
        }

    @Override
    public TiledMapTileLayer.Cell getTexture() {
        tr = TextureRegion.split(AssetManagerUtil.getRobotTexture(robotID), 250, 300);

        if (this.robotDefaultCellTexture == null) {
            this.robotDefaultCellTexture = new TiledMapTileLayer.Cell();
            this.robotDefaultCellTexture.setTile(new StaticTiledMapTile(tr[1][1]));
            this.currentTexture.set(1, 1);
        }
        this.robotDefaultCellTexture.getTile().getTextureRegion().setRegion(0, 0, 0, 0);
        return this.robotDefaultCellTexture;
    }

    @Override
    public TextureRegion[][] getTextureRegion() {
        return this.robotTextureRegion;
    }

    @Override
    public GridPoint2 getCurrentTexture() {
        return this.currentTexture;
    }

    @Override
    public void setTextureRegion(int robotID) {
        this.robotID = robotID;
        this.robotTextureRegion = TextureRegion.split(AssetManagerUtil.getRobotTexture(robotID), 250, 300);
        layers.setRobotCell(this.pos, getTexture());
    }

    @Override
    public boolean moveRobot(GridPoint2 pos, GridPoint2 move) {
        GridPoint2 newPos = pos.cpy().add(move);
        if ((newPos.x >= 0) && (newPos.y >= 0) && (newPos.x < width) && (newPos.y < height)) {
            layers.setRobotCell(newPos, getTexture());
            layers.setRobotCell(pos, null);
            return true;
        }
        return false;
    }

    @Override
    public void goToCheckPoint(GridPoint2 pos, GridPoint2 checkPoint) {
        layers.setRobotCell(checkPoint, getTexture());
        layers.getRobotCell(checkPoint).setRotation(0);
        if (!pos.equals(checkPoint))
            layers.setRobotCell(pos, null);
    }

    @Override
    public void setDirection(GridPoint2 pos, Direction direction) {
        if (layers.assertRobotNotNull(pos))
            if (direction == Direction.North) {
                this.robotDefaultCellTexture.setTile(new StaticTiledMapTile(tr[1][1]));
                this.currentTexture.set(1, 1);
            } else if (direction == Direction.West) {
                this.robotDefaultCellTexture.setTile(new StaticTiledMapTile(tr[1][0]));
                this.currentTexture.set(1, 0);
            } else if (direction == Direction.South) {
                this.robotDefaultCellTexture.setTile(new StaticTiledMapTile(tr[0][0]));
                this.currentTexture.set(0, 0);
            } else {
                this.robotDefaultCellTexture.setTile(new StaticTiledMapTile(tr[0][1]));
                this.currentTexture.set(0, 1);
            }
        this.robotDefaultCellTexture.getTile().getTextureRegion().setRegion(0, 0, 0, 0);
    }
}
