package roborally.ui.robot;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import roborally.game.objects.IRobot;

public interface IUIRobot {
    TiledMapTileLayer.Cell getWinTexture();

    TiledMapTileLayer.Cell getLostTexture();

    TiledMapTileLayer.Cell getTexture();

    IRobot getRobot();
}