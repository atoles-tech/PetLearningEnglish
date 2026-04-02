package atl.eng.cards.model;

import atl.eng.cards.model.util.Level;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "words")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Word {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word", length = 70, nullable = false, unique = true)
    private String word;

    @Column(name = "transcription", length = 70)
    private String transcription;

    @Column(name = "translation", length = 120, nullable = false)
    private String translation;

    @Column(name = "eng_s", length = 255)
    private String engSentences;

    @Column(name = "ru_s", length = 255)
    private String ruSentences;

    @Column(name = "definition", length = 255)
    private String definition;

    @Column(name = "audio_url", length = 255)
    private String audioUrl;

    @Column(name = "level", length = 4)
    @Enumerated(EnumType.STRING)
    private Level level;
}
