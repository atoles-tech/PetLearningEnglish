package atl.eng.cards.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateCardDto {
    private Long id;
    
    @Min(value = 0)
    @Max(value = 10)
    private Integer knowledge;
}
