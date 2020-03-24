package roborally.game.objects.robot;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Queue;
import roborally.game.objects.cards.IProgramCards;
import roborally.game.objects.cards.ProgramCards;
import roborally.game.objects.gameboard.Flag;
import roborally.game.objects.gameboard.GameBoard;
import roborally.utilities.enums.Direction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class AIPlayer {
    private Programmable robot;
    private GameBoard gameBoard;
    private Queue<Flag> flags;
    private ArrayList<ArrayList<IProgramCards.Card>> valueCards; // 0 = move cards, 1 = left, 2 = right
    private ArrayList<Integer> moveCardValues;
    private int[] order;
    private int cardPick;
    private HashMap<String, Integer> cardValues;
    private GridPoint2 hypoPos;
    private boolean ok;

    public AIPlayer(Programmable robot, GameBoard gameBoard) {
        this.robot = robot;
        this.gameBoard = gameBoard;
        this.flags = new Queue<>();
        this.order = new int[5]; // Making compatible for less cards at a later point.
        this.cardPick = 0;
        this.hypoPos = robot.getPosition();
    }

    public void printAllCardsAndFlags() {
        printAllCards();
        printAllFlags();
        takeAllTheMoveCards();
    }

    private ArrayList<ProgramCards.Card> printAllCards() {
        int totalMoves = 0;
        int totalRight = 0;
        int totalLeft = 0;
        this.cardValues = new HashMap<>();
        this.moveCardValues = new ArrayList<>();
        ArrayList<ProgramCards.Card> temp = robot.getLogic().getCards();
        temp.sort(Comparator.comparing(IProgramCards.Card::getCardType));
        for (ProgramCards.Card card : temp) {
            System.out.print(card.getCard() + " ");
            if(card.getValue()==1 || card.getValue() == 2 || card.getValue() == 3) {
                totalMoves += card.getValue();
                moveCardValues.add(card.getValue());
            }
            else if(card.getValue() == 90)
                totalRight += card.getValue();
            else if(card.getValue() < 0)
                totalLeft += card.getValue();
            else
                totalMoves += card.getValue();
        }
        cardValues.put("moves", totalMoves);
        cardValues.put("right", totalRight);
        cardValues.put("left", totalLeft);
        return temp;
    }

    private void printAllFlags() {
        ArrayList<Flag> temp = gameBoard.findAllFlags();
        temp.sort(Comparator.comparing(Flag::getID));
        for (Flag flag : temp)
            flags.addLast(flag);
    }

    private void takeAllTheMoveCards() {
        int total = this.cardValues.get("moves");
        valueCards = new ArrayList<>();
        valueCards.add(new ArrayList<>());
        ArrayList<IProgramCards.Card> temp = printAllCards();
        ProgramCards.Card card;
        while(total!=0) {
            card = temp.iterator().next();
            total -= card.getValue();
            valueCards.get(valueCards.size()-1).add(card);
        }
        if(!ok)
            findFastestWayToPos(findDirectionToNextFlag());
    }

    private void takeAllTheRotateLeftCards() {
        ArrayList<ProgramCards.Card> temp = printAllCards();
        int total = 0;
        if(cardValues.get("Left")!=null)
            total = cardValues.get("Left");
        valueCards.add(new ArrayList<>());
        ProgramCards.Card card;
        while(total!=0) {
            card = temp.iterator().next();
            if (card.getCardType() == ProgramCards.CardTypes.ROTATE_LEFT || card.getCardType() == ProgramCards.CardTypes.U_TURN)
                valueCards.get(valueCards.size()-1).add(card);
            total-=card.getValue();

        }
    }

    private void takeAlltheRotateRightCards() {
        ArrayList<ProgramCards.Card> temp = printAllCards();
        int total = 0;
        if(cardValues.get("Right")!=null)
            total = cardValues.get("Right");
        valueCards.add(new ArrayList<>());
        ProgramCards.Card card;
        while(total!=0) {
            card = temp.iterator().next();
            if (card.getCardType() == ProgramCards.CardTypes.ROTATE_RIGHT)
                valueCards.get(valueCards.size()-1).add(card);
            total-=card.getValue();
        }
    }

    private GridPoint2 findDirectionToNextFlag() {
        if(flags.isEmpty()) {
            ok = true;
            return new GridPoint2(0,0);
        }
        Flag flag = flags.removeFirst();
        GridPoint2 fPos = flag.getPosition();
        GridPoint2 rPos = hypoPos;
        GridPoint2 movableValue = new GridPoint2();
        if(Math.abs(fPos.x - rPos.x) - Math.abs(fPos.y - rPos.y)
                < flag.getPosition().dst(hypoPos))
            return rPos.x > fPos.x ? movableValue.set(-1,0) : movableValue.set(1,0);
        else
            return (Math.abs(rPos.x) - fPos.x) > Math.abs(rPos.y - fPos.y) ? rPos.x > fPos.x
            ? movableValue.set(-1,0) : movableValue.set(1,0) : rPos.y > fPos.y
                    ? movableValue.set(0,-1) : movableValue.set(0,1);
    }

    private void findFastestWayToPos(GridPoint2 pos) {
        boolean foundPos = false;
        GridPoint2 movableValue = robot.getLogic().getMoveValues();
        if (movableValue.equals(pos) && (moveCardValues.contains(Math.max(pos.x, pos.y)))) {
            hypoPos.add(movableValue);
            ProgramCards.Card card;
            for (int i = 0; i < robot.getLogic().getCards().size(); i++) {
                card = robot.getLogic().getCards().get(i);
                if (!card.getCardType().toString().contains("MOVE"))
                    continue;
                boolean nope = false;
                if (Integer.parseInt(robot.getLogic().getCards().get(i).getCard().substring(5, 6)) == 1) {
                    for (int k = 0; k <= cardPick; k++)
                        if (order[k] == i) {
                            nope = true;
                            break;
                        }
                    if (!nope) {
                        order[cardPick++] = i;
                        foundPos = true;
                    }
                }
            }
        }
        if (!foundPos) {
            insertMethodNameHere();
        }
    }

    private void insertMethodNameHere() {

        takeAllTheRotateLeftCards();
        takeAlltheRotateRightCards();
        ProgramCards.Card card;
        boolean nope = false;
        for (int i = 0; i < robot.getLogic().getCards().size(); i++) {
            card = robot.getLogic().getCards().get(i);
            if (card.getCardType() == ProgramCards.CardTypes.ROTATE_LEFT || card.getCardType() == ProgramCards.CardTypes.ROTATE_RIGHT) {
                for (int k = 0; k <= cardPick; k++) {
                    if (order[k] == i) {
                        nope = true;
                        break;
                    }
                }
                if (!nope) {
                    order[cardPick++] = i;
                    if (card.getCardType() == ProgramCards.CardTypes.ROTATE_LEFT)
                        robot.getLogic().rotate(Direction.turnLeftFrom(robot.getLogic().getDirection()));
                    else
                        robot.getLogic().rotate(Direction.turnRightFrom(robot.getLogic().getDirection()));
                    break;
                }
            }
        }
        if (cardPick != 5 && !ok)
            findFastestWayToPos(findDirectionToNextFlag());
    }

    public int[] getOrder() {
        return this.order;
    }

    public int runRobotCore() {
        // Calculates a good move.
        return 123456789;
    }
}