package atl.eng.cards.services;

import atl.eng.cards.dto.translation.TranslationAnswer;
import atl.eng.cards.model.Word;

public interface WordService {
    public Word addWord(String word);
    public TranslationAnswer getTranslation(String word);
    public Word findByWord(String word);
    public void deleteByWord(String word);
}
