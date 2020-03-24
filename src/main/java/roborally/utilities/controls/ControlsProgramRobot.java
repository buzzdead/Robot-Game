package roborally.utilities.controls;

import com.badlogic.gdx.Input;
import roborally.game.Game;

import java.util.HashMap;

public class ControlsProgramRobot implements IControls {
    private HashMap<Integer, Runnable> controlMap;

    public ControlsProgramRobot(Game game) {
        controlMap = new HashMap<>();
        // TODO: Add controls for programming robot
        controlMap.put(Input.Keys.ESCAPE, game::exitGame);
    }

    @Override
    public Runnable getAction(int keycode) {
        if (!controlMap.containsKey(keycode)) {
            return this::doNothing;
        }
        return controlMap.get(keycode);
    }

    private void doNothing() {
        // Ok!
    }
}
