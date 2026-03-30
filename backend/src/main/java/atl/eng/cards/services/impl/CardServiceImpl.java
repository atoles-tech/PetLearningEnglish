package atl.eng.cards.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atl.eng.cards.dto.UpdateCardDto;
import atl.eng.cards.dto.cards.CardResponse;
import atl.eng.cards.mapper.CardMapper;
import atl.eng.cards.model.Card;
import atl.eng.cards.model.Credential;
import atl.eng.cards.model.Word;
import atl.eng.cards.repositories.CardRepository;
import atl.eng.cards.services.CardService;
import atl.eng.cards.services.CredentialService;
import atl.eng.cards.services.WordService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService{
    
    private final CardRepository cardRepository;
    
    private final WordService wordService;
    private final CredentialService credentialService;

    private final CardMapper cardMapper;

    @Transactional
    public CardResponse addCard(String word, Long userId){
        Word savedWord = wordService.addWord(word);
        
        Credential credential = credentialService.findEntityById(userId);

        if(cardRepository.existsByWordAndUserId(savedWord, userId)){
            return cardMapper.toCardResponse(cardRepository.findByWordAndCredential(savedWord, credential).get());
        }

        Card card = new Card(null, credential, savedWord, 0, LocalDateTime.now());
        return cardMapper.toCardResponse(cardRepository.save(card));
    }

    @Transactional
    public void updateCards(List<UpdateCardDto> cards){
        List<Card> repCards = cardRepository.findAllById(cards.stream().map(UpdateCardDto::getId).toList());
        
        for(Card card: repCards){
            Integer knowledge = cards.stream().filter(c -> card.getId().equals(c.getId()))
                .findFirst().get().getKnowledge();
            
            card.setKnowledge(knowledge>10?10:knowledge<0?0:knowledge);
            card.setLastUsed(LocalDateTime.now());
        }

        cardRepository.saveAll(repCards);
    }

    public List<CardResponse> learnCards(Integer limit, Long userId){
        return cardMapper.toCardResponseList(cardRepository.findCardsForLearn(limit, userId));
    }

    public List<CardResponse> repeatCards(Integer limit, Long userId){
        return cardMapper.toCardResponseList(cardRepository.findCardsForRepeat(limit, userId));
    }

    public List<CardResponse> findAllCardsByUserId(Long userId){
        return cardMapper.toCardResponseList(cardRepository.findCardsByUserId(userId));
    }

    public List<CardResponse> getCards(){
        return cardMapper.toCardResponseList(cardRepository.findAll());
    }

    public Boolean isOwnerCards(List<UpdateCardDto> cards, String userId){
        return cardRepository.findAllById(cards.stream().map(x->x.getId()).toList()).stream()
            .filter(x->!x.getCredential().getId().equals(Long.valueOf(userId))).count() == 0;
    }
}
