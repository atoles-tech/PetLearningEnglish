package atl.eng.cards.dto.cards;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WordResponse {
    private Long id;
    private String word;
    private String transcription;
    private String translation;
    private String engSentences;
    private String ruSentences;
    private String definition;
    private String audioUrl;
}
