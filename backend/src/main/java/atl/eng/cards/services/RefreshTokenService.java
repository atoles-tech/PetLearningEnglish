package atl.eng.cards.services;

import atl.eng.cards.model.Credential;
import atl.eng.cards.model.RefreshToken;

public interface RefreshTokenService {
    public RefreshToken getTokenByCredential(Credential credential);
    public void deactiveToken(String username);
    public void refreshToken(RefreshToken token);
    public void createToken(Credential credential, String token);
    public RefreshToken getByToken(String token);
}
