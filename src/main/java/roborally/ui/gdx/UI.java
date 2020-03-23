package roborally.ui.gdx;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import roborally.game.Game;
import roborally.game.IGame;
import roborally.ui.gdx.events.Events;
import roborally.ui.gdx.events.LaserEvent;
import roborally.utilities.AssetManagerUtil;
import roborally.utilities.SettingsUtil;
import roborally.utilities.controls.ControlsDebug;

public class UI extends InputAdapter implements ApplicationListener {

    // Size of tile, both height and width
    public static final int TILE_SIZE = 300;
    private IGame game;
    private TiledMap tiledMap;
    private int mapID;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Menu menu;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private ControlsDebug debugControls;
    private boolean paused;
    private Stage stage;
    private boolean cardPhase;
    private ProgramCardsView programCardsView;
    private Events events;
    private Image bar;
    private Image bar2;
    private Image bar3;
    private Image bar4;
    private Image poweredDown;


    public UI() {
        this.paused = true;
        this.mapID = 1;
        this.cardPhase = false;
        this.events = new Events();
        this.programCardsView = new ProgramCardsView();
    }

    @Override
    public void create() {
        // Prepare assets manager
        AssetManagerUtil.manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        AssetManagerUtil.load();
        AssetManagerUtil.manager.finishLoading();

        // Initialize the uimap
        tiledMap = AssetManagerUtil.getMap(mapID);

        // Start a new game
        //boolean runAIGame = true;
        //game = new Game(runAIGame);
        game = new Game(this.events);

        // Setup controls for the game
        debugControls = new ControlsDebug(game);

        // Initialize the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth()+250, Gdx.graphics.getHeight()+150);
        camera.translate(-125, -75);
        camera.update();
        // Initialize the map renderer
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 3 / 16f);
        mapRenderer.setView(camera);
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
        stage = new Stage();

        menu = new Menu(stage, events);
        bar = new Image(new Texture("assets/bar.png"));
        bar.setY(613);
        bar2 = new Image(new Texture("assets/bar2.png"));
        bar3 = new Image(new Texture("assets/bar2.png"));
        bar2.setHeight(675);
        bar3.setHeight(675);
        bar3.setPosition(675+125, 0);
        game.getGameOptions().enterMenu(true);
        bar4 = new Image(new Texture("assets/bar.png"));
        bar4.setHeight(62);
        poweredDown = new Image(new Texture("assets/powereddown.png"));
        poweredDown.setY(0);
        poweredDown.setX(815);
        poweredDown.setSize(75, 75);

    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
        events.dispose();
        stage.dispose();
        AssetManagerUtil.dispose();
    }

    @Override
    public void render() {
        if (events.hasWaitEvent() && !events.hasLaserEvent())
            events.waitMoveEvent(Gdx.graphics.getDeltaTime(), game);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        mapRenderer.render();
        if (paused) {
            pause();
        }
        batch.begin();
        if(!paused) {
            bar.draw(batch, 1);
            bar2.draw(batch, 1);
            bar3.draw(batch, 1);
            bar4.draw(batch, 1);
        }
        if (cardPhase) {
            poweredDown.draw(batch, 1);
            cardPhaseRun();
            stage.act();
        }
        if (events.getFadeRobot() && !paused)
            events.fadeRobots(batch);
        if (events.hasLaserEvent() && !paused)
            for (LaserEvent laserEvent : events.getLaserEvents())
                laserEvent.drawLaserEvent(batch, game.getRobots());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Maybe this can help us run the game even on smaller screen than Tim Cooks.
        Vector2 size = Scaling.fit.apply(SettingsUtil.WINDOW_WIDTH, SettingsUtil.WINDOW_HEIGHT, width, height);
        int viewportX = (int) (width - size.x) / 2;
        int viewportY = (int) (height - size.y) / 2;
        int viewportWidth = (int) size.x;
        int viewportHeight = (int) size.y;
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
        stage.getViewport().update(width, height, true);
        //stage.setViewport(800, 480, true, viewportX, viewportY, viewportWidth, viewportHeight);
    }

    @Override
    public void pause() {
        Gdx.input.setInputProcessor(stage);
        batch.begin();
        menu.drawMenu(batch, stage);
        batch.end();
        if (menu.isChangeMap())
            changeMap();
        if (menu.isResume(game))
            resume();
    }

    @Override
    public void resume() {
        game.getGameOptions().enterMenu(false);
        paused = false;
        Gdx.input.setInputProcessor(this);
    }

    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ENTER && !events.hasWaitEvent()) {
            runCardPhase(game.getCards());
            return true;
        }
        if (!game.isRunning()) {
            debugControls.getAction(keycode).run();
        }

        /*if (game.isRunning()) {
            // Start new round if no round is currently activ1e
            if (game.currentRoundStep() == RoundStep.NULL_STEP) {
                game.startNewRound();
            }
            if (game.currentRoundStep() == RoundStep.PROGRAM_ROBOT) {
                programRobotControls.getAction(keycode).run();
            }
            // Decides what happens when we are running through phases
            if (game.currentRoundStep() == RoundStep.PHASES) {
                if (game.currentPhaseStep() == PhaseStep.REVEAL_CARDS) {
                    game.revealProgramCards();
                }
                // Handles logic when in "Check for winner step
                if (game.currentPhaseStep() == PhaseStep.CHECK_FOR_WINNER) {
                    game.checkIfSomeoneWon();
                }
            }
        }*/
        if (game.getGameOptions().getMenu())
            paused = true;
        return true;
    }

    public void changeMap() {
        this.mapID = menu.getMapId();
        if(game.getRobots()!=null)
            game.endGame();
        mapRenderer.setMap(AssetManagerUtil.getMap(mapID));
    }

    public void cardPhaseRun() {
        programCardsView.getDoneLabel().draw(batch, stage.getWidth() / 2);
        for (Group group : programCardsView.getGroups()) {
            group.draw(batch, 1);
        }
        if (programCardsView.done()) {
            Gdx.input.setInputProcessor(this);
            cardPhase = false;
            stage.clear();
            game.shuffleTheRobotsCards(programCardsView.getOrder());
            programCardsView.clearStuff();
            game.playNextCard();
            menu.reloadStage(stage);
            events.setPauseEvent(true);
        }
    }

    public void runCardPhase(ProgramCardsView programCardsView) {
        this.programCardsView = programCardsView;
        programCardsView.makeDoneLabel();
        stage.addActor(programCardsView.getDoneLabel());
        float i = stage.getWidth() - programCardsView.getGroups().size() * programCardsView.getCardWidth();
        programCardsView.getDoneLabel().setX(820);
        programCardsView.getDoneLabel().setY(70);
        i = i / 2 - programCardsView.getCardWidth();
        for (Group group : this.programCardsView.getGroups()) {
            group.setX(i += programCardsView.getCardWidth());
            System.out.println(i);
            stage.addActor(group);
        }
        cardPhase = true;
        Gdx.input.setInputProcessor(stage);
    }

}