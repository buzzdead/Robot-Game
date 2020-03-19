package roborally.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import roborally.game.objects.cards.CardsInHand;
import roborally.game.objects.cards.IProgramCards;
import roborally.game.objects.cards.ProgramCards;
import roborally.game.objects.gameboard.GameBoard;
import roborally.game.objects.gameboard.IFlag;
import roborally.game.objects.gameboard.IGameBoard;
import roborally.game.objects.robot.AIPlayer;
import roborally.game.objects.robot.Robot;
import roborally.ui.ILayers;
import roborally.ui.gdx.ProgramCardsView;
import roborally.ui.gdx.events.Events;
import roborally.utilities.AssetManagerUtil;
import roborally.utilities.SettingsUtil;
import roborally.utilities.enums.Direction;
import roborally.utilities.enums.PhaseStep;
import roborally.utilities.enums.RoundStep;

import java.util.ArrayList;
import java.util.Random;

public class Game implements IGame {
    private final boolean DEBUG = true;

    //region Game Objects
    private IGameBoard gameBoard;
    private ILayers layers;
    private AIPlayer[] aiRobots;
    private ArrayList<Robot> robots;
    private ArrayList<IFlag> flags;
    private IProgramCards deckOfProgramCards;
    //endregion

    private Robot winner;

    private boolean gameRunning = false;
    private RoundStep roundStep = RoundStep.NULL_STEP;
    private PhaseStep phaseStep = PhaseStep.NULL_PHASE;
    private int currentRobotID;
    private Events events;
    private GameOptions gameOptions;
    private AIPlayer AIPlayer;

    private boolean fun;

    public Game(Events events) {
        currentRobotID = 0;
        gameBoard = new GameBoard();
        layers = gameBoard.getLayers();
        flags = gameBoard.findAllFlags();
        deckOfProgramCards = new ProgramCards();
        this.events = events;
        this.gameOptions = new GameOptions();
    }

    public Game(boolean runAIGame) {
        assert (runAIGame);
        gameBoard = new GameBoard();
        layers = gameBoard.getLayers();
    }

    @Override
    public void startUp() {
        this.robots = gameOptions.makeRobots(layers);
        //this.AIPlayer = new AIPlayer(robots.get(1), this.gameBoard);
    }

    @Override
    public void funMode() {
        robots = gameOptions.funMode(layers, flags);
        this.events.setGameSpeed("fastest");
        fun = true;
    }

    @Override
    public void checkForDestroyedRobots() {
        for (Robot robot : robots) {
            if (robot.getLogic().getStatus().equals("Destroyed")) {
                removeFromUI(robot, true);
            }
        }
    }

    private void removeFromUI(Robot robot, boolean fade) {
        events.fadeRobot(robot.getPosition(), robot.getTexture());
        layers.setRobotCell(robot.getPosition().x, robot.getPosition().y, null);
        robot.setPosition(SettingsUtil.GRAVEYARD);
        robot.clearRegister();
        this.events.setFadeRobot(fade);
    }

    @Override
    public ILayers getLayers() {
        return this.layers;
    }

    @Override
    public AIPlayer[] getAIRobots() {
        return aiRobots;
    }

    //region Robots
    @Override
    public Robot getFirstRobot() {

        if (this.currentRobotID == robots.size()) {
            this.currentRobotID = 0;
        }
        checkForDestroyedRobots();
        return robots.get(0);
    }

    @Override
    public ArrayList<Robot> getRobots() {
        return this.robots;
    }

    private void setRobots(ArrayList<Robot> newRobots) {
        this.robots = newRobots;
    }
    //endregion

    @Override
    public void startGame() {
        assert (!gameRunning);
        if (DEBUG) {
            System.out.println("\nGame started...");
        }
        gameRunning = true;
        startNewRound();
    }

    @Override
    public void restartGame() {
        System.out.println("Restarting game...");
        for (Robot robot : robots) {
            removeFromUI(robot, false);
        }
        setRobots(AssetManagerUtil.makeRobots());
    }

    //region Rounds
    @Override
    public void startNewRound() {
        assert (gameRunning);
        assert (roundStep == RoundStep.NULL_STEP);
        assert (phaseStep == PhaseStep.NULL_PHASE);

        roundStep = RoundStep.ANNOUNCE_POWERDOWN;

        if (DEBUG) {
            System.out.println("\nRound started...");
            System.out.println("Entering " + roundStep + "...");
            System.out.println("Waiting for input..");
        }
    }

