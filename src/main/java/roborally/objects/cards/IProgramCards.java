package roborally.objects.cards;

import java.util.ArrayList;

public interface IProgramCards {
    /**
     * Shuffles the initial deck of program cards
     */
    void shuffleCards();

    /**
     * @return Deck of program cards
     */
    ArrayList<Card> getDeck();

    enum CardTypes {
        MOVE_1(18),
        MOVE_2(12),
        MOVE_3(6),
        BACKUP(6),
        ROTATE_RIGHT(18),
        ROTATE_LEFT(18),
        U_TURN(6);


        private final int nCards;

        CardTypes(int nCards) {
            this.nCards = nCards;
        }

        public int getNumberOfCards() {
            return nCards;
        }
    }

    class Card {
        private CardTypes cardType;
        private int priority;
        private int priorityRangeMin;
        private int priorityRangeMax;

        public Card(CardTypes cardType) {
            this.cardType = cardType;
            this.priorityRangeMin = 1;
            this.priorityRangeMax = 500;
            this.priority = priorityRangeMin + (int) (Math.random() * priorityRangeMax);
        }

        // TODO: @adipid. Er det en plan å bruke dette eller er dette deprecate? @thomas
        public String getCard() {
            return this.cardType + " " + this.priority;
        }

        public int getValue() {
            if (cardType.toString().contains("MOVE"))
                return Integer.parseInt(getCard().substring(5, 6));
            else if (cardType.toString().contains("U_TURN"))
                return -180;
            else if (cardType.toString().contains("ROTATE"))
                return cardType.toString().contains("RIGHT") ? 90 : -90;
            return 0;
        }

        public CardTypes getCardType() {
            return this.cardType;
        }

        public int getPriority() {
            return this.priority;
        }

        public int getPriorityRangeMin() {
            return this.priorityRangeMin;
        }

        public int getPriorityRangeMax() {
            return this.priorityRangeMax;
        }
    }

}
