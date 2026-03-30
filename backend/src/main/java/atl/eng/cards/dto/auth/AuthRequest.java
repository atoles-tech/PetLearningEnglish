package atl.eng.cards.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthRequest {
    
    @NotBlank(message = "Username is required")
    @Size(min = 5, message = "Minimum size of username is 5 characters")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
