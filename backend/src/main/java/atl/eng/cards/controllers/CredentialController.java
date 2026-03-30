package atl.eng.cards.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import atl.eng.cards.jwt.UserDetailsImpl;
import atl.eng.cards.services.impl.CredentialServiceImpl;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class CredentialController {
    
    private final CredentialServiceImpl credentialService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id.equals(#userDetailsImpl.getUsername()))")
    public ResponseEntity<?> findById(
        @PathVariable Long id, 
        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ){
        return ResponseEntity.ok(credentialService.findById(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> findMe(
        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ){
        return ResponseEntity.ok(credentialService.findById(Long.valueOf(userDetailsImpl.getUsername())));
    }
}