    @Override
    public RoundStep currentRoundStep() {
        return roundStep;
    }
    //endregion

    @Override
    public boolean isRunning() {
        return gameRunning;
    }

    @Override
    public GameOptions getGameOptions() {
        return this.gameOptions;
    }

    @Override
    public PhaseStep currentPhaseStep() {
        return phaseStep;
    }

    // It reset game states at the end of a round
    private void cleanUp() {
        assert (gameRunning);
        roundStep = RoundStep.NULL_STEP;
        phaseStep = PhaseStep.NULL_PHASE;
    }

    @Override
    public void fireLaser() {
        Sound sound = AssetManagerUtil.manager.get(AssetManagerUtil.SHOOT_LASER);
        sound.play((float) 0.15 * AssetManagerUtil.volume);
        robots.get(0).fireLaser();
        ArrayList<GridPoint2> coords = robots.get(0).getLaser().getCoords();
        if (!coords.isEmpty())
            events.createNewLaserEvent(robots.get(0).getPosition(), coords.get(coords.size() - 1));
    }


    private void removeDeadRobots() {
        ArrayList<Robot> aliveRobots = new ArrayList<>();
        for (Robot robot : getRobots()) {
            if (isNotInGraveyard(robot))
                aliveRobots.add(robot);
        }
        setRobots(aliveRobots);

        returnToMenuIfOnlyOneRobotLeft();
    }

    private void returnToMenuIfOnlyOneRobotLeft() {
        if (getRobots().size() < 2) {
            System.out.println("Entering menu");
            gameOptions.enterMenu();
        }
    }

    //region Cards
    @Override
    public ProgramCardsView getCards() {
        //TODO Refactor for readability
        checkForDestroyedRobots();
        if (fun)
            removeDeadRobots();

        ArrayList<IProgramCards.Card> cardsDrawn;
        CardsInHand cardsInHand;

        int numberOfCardsDrawnFromDeck = 0;
        int sizeOfDeck = deckOfProgramCards.getDeck().size();
        for (int robotID = 0; robotID < robots.size(); robotID++) {
            cardsDrawn = new ArrayList<>();

            //TODO FIX. Bugs with drawing cards...
            int robotHealth = robots.get(robotID).getLogic().getHealth() - 1;
            int cardsToDraw = Math.max(0, robotHealth);

            for (int j = 0; j < cardsToDraw; j++) {
                if (numberOfCardsDrawnFromDeck == sizeOfDeck) {
                    deckOfProgramCards.shuffleCards();
                    numberOfCardsDrawnFromDeck = 0;
                }
                cardsDrawn.add(deckOfProgramCards.getDeck().get(numberOfCardsDrawnFromDeck++));
            }
            cardsInHand = new CardsInHand(cardsDrawn);
            robots.get(robotID).getLogic().newCards(cardsInHand);


            // This codesnippet lets all robots except the first one play their cards in default order.

            if (robotID > 0) { // > 1 for testing ArtificialPlr.
                int[] newOrder = new int[cardsToDraw];

                for (int i = 0; i < Math.min(cardsToDraw, 5); i++) {
                    newOrder[i] = i;
                }
                robots.get(robotID).getLogic().arrangeCards(newOrder);
            }
        }
        //artificialPlr.printAllCardsAndFlags();
        //robots.get(1).getModel().arrangeCards(artificialPlr.getOrder());
        ProgramCardsView programCardsView = new ProgramCardsView();
        for (IProgramCards.Card card : robots.get(0).getLogic().getCards()) {
            programCardsView.makeCard(card);
        }
        return programCardsView;
    }

    @Override
    public void playNextCard() {
        Robot robot = getRobots().get(currentRobotID);
        if (isNotInGraveyard(robot)) {
            getRobots().get(currentRobotID).playNextCard();
        }
        incrementCurrentRobotID();
        checkForDestroyedRobots();
    }
    //endregion

    private void incrementCurrentRobotID() {
        currentRobotID++;
        if (currentRobotID == getRobots().size())
            currentRobotID = 0;
    }

    private boolean isNotInGraveyard(Robot robot) {
        return !robot.getPosition().equals(SettingsUtil.GRAVEYARD);
    }

