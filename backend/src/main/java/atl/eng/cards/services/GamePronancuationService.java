package atl.eng.cards.services;

import java.io.OutputStream;

import org.springframework.stereotype.Service;

import atl.eng.cards.dto.game.StartGamePronancuationResponse;
import atl.eng.cards.services.store.WordGamePronancuationStorage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GamePronancuationService {
    
    private final WordGamePronancuationStorage storage;
    private final WordService wordService;

    public StartGamePronancuationResponse startGamePronancuationFromAllWords(){ // TODO: remove and replace to categories
        return new StartGamePronancuationResponse(storage.generateNewSession(wordService.findAll()));
    }

    public boolean checkWord(Long sessionId, String word){
        if(storage.checkSession(sessionId, word)){
            return true;
        }
        return false;
    }

    public byte[] getAudio(Long sessionId){
        return storage.getAudio(sessionId);
    }
}
