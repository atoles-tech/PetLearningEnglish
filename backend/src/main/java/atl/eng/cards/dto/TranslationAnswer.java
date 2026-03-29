package atl.eng.cards.dto;

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
}