    @Override
    public void shuffleTheRobotsCards(int[] order) {
        robots.get(0).getLogic().arrangeCards(order);
    }

    //region Conveyor belts
    @Override
    public void moveAllConveyorBelts() {
        // TODO: Implement the corresponding phase.
    }

    @Override
    public void moveExpressConveyorBelts() {
        // TODO: Implement the corresponding phase.
    }
    //endregion

    @Override
    public void moveCogs() {
        // TODO: Implement the corresponding phase.
    }

    @Override
    public void fireLasers() {
        Sound sound = AssetManagerUtil.manager.get(AssetManagerUtil.SHOOT_LASER);
        sound.play((float) 0.15 * AssetManagerUtil.volume);
        for (Robot robot : robots) {
            robot.fireLaser();
            ArrayList<GridPoint2> coords = robot.getLaser().getCoords();
            if (!coords.isEmpty())
                events.createNewLaserEvent(robot.getPosition(), coords.get(coords.size() - 1));
        }
        // TODO: Implement the corresponding phase.
    }

    @Override
    public void allowMovingBackupPoints() {
        // TODO: Implement the corresponding phase.
    }

    @Override
    public void registerFlagPositions() {
        System.out.println("\nChecking if any robots have currently arrived at their next flag position...");
        for (IFlag flag : flags) {
            int flagX = flag.getPosition().x;
            int flagY = flag.getPosition().y;
            for (Robot robot : robots) {
                int robotX = robot.getPosition().x;
                int robotY = robot.getPosition().y;
                if (flagX == robotX && flagY == robotY) {
                    int nextFlag = robot.getNextFlag();
                    if (flag.getID() == nextFlag) {
                        robot.visitNextFlag();
                        System.out.println("A flag has been visited");
                    }
                }
            }
        }
    }

    @Override
    public boolean checkIfSomeoneWon() {
        assert (gameRunning);
        assert (roundStep == RoundStep.PHASES);
        assert (phaseStep == PhaseStep.CHECK_FOR_WINNER);
        if (DEBUG) System.out.println("\nChecking if someone won...");

        boolean someoneWon = checkAllRobotsForWinner();
        if (someoneWon) {
            endGame();
        }

        if (DEBUG) System.out.println("Found winner: " + someoneWon);
        return someoneWon;
    }

    private boolean checkAllRobotsForWinner() {
        assert (gameRunning);
        assert (roundStep == RoundStep.PHASES);
        assert (phaseStep == PhaseStep.CHECK_FOR_WINNER);
        checkAllRobotsAreCreated();

        for (Robot robot : robots) {
            if (robot.hasVisitedAllFlags()) {
                winner = robot;
            }
        }

        return (winner != null);
    }

    private boolean checkAllRobotsAreCreated() {
        boolean robotsAreCreated = true;
        if (robots == null) {
            robotsAreCreated = false;
        } else {
            for (Robot robot : robots) {
                if (robot == null) {
                    robotsAreCreated = false;
                    break;
                }
            }
        }
        if (!robotsAreCreated) {
            throw new IllegalStateException("Robots are not created");
        }
        return true;
    }

    @Override
    public Robot getWinner() {
        return winner;
    }

    @Override
    public void endGame() {
        assert (gameRunning);
        //if(DEBUG){
        //System.out.println("Stopping game...");
        //}
        cleanUp();
        gameRunning = false;
    }

    @Override
    public void exitGame() {
        Gdx.app.exit();
    }

    @Override
    public void moveRobots() {
        Random r = new Random();
        int m;
        for (Robot robot : robots) {
            m = r.nextInt(4);
            if (m == 0)
                robot.rotate(Direction.turnLeftFrom(robot.getLogic().getDirection()));
            else if (m == 1)
                robot.move(1);
            else if (m == 2)
                robot.move(-1);
            else if (m == 3)
                robot.rotate(Direction.turnRightFrom(robot.getLogic().getDirection()));
        }
    }

    @Override
    public void revealProgramCards() {
        // TODO: Implement simple method to make some use of our ProgramCards class.
    }

    @Override
    public void programRobots() {
        // TODO: Implement some simple method to make some use of ProgramCards.
    }

    @Override
    public void dealCards() {
        // TODO: Implement some simple method to make some use of ProgramCards.
    }

    @Override
    public void announcePowerDown() {
        // TODO: Implement some damage system.
    }
}
