package atl.eng.cards.services;

import java.util.List;

import atl.eng.cards.dto.UpdateCardDto;
import atl.eng.cards.dto.cards.CardResponse;

public interface CardService {
    public CardResponse addCard(String word, Long userId);
    public void updateCards(List<UpdateCardDto> cards);

    // get cards for studying 
    public List<CardResponse> learnCards(Integer limit, Long userId);
    public List<CardResponse> repeatCards(Integer limit, Long userId);
    
    public List<CardResponse> findAllCardsByUserId(Long userId);
    public List<CardResponse> getCards();

    // util
    public Boolean isOwnerCards(List<UpdateCardDto> cards, String userId);
}
