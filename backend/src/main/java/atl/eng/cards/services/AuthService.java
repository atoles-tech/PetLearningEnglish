package atl.eng.cards.services;

import atl.eng.cards.dto.auth.AuthRequest;
import atl.eng.cards.dto.auth.AuthResponse;
import atl.eng.cards.dto.credential.CredentialCreateRequest;
import atl.eng.cards.dto.credential.CredentialResponse;

public interface AuthService {
    public AuthResponse auth(AuthRequest req);
    public CredentialResponse register(CredentialCreateRequest req);
    public void logout(String username);
    public AuthResponse refreshToken(String token);
    public Boolean validateToken(String token);
}
