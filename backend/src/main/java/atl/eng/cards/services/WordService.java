package atl.eng.cards.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import atl.eng.cards.dto.cards.WordResponse;
import atl.eng.cards.dto.translation.TranslationAnswer;
import atl.eng.cards.model.Word;

public interface WordService {
    public WordResponse findById(Long id);

    public Page<WordResponse> findAll(Pageable pageable);
    public Page<WordResponse> findAllByWord(String word, Pageable pageable);
    
    public Word addWord(String word);
    public void deleteByWord(String word);
    
    public TranslationAnswer getTranslation(String word);
    public void saveWord(Word word);

    //ENTITY
    public Word findByWord(String word);
    public List<Word> findAll();
}
