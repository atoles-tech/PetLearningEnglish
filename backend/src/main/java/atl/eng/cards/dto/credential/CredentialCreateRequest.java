package atl.eng.cards.dto.credential;

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
public class CredentialCreateRequest {
    @NotBlank(message = "username is required")
    @Size(min = 5, max = 16, message = "Username size must be between 5 and 16")
    private String username;
    
    @NotBlank(message = "password is required")
    @Size(min = 8, max = 32, message = "Password size must be between 8 and 32")
    private String password;
}
