package roborally.game.objects.robot;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Queue;
import roborally.game.objects.cards.IProgramCards;
import roborally.game.objects.cards.CardsInHand;
import roborally.utilities.enums.Direction;

import java.util.ArrayList;

public class RobotLogic implements Programmable {
    private String name;
    private GridPoint2 robotPosition;
    private GridPoint2 checkPoint;
    private int health = 10;
    private int reboots = 3;
    private Direction direction;
    private CardsInHand cardsInHand;
    private Queue<IProgramCards.Card> nextCard;

    public RobotLogic(String name) {
        this.name = name;
        this.robotPosition = new GridPoint2();
        this.direction = Direction.North;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public RobotLogic getModel() {
        return this;
    }

    //region Position
    @Override
    public GridPoint2 getPosition() {
        return this.robotPosition;
    }

    @Override
    public void setPosition(GridPoint2 newPosition) {
        this.robotPosition.set(newPosition);
    }
    //endregion

    //region Movement
    @Override
    public int[] move(int steps) {
        System.out.println(this.getName());
        int[] moveValues = getMoveValues();
        System.out.println("\nMoving forward...");
        if (steps == -1) {
            moveValues[0] = -moveValues[0];
            moveValues[1] = -moveValues[1];
        }
        return moveValues;
    }

    @Override
    public Direction rotate(Direction direction) {
        this.direction = direction;

        return this.direction;
    }

    public int[] getMoveValues() {
        int dy = 0;
        int dx = 0;
        Direction dir = this.direction;
        if (dir == Direction.North) {
            dy = 1;
        }
        if (dir == Direction.East) {
            dx = 1;
        }
        if (dir == Direction.West) {
            dx = -1;
        }
        if (dir == Direction.South) {
            dy = -1;
        }
        return new int[]{dx, dy};
    }
    //endregion

    //region Checkpoint
    @Override
    public void backToCheckPoint() {
        setPosition(this.checkPoint);
        this.direction = Direction.North;
    }

    public GridPoint2 getCheckPoint() {
        return this.checkPoint;
    }

    public void setCheckPoint(int x, int y) {
        this.checkPoint = new GridPoint2(x, y);
    }
    //endregion

    public int getHealth() {
        return health;
    }

    public int getReboots() {
        return reboots;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0)
            this.health = 0;
    }

    //region Direction
    public Direction getDirection() {
        return this.direction;
    }

    public int getDirectionID() {
        return this.direction.getDirectionID();
    }
    //endregion


    public String getStatus() {
        if (this.health < 5 && this.health > 0)
            return "Badly damaged";
        else if (this.health == 0) {
            this.health = -1;
            return "Destroyed";
        } else if (this.health > 5)
            return "Everything ok!";
        else
            return "Robot is gone";
    }

    //region Cards
    public void newCards(CardsInHand cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public void arrangeCards(int[] order) {
        this.cardsInHand.arrangeCards(order);
        Queue<IProgramCards.Card> nextCard = new Queue<>();
        for (IProgramCards.Card card : cardsInHand.getCards())
            nextCard.addFirst(card);
        this.nextCard = nextCard;
    }

    public IProgramCards.Card getNextCard() {
        assert nextCard != null;
        if (!nextCard.isEmpty()) {
            return nextCard.removeLast();
        }
        return null;
    }

    public ArrayList<IProgramCards.Card> getCards() {
        return this.cardsInHand.getCards();
    }
    //endregion
}