package atl.eng.cards.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atl.eng.cards.dto.TranslationAnswer;
import atl.eng.cards.exceptions.cards.WordNotFoundInDictException;
import atl.eng.cards.model.Word;
import atl.eng.cards.repositories.WordRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class WordService{

    private WordRepository wordRepository;
    private DictionaryService dictService;
    private TranslationService translationService;

    @Transactional
    public Word addWord(String word) {
        if (wordRepository.existsByWord(word)) {
            return wordRepository.findByWord(word).get();
        }

            try {
                return wordRepository.save(dictService.getTranslation(word));
            } catch (Exception ignored) {
                throw new WordNotFoundInDictException(word);
            }
        }

    @Transactional
    public void deleteByWord(String word) {
        wordRepository.deleteByWord(word);
    }

    @Transactional
    public TranslationAnswer getTranslation(String word) { // if we want get any translation
        if (word.trim().contains(" ")) {
            return new TranslationAnswer(word, translationService.translate(word.trim()));
        }

        if (wordRepository.existsByWord(word.trim())) {
            return new TranslationAnswer(word, wordRepository.findByWord(word.trim()).get().getTranslation());
        }

        try {
            return new TranslationAnswer(word,
                    wordRepository.save(dictService.getTranslation(word.trim())).getTranslation());
        } catch (Exception ignored) {
            return new TranslationAnswer(word, translationService.translate(word.trim()));
        }

    }

    public Word findByWord(String word) {
        return wordRepository.findByWord(word).get(); // TODO:!!!!!!!!! EXCEPTION
    }

}
