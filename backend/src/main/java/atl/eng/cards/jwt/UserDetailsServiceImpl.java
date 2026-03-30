package atl.eng.cards.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import atl.eng.cards.model.Credential;
import atl.eng.cards.repositories.CredentialRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

    private final CredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credential credential = credentialRepository
            .findByUsername(username)
            .orElseThrow(()->new UsernameNotFoundException(username));
        
        UserDetailsImpl user = new UserDetailsImpl(
            credential.getId(), 
            username, 
            credential.getRole()
        );

        return user;
    }
    
    
}
