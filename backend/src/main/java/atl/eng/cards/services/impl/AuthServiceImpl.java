package atl.eng.cards.services.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atl.eng.cards.dto.auth.AuthRequest;
import atl.eng.cards.dto.auth.AuthResponse;
import atl.eng.cards.dto.credential.CredentialCreateRequest;
import atl.eng.cards.dto.credential.CredentialResponse;
import atl.eng.cards.exceptions.auth.IncorrectPasswordException;
import atl.eng.cards.exceptions.token.IncorrectTokenException;
import atl.eng.cards.exceptions.token.RefreshTokenNotFound;
import atl.eng.cards.jwt.JwtUtil;
import atl.eng.cards.model.Credential;
import atl.eng.cards.model.RefreshToken;
import atl.eng.cards.services.AuthService;
import atl.eng.cards.services.CredentialService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{
    
    private final CredentialService credentialService;
    private final RefreshTokenServiceImpl refreshTokenService;

    private final BCryptPasswordEncoder encoder;

    private final JwtUtil jwtUtil;

    @PostConstruct
    public void init(){
        if(credentialService.findAllCredentials().isEmpty()){
            credentialService.createCredential(new CredentialCreateRequest("admin","password"));
        }
    }

    @Transactional
    public AuthResponse auth(AuthRequest req){
        Credential credential = credentialService.findEntityByUsername(req.getUsername());
        
        if(!encoder.matches(req.getPassword(), credential.getHashPassword())){
            throw new IncorrectPasswordException();
        }

        String accessToken = jwtUtil.generateAccessToken(credential);
        String refreshToken = jwtUtil.generateRefreshToken(credential);

        refreshTokenService.createToken(credential, refreshToken);
        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public CredentialResponse register(CredentialCreateRequest req){
        return credentialService.createCredential(req);
    }

    @Transactional
    public void logout(String username){
        refreshTokenService.deactiveToken(username);
    }

    @Transactional
    public AuthResponse refreshToken(String token){
        if(!jwtUtil.validateToken(token)){
            throw new IncorrectTokenException();
        }

        Credential credential = credentialService.findEntityByUsername(jwtUtil.extractUsernameFromToken(token));
        RefreshToken rToken = credential.getRefreshToken();
        
        if(!rToken.getRefreshToken().equals(token)){
            throw new RefreshTokenNotFound(token);
        }

        String accessToken = jwtUtil.generateAccessToken(credential);
        String refreshToken = jwtUtil.generateRefreshToken(credential);
    
        rToken.setRefreshToken(refreshToken);
        refreshTokenService.refreshToken(rToken);

        return new AuthResponse(accessToken, refreshToken);
    }

    public Boolean validateToken(String token){
        return jwtUtil.validateToken(token);
    }

}
