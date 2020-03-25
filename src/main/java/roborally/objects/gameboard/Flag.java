package roborally.objects.gameboard;

import com.badlogic.gdx.math.GridPoint2;

public class Flag {
    private int id;
    private GridPoint2 pos;

    public Flag(int id, GridPoint2 pos) {
        this.id = id;
        this.pos = pos;
    }

    public GridPoint2 getPosition() {
        return pos;
    }

    public int getID() {
        return id;
    }
}
