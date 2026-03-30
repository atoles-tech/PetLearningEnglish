package atl.eng.cards.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "words")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Word {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word", length = 70, nullable = false, unique = true)
    private String word;

    @Column(name = "transcription", length = 70)
    private String transcription;

    @Column(name = "translation", length = 70, nullable = false)
    private String translation;

    @Column(name = "eng_s", length = 255)
    private String engSentences;

    @Column(name = "ru_s", length = 255)
    private String ruSentences;

    @Column(name = "definition", length = 255)
    private String definition;
}
