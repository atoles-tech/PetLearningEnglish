package atl.eng.cards.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atl.eng.cards.dto.translation.TranslationAnswer;
import atl.eng.cards.exceptions.cards.WordNotFoundException;
import atl.eng.cards.exceptions.cards.WordNotFoundInDictException;
import atl.eng.cards.model.Word;
import atl.eng.cards.model.util.TypeTranslation;
import atl.eng.cards.repositories.WordRepository;
import atl.eng.cards.services.DictionaryService;
import atl.eng.cards.services.TranslationService;
import atl.eng.cards.services.WordService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WordServiceImpl implements WordService{

    private final WordRepository wordRepository;

    private final DictionaryService dictService;
    private final TranslationService translationService;

    @Transactional
    public Word addWord(String word) {
        if(word.isEmpty()){
            throw new WordNotFoundInDictException(word);
        }


        
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
            return new TranslationAnswer(
                word, 
                translationService.translate(word.trim()), 
                TypeTranslation.CUSTOM
            );
        }

        if (wordRepository.existsByWord(word.trim())) {
            Word currentWord = wordRepository.findByWord(word.trim()).get();

            return new TranslationAnswer(
                word, 
                wordRepository.findByWord(word.trim()).get().getTranslation(), 
                TypeTranslation.DICTIONARY,
                currentWord.getDefinition()
            );
        }

        try {
            Word currentWord = dictService.getTranslation(word.trim());

            return new TranslationAnswer(
                word,
                wordRepository.save(currentWord).getTranslation(),
                TypeTranslation.DICTIONARY,
                currentWord.getDefinition()
            );
        } catch (Exception ignored) {
            return new TranslationAnswer(
                word, 
                translationService.translate(word.trim()),
                TypeTranslation.CUSTOM,
                dictService.getTips(word)
            );
        }

    }

    public Word findByWord(String word) {
        return wordRepository.findByWord(word)
            .orElseThrow(()->new WordNotFoundException(word));
    }

}
