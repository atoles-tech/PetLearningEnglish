package atl.eng.cards.services;

import java.util.List;

import atl.eng.cards.dto.UpdateCardDto;
import atl.eng.cards.dto.cards.CardResponse;
import atl.eng.cards.model.Card;

public interface CardService {
    public CardResponse addCard(String word, Long userId);
    public void updateCards(List<UpdateCardDto> cards);

    // get cards for studying 
    public List<Card> learnCards(Integer limit, Long userId);
    public List<Card> repeatCards(Integer limit, Long userId);
    
    public List<Card> findAllCardsByUserId(Long userId);
    public List<Card> getCards();

    // util
    public Boolean isOwnerCards(List<UpdateCardDto> cards, String userId);
}
