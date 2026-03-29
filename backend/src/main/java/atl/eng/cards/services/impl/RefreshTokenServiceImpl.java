package atl.eng.cards.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atl.eng.cards.exceptions.token.IncorrectTokenException;
import atl.eng.cards.exceptions.token.RefreshTokenNotFound;
import atl.eng.cards.model.Credential;
import atl.eng.cards.model.RefreshToken;
import atl.eng.cards.repositories.RefreshTokenRepository;
import atl.eng.cards.services.CredentialService;
import atl.eng.cards.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService{
    
    private RefreshTokenRepository refreshTokenRepository;
    private CredentialService credentialService;

    public RefreshToken getTokenByCredential(Credential credential){
        return refreshTokenRepository.findByCredential(credential)
            .orElseThrow(()->new RefreshTokenNotFound(credential.getId()));
    }

    @Transactional
    public void deactiveToken(String username){
        Credential credential = credentialService.findEntityByUsername(username);
        RefreshToken refreshToken = refreshTokenRepository
            .findByCredential(credential).orElseThrow(()->new RefreshTokenNotFound(credential.getId()));
        
        refreshToken.setIsActive(false);
        refreshTokenRepository.save(refreshToken);
    } 

    @Transactional
    public void refreshToken(RefreshToken token){
        if(!token.getIsActive()){
            throw new IncorrectTokenException();
        }

        refreshTokenRepository.save(token);
    }

    @Transactional
    public void createToken(Credential credential, String token){ // REVIEW THIS METHOD
        if(refreshTokenRepository.existsByCredential(credential)){
            System.out.println("EXISTS");
            RefreshToken refreshToken = refreshTokenRepository
                .findByCredential(credential).get();

            refreshToken.setRefreshToken(token);
            refreshToken.setIsActive(true);
            refreshTokenRepository.save(refreshToken);
            return;
        }   

        RefreshToken refreshToken = new RefreshToken(null, token, credential, true);
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getByToken(String token){
        return refreshTokenRepository.findByRefreshToken(token)
            .orElseThrow(()->new RefreshTokenNotFound(token));
    }

}
