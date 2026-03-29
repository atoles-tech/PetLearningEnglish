package atl.eng.cards.services;

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
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    
    private CredentialService credentialService;
    private RefreshTokenService refreshTokenService;

    private BCryptPasswordEncoder encoder;

    private JwtUtil jwtUtil;

    @PostConstruct
    public void init(){
        if(credentialService.getAllCredential().isEmpty()){
            credentialService.createCredential(new CredentialCreateRequest("admin","password"));
        }
    }

    //try to auth
    @Transactional
    public AuthResponse auth(AuthRequest req){
        Credential credential = credentialService.getByUsernameEntity(req.getUsername());
        
        if(!encoder.matches(req.getPassword(), credential.getHashPassword())){
            throw new IncorrectPasswordException();
        }

        String accessToken = jwtUtil.generateAccessToken(credential);
        String refreshToken = jwtUtil.generateRefreshToken(credential);

        refreshTokenService.createToken(credential, refreshToken);
        return new AuthResponse(accessToken, refreshToken);
    }

    //try to create credential
    @Transactional
    public CredentialResponse register(CredentialCreateRequest req){
        return credentialService.createCredential(req);
    }

    // logout
    @Transactional
    public void logout(String username){
        refreshTokenService.deactiveToken(username);
    }

    @Transactional
    public AuthResponse refreshToken(String token){
        if(!jwtUtil.validateToken(token)){
            throw new IncorrectTokenException();
        }

        Credential credential = credentialService.getByUsernameEntity(jwtUtil.extractUsernameFromToken(token));
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
