package roborally.game;

import roborally.objects.cards.CardsInHand;
import roborally.objects.cards.IProgramCards;
import roborally.objects.cards.ProgramCards;
import roborally.objects.gameboard.Flag;
import roborally.objects.robot.Robot;
import roborally.ui.Layers;
import roborally.ui.gdx.ProgramCardsView;
import roborally.ui.gdx.events.Events;
import roborally.utilities.SettingsUtil;

import java.util.ArrayList;

public class Round {
    private boolean fun;
    private ArrayList<Robot> robots;
    private Events events;
    private IProgramCards deckOfProgramCards;
    private Game game;
    private int currentRobotID;
    private ArrayList<Flag> flags;
    private Phase phase;

    public Round(ArrayList<Robot> robots, Events events, Game game, ArrayList<Flag> flags, Layers layers) {
        this.robots = robots;
        this.events = events;
        this.fun = false;
        this.game = game;
        this.flags = flags;
        this.currentRobotID = 0;
        this.phase = new Phase(layers, events, robots);
        deckOfProgramCards = new ProgramCards();
    }

    public void checkForDestroyedRobots() {
        for (Robot robot : robots) {
            if (("Destroyed").equals(robot.getLogic().getStatus())) {
                System.out.println("yeah1");
                removeFromUI(robot, true);
            }
        }
    }

    private void removeFromUI(Robot robot, boolean fade) {
        events.fadeRobot(robot.getPosition(), robot.getTexture(), robot.getCurrentTexture());
        robot.deleteRobot();
        System.out.println("yeah2");
        this.events.setFadeRobot(fade);
    }

    public ProgramCardsView getCards() {
        checkForDestroyedRobots();
        if (fun)
            removeDeadRobots();

        ArrayList<IProgramCards.Card> cardsDrawn;
        CardsInHand cardsInHand;

        int numberOfCardsDrawnFromDeck = 0;
        int sizeOfDeck = deckOfProgramCards.getDeck().size();
        for (int robotID = 0; robotID < robots.size(); robotID++) {
            cardsDrawn = new ArrayList<>();
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

            if (robotID > 0) {
                int[] newOrder = new int[cardsToDraw];

                for (int i = 0; i < Math.min(cardsToDraw, 5); i++) {
                    newOrder[i] = i;
                }
                robots.get(robotID).getLogic().arrangeCards(newOrder);
            }
        }
        ProgramCardsView programCardsView = new ProgramCardsView();
        for (IProgramCards.Card card : robots.get(0).getLogic().getCards()) {
            programCardsView.makeCard(card);
        }
        return programCardsView;
    }

    public void setFun() {
        this.fun = true;
    }

    private void removeDeadRobots() {
        ArrayList<Robot> aliveRobots = new ArrayList<>();
        for (Robot robot : robots) {
            if (isNotInGraveyard(robot))
                aliveRobots.add(robot);
        }
        robots = aliveRobots;
        game.setRobots(robots);
        returnToMenuIfOnlyOneRobotLeft();
    }

    private boolean isNotInGraveyard(Robot robot) {
        return !robot.getPosition().equals(SettingsUtil.GRAVEYARD);
    }

    private void returnToMenuIfOnlyOneRobotLeft() {
        if (robots.size() < 2) {
            System.out.println("Entering menu");
            game.getGameOptions().enterMenu();
        }
    }

    public void fireLasers() {
        phase.fireLasers();
    }

    public void playNextCard() {
        Robot robot = robots.get(currentRobotID);
        if (isNotInGraveyard(robot)) {
            robots.get(currentRobotID).playNextCard();
        }
        incrementCurrentRobotID();
        checkForDestroyedRobots();
    }

    private void incrementCurrentRobotID() {
        currentRobotID++;
        if (currentRobotID == robots.size()) {
            phase.runPhase(flags);
            currentRobotID = 0;
        }
    }
}
