package roborally.ui.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import roborally.game.objects.cards.IProgramCards;
import roborally.utilities.AssetManagerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ProgramCardsView {
    private int cardPick;
    private ArrayList<Group> groups;
    private int[] order;
    private ArrayList<Label> topLabelList;
    private int cardWidth;
    private int cardHeight;
    private Label doneLabel;
    private float chosenX;
    private float xPosition;
    private HashMap<Integer, Float> xPositions;

    public ProgramCardsView() {
        this.topLabelList = new ArrayList<>();
        this.cardPick = 0;
        this.groups = new ArrayList<>();
        this.order = new int[]{-1, -1, -1, -1, -1};
        this.cardWidth = 75;
        this.cardHeight = 65;
        this.xPositions = new HashMap<>();
    }

    public void makeCard(IProgramCards.Card card) {
        if (card.getCardType() == IProgramCards.CardTypes.MOVE_1)
            this.makeMove1(card.getPriority());
        else if (card.getCardType() == IProgramCards.CardTypes.MOVE_2)
            this.makeMove2(card.getPriority());
        else if (card.getCardType() == IProgramCards.CardTypes.MOVE_3)
            this.makeMove3(card.getPriority());
        else if (card.getCardType() == IProgramCards.CardTypes.ROTATE_LEFT)
            this.makeRotateLeft(card.getPriority());
        else if (card.getCardType() == IProgramCards.CardTypes.ROTATE_RIGHT)
            this.makeRotateRight(card.getPriority());
        else if (card.getCardType() == IProgramCards.CardTypes.U_TURN)
            this.makeUTurn(card.getPriority());
        else if (card.getCardType() == IProgramCards.CardTypes.BACKUP)
            this.makeBackup(card.getPriority());
    }

    public void makeUTurn(int priority) {
        Image uTurn = new Image(AssetManagerUtil.getCardTexture("Uturn"));
        makeSomething(priority, uTurn);
    }

    public void makeBackup(int priority) {
        Image backup = new Image(AssetManagerUtil.getCardTexture("Backup"));
        makeSomething(priority, backup);
    }

    public void makeMove1(int priority) {
        Image move = new Image(AssetManagerUtil.getCardTexture("Move1"));
        makeSomething(priority, move);
    }

    public void makeMove2(int priority) {
        Image move = new Image(AssetManagerUtil.getCardTexture("Move2"));
        makeSomething(priority, move);
    }

    public void makeMove3(int priority) {
        Image move = new Image(AssetManagerUtil.getCardTexture("Move3"));
        makeSomething(priority, move);
    }

    public void makeRotateRight(int priority) {
        Image RotateR = new Image(AssetManagerUtil.getCardTexture("RotateRight"));
        makeSomething(priority, RotateR);
    }

    public void makeRotateLeft(int priority) {
        Image RotateL = new Image(AssetManagerUtil.getCardTexture("RotateLeft"));
        makeSomething(priority, RotateL);
    }

    /**
     * Makes a new Image of the card that is being played. Adds top
     * label for the order the card is played, and a label for the priority.
     *
     * @param priority The priority of this card.
     * @param image    The image created for the card, with the related texture.
     */
    private void makeSomething(int priority, Image image) {
        image.setSize(getCardWidth(), getCardHeight());
        Group group = new Group();
        group.addActor(image);
        Label selectedOrderLabel = makeSelectedOrderLabel();
        Label priorityLabel = makePriorityLabel(priority);
        group.addActor(priorityLabel);
        group.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                for (int i = 0; i < order.length; i++)
                    if (groups.indexOf(group) == order[i]) {
                        group.getChildren().get(1).setColor(Color.GREEN);
                        group.getChildren().get(0).setColor(Color.WHITE);
                        group.setY(0);
                        group.setX(xPositions.get(groups.indexOf(group)));
                        selectedOrderLabel.setColor(Color.GREEN);
                        selectedOrderLabel.setText("");
                        reArrange(i);
                        if(cardPick -1 != -1)
                            cardPick--;
                        return true;
                    }
                if(cardPick == 5) {
                    doneLabel.setColor(Color.RED);
                    return true;
                }
                topLabelList.add(cardPick, selectedOrderLabel);
                topLabelList.get(cardPick).setText(Integer.toString((cardPick)));
                group.addActor(topLabelList.get(cardPick));
                order[cardPick++] = groups.indexOf(group);
                group.getChildren().get(1).setColor(Color.GREEN);
                group.getChildren().get(0).setColor(Color.GREEN);
                group.setY(612);
                xPositions.put(groups.indexOf(group), group.getX());
                chosenX = 3 * getCardWidth() + getCardWidth() * cardPick+1;
                group.setX(chosenX);
                return true;
            }
        });
        this.groups.add(group);
    }

    public Label makeSelectedOrderLabel() {
        Label.LabelStyle topLabelStyle = new Label.LabelStyle();
        topLabelStyle.font = new BitmapFont();
        Label topLabel = new Label("", topLabelStyle);
        topLabel.setY(75);
        topLabel.setX(28);
        topLabel.setColor(Color.GREEN);
        topLabel.setFontScale(2f);
        return topLabel;
    }

    public Label makePriorityLabel(int priority) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        Label label = new Label(Integer.toString(priority), labelStyle);
        label.setX(15);
        label.setY(0);
        label.setFontScale(0.001f);
        label.setFontScaleX(2);
        label.setColor(Color.GREEN.add(Color.RED).add(Color.BLACK));
        return label;
    }

    // Used to sort cards when deselecting a card.
    public void reArrange(int oldI) {
        int i = oldI;
        order[i] = -1;
        topLabelList.remove(i);
        while (i < 4) {
            order[i] = order[++i];
            if (order[i] != -1) {
                topLabelList.get(i - 1).setText(Integer.toString(i - 1));
                order[i] = -1;
            }
        }
    }

    public ArrayList<Group> getGroups() {
        return this.groups;
    }

    public int[] getOrder() {
        return this.order;
    }

    public boolean done() {
        return this.cardPick == -1;
    }


    // Maybe deprecated.
    public void clearStuff() {
        this.order = new int[]{-1, -1, -1, -1, -1};
        this.cardPick = 0;
        this.groups.clear();
        this.groups = new ArrayList<>();
    }

    public int getCardWidth() {
        return this.cardWidth;
    }

    public int getCardHeight() {
        return this.cardHeight;
    }

    public Label getDoneLabel() {
        return this.doneLabel;
    }

    public void makeDoneLabel() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        doneLabel = new Label("Done", labelStyle);
        doneLabel.setPosition(getCardWidth(), getCardHeight());
        doneLabel.setFontScale(2);
        doneLabel.setScale(2);
        doneLabel.setHeight(this.cardHeight);
        doneLabel.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                doneLabel.setColor(Color.GREEN);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                doneLabel.setColor(Color.WHITE);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                int[] newOrder = new int[cardPick];
                System.arraycopy(order, 0, newOrder, 0, cardPick);
                order = newOrder;
                cardPick = -1;
            }
        });
    }

}