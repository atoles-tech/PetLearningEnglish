package atl.eng.cards.dto.credential;

import atl.eng.cards.model.util.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CredentialResponse {
    private Long id;
    private String username;
    private Role role;
}
