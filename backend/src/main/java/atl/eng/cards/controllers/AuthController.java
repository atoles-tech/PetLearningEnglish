package atl.eng.cards.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import atl.eng.cards.dto.auth.AuthRequest;
import atl.eng.cards.dto.auth.AuthResponse;
import atl.eng.cards.dto.credential.CredentialCreateRequest;
import atl.eng.cards.dto.credential.CredentialResponse;
import atl.eng.cards.dto.token.TokenRequest;
import atl.eng.cards.services.impl.AuthServiceImpl;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req){
        return ResponseEntity.ok(authService.auth(req));
    }

    @PostMapping("/register")
    public ResponseEntity<CredentialResponse> register(@RequestBody CredentialCreateRequest req){
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody TokenRequest req){
        return ResponseEntity.ok(authService.validateToken(req.getToken()));
    } 

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody TokenRequest req){
        return ResponseEntity.ok(authService.refreshToken(req.getToken()));
    }
}
