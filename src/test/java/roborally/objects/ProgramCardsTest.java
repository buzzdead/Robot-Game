package roborally.objects;

import org.junit.Before;
import org.junit.Test;
import roborally.objects.cards.IProgramCards;
import roborally.objects.cards.ProgramCards;

import static org.junit.Assert.*;

public class ProgramCardsTest {
    private IProgramCards programCards;
    private ProgramCards.Card card;
    private int numberOfCards;

    @Before
    public void setUp() throws Exception {
        programCards = new ProgramCards();
        card = new ProgramCards.Card(IProgramCards.CardTypes.MOVE_1);

        // Adds all the number of card types together
        for (IProgramCards.CardTypes cardType : IProgramCards.CardTypes.values()) {
            numberOfCards += cardType.getNumberOfCards();
        }
    }

    @Test
    public void verifyThatNumberOfCardsIsEqualToAmountOfCardsInDeck() {
        assertEquals(numberOfCards, programCards.getDeck().size());
    }

    @Test
    public void verifyThatANewCardIsGenerated() {
        ProgramCards.Card newCard = new ProgramCards.Card(IProgramCards.CardTypes.MOVE_1);
        assertNotNull(newCard);
    }

    @Test
    public void verifyThatCardTypeIsMove1() {
        assertEquals("MOVE_1", card.getCardType().name());
    }

    @Test
    public void verifyThatCardPriorityIsWithinMaxMinRange() {
        assertTrue(card.getPriorityRangeMin() <= card.getPriority() && card.getPriority() <= card.getPriorityRangeMax());
    }

    @Test
    public void verifyThatCardsGetShuffled() {
        IProgramCards notShuffled = new ProgramCards();
        programCards.shuffleCards();

        assertNotEquals(notShuffled, programCards);
    }
}