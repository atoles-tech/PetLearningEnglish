package atl.eng.cards.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atl.eng.cards.dto.cards.WordResponse;
import atl.eng.cards.dto.translation.TranslationAnswer;
import atl.eng.cards.exceptions.cards.WordNotFoundException;
import atl.eng.cards.exceptions.cards.WordNotFoundInDictException;
import atl.eng.cards.mapper.WordMapper;
import atl.eng.cards.model.Word;
import atl.eng.cards.model.util.Level;
import atl.eng.cards.model.util.TypeTranslation;
import atl.eng.cards.repositories.WordRepository;
import atl.eng.cards.services.DictionaryService;
import atl.eng.cards.services.TranscriptionService;
import atl.eng.cards.services.TranslationService;
import atl.eng.cards.services.WordService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    private final DictionaryService dictService;
    private final TranslationService translationService;
    private final TranscriptionService transcriptionService;

    private final WordMapper wordMapper;

    private WordService wordService;

    @Lazy
    @Autowired
    public void setWordService(WordService wordService) {
        this.wordService = wordService;
    }

    @PostConstruct
    public void init(){
        List<Word> words = wordRepository.findWordsWhereDefinitionIsNull();
        dictService.addWordsToQueue(words.stream().map(w->w.getWord()).toList());

        //test
        List<Word> allWords = wordRepository.findAll();
        Word word = wordRepository.findById(16L).get();

        System.out.println(transcriptionService.findNearPronancuationWords(allWords, word, 0.66));
    }

    @Transactional
    public Word addWord(String word) {
        if (word.isEmpty()) {
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
                    TypeTranslation.CUSTOM);
        }

        if (wordRepository.existsByWord(word.trim())) {
            Word currentWord = wordRepository.findByWord(word.trim()).get();

            return new TranslationAnswer(
                    word,
                    wordRepository.findByWord(word.trim()).get().getTranslation(),
                    TypeTranslation.DICTIONARY,
                    currentWord.getDefinition(),
                    currentWord.getAudioUrl(),
                    currentWord.getLevel());
        }

        try {
            Word currentWord = dictService.getTranslation(word.trim());

            return new TranslationAnswer(
                    word,
                    wordRepository.save(currentWord).getTranslation(),
                    TypeTranslation.DICTIONARY,
                    currentWord.getDefinition(),
                    currentWord.getAudioUrl(),
                    currentWord.getLevel());
        } catch (Exception ignored) {
            return new TranslationAnswer(
                    word,
                    translationService.translate(word.trim()),
                    TypeTranslation.CUSTOM,
                    dictService.getTips(word));
        }

    }

    public Word findByWord(String word) {
        return wordRepository.findByWord(word)
                .orElseThrow(() -> new WordNotFoundException(word));
    }

    @Override
    public Page<WordResponse> findAll(Pageable pageable) {
        return wordMapper.toWordResponsePage(wordRepository.findAll(pageable));
    }

    @Override
    public Page<WordResponse> findAllByWord(String word, Pageable pageable) {
        return wordMapper.toWordResponsePage(wordRepository.findAllByWord(word.toLowerCase(), pageable));
    }

    @Override
    public WordResponse findById(Long id) {
        return wordMapper.toWordResponse(wordRepository.findById(id)
                .orElseThrow(() -> new WordNotFoundException(id)));
    }

    @Override
    @Transactional
    public void saveWord(Word word) {
        wordRepository.save(word);
    }

    @Scheduled(fixedRate = 300_000)
    @Transactional
    public void getDefinitions() {
        log.info("Started find definitions to words");
        while (dictService.available()) {
            String word = dictService.peek();
            String definition = dictService.getDefinition(word);
            Level level = dictService.getLevel(word);

            Word currentWord = findByWord(word);

            currentWord.setDefinition(definition);
            currentWord.setLevel(level);

            wordService.saveWord(currentWord);
            log.info("Saved definition and level for word: '{}', definition: '{}', level: '{}'", word, definition, level);
        }
        log.info("Finished find definitions");

    }

}
