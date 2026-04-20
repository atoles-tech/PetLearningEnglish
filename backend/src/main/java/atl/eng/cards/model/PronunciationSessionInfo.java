package atl.eng.cards.model;

import atl.eng.cards.model.util.PronunciationSessionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@Entity
public class PronunciationSessionInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id")
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "credential_id", referencedColumnName = "id")
    private Credential credential;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "session_status")
    @Enumerated(EnumType.STRING)
    private PronunciationSessionStatus status;

    @ManyToOne
    @JoinColumn(name = "word_id", referencedColumnName = "id")
    private Word word;

    @Column(name = "attempt")
    private Integer attempt;
}
