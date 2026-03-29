package atl.eng.cards.dto.cards;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {
    private Long id;
    private Long userId;
    private WordResponse word;
    private Integer knowledge; // 0-10
    private LocalDateTime lastUsed;
}
