package atl.eng.cards.dto.translation;

import java.util.List;

import atl.eng.cards.model.util.Level;
import atl.eng.cards.model.util.TypeTranslation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TranslationAnswer {
    private String word;
    private String translation;
    private TypeTranslation typeTranslation;
    private String definition;
    private String audioUrl;
    private List<Tip> tips;
    private Level level;

    public TranslationAnswer(String word, String translation, TypeTranslation typeTranslation) {
        this.word = word;
        this.translation = translation;
        this.typeTranslation = typeTranslation;
    }
    
    public TranslationAnswer(String word, String translation, TypeTranslation typeTranslation, List<Tip> tips) {
        this.word = word;
        this.translation = translation;
        this.typeTranslation = typeTranslation;
        this.tips = tips;
    }

    public TranslationAnswer(String word, String translation, TypeTranslation typeTranslation, String definition, String audioUrl, Level level) {
        this.word = word;
        this.translation = translation;
        this.typeTranslation = typeTranslation;
        this.definition = definition;
        this.audioUrl = audioUrl;
        this.level = level;
    }


}
