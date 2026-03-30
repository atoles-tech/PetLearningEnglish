package atl.eng.cards.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateCardDto {

    @NotNull(message = "Id is required")
    private Long id;
    
    @Min(value = 1, message = "Knowledge must be between 1 and 10")
    @Max(value = 10, message = "Knowledge must be between 1 and 10")
    private Integer knowledge;
}
