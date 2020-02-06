package PlacableObjects;

import Board.GameBoard;
import Board.IGameBoard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceableObjectTest {

    private PlaceableObject placeableObject;
    private GameBoard board;


    @Test
    public void getPosXTest() {
        placeableObject = new PlaceableObject();
        int startX = 0;
        assertEquals(placeableObject.getPosX(),startX);
    }

    @Test
    public void getPosYTest() {
        placeableObject = new PlaceableObject();
        int startY = 0;
        assertEquals(placeableObject.getPosY(),startY);
    }
}