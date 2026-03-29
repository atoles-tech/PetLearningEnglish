package atl.eng.cards.services;

import java.util.List;

import atl.eng.cards.dto.credential.CredentialCreateRequest;
import atl.eng.cards.dto.credential.CredentialResponse;
import atl.eng.cards.model.Credential;

public interface CredentialService {
    
    public List<CredentialResponse> findAllCredentials();
    public CredentialResponse findById(Long id);
    public CredentialResponse findByUsername(String username);
    public CredentialResponse createCredential(CredentialCreateRequest req);
    public void deleteCredential(Long id);

    // Entity methods (utils)
    public Credential findEntityByUsername(String username);
    public Credential findEntityById(Long id);
}
