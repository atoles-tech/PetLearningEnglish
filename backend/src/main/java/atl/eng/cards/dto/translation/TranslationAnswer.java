package atl.eng.cards.dto.translation;

import java.util.List;

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
    private List<Tip> tips;

    public TranslationAnswer(String word, String translation, TypeTranslation typeTranslation) {
        this.word = word;
        this.translation = translation;
        this.typeTranslation = typeTranslation;
    }
    
}
