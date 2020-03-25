package roborally.utilities.controls;

import com.badlogic.gdx.Input;
import roborally.game.Game;
import roborally.utilities.enums.Direction;

import java.util.HashMap;

public class ControlsDebug implements IControls {
    private HashMap<Integer, Runnable> menuControlMap;

    public ControlsDebug(Game game) {
        menuControlMap = new HashMap<>();
        menuControlMap.put(Input.Keys.UP, () -> game.getFirstRobot().move(1));
        menuControlMap.put(Input.Keys.DOWN, () -> game.getFirstRobot().move(-1));
        menuControlMap.put(Input.Keys.LEFT, () -> game.getFirstRobot().rotate(game.getFirstRobot().rotate(Direction.turnLeftFrom(game.getFirstRobot().getLogic().getDirection()))));
        menuControlMap.put(Input.Keys.RIGHT, () -> game.getFirstRobot().rotate(game.getFirstRobot().rotate(Direction.turnRightFrom(game.getFirstRobot().getLogic().getDirection()))));
        menuControlMap.put(Input.Keys.F, game::fireLaser);
        menuControlMap.put(Input.Keys.R, game::restartGame);
        menuControlMap.put(Input.Keys.ESCAPE, game::exitGame);
        menuControlMap.put(Input.Keys.M, game.getGameOptions()::enterMenu);
        menuControlMap.put(Input.Keys.O, game::playNextCard);
        menuControlMap.put(Input.Keys.T, game::testEndPhase);
    }

    @Override
    public Runnable getAction(int keycode) {
        if (!menuControlMap.containsKey(keycode)) {
            return this::doNothing;
        }

        return menuControlMap.get(keycode);
    }

    private void doNothing() {
        // Ok!
    }
}
