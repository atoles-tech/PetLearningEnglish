package atl.eng.cards.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atl.eng.cards.dto.credential.CredentialCreateRequest;
import atl.eng.cards.dto.credential.CredentialResponse;
import atl.eng.cards.exceptions.credential.CredentialAlreadyExists;
import atl.eng.cards.exceptions.credential.CredentialNotFoundException;
import atl.eng.cards.mapper.CredentialMapper;
import atl.eng.cards.model.Credential;
import atl.eng.cards.model.util.Role;
import atl.eng.cards.repositories.CredentialRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CredentialService {

    private CredentialRepository credentialRepository;
    private CredentialMapper credentialMapper;
    private BCryptPasswordEncoder encoder;

    //read
    public List<CredentialResponse> getAllCredential(){
        return credentialMapper
            .toCredentialResponseList(credentialRepository.findAll());
    }

    public CredentialResponse getById(Long id){
        return credentialMapper.toCredentialResponse(credentialRepository
            .findById(id).orElseThrow(()->new CredentialNotFoundException(id)));
    }

    public CredentialResponse getByUsername(String username){
        return credentialMapper.toCredentialResponse(credentialRepository
            .findByUsername(username).orElseThrow(()->new CredentialNotFoundException(username)));
    }

    //create
    @Transactional
    public CredentialResponse createCredential(CredentialCreateRequest req){
        if(credentialRepository.existsByUsername(req.getUsername())){
            throw new CredentialAlreadyExists(req.getUsername());
        }

        Credential credential = new Credential(null, req.getUsername(), encoder.encode(req.getPassword()), Role.USER, null, LocalDateTime.now());
        return credentialMapper.toCredentialResponse(credentialRepository.save(credential));
    }

    //delete
    @Transactional
    public void deleteCredential(Long id){
        if(credentialRepository.existsById(id)){
            throw new CredentialNotFoundException(id);
        }

        credentialRepository.deleteById(id);
    }

    // ENTITY
    public Credential getByUsernameEntity(String username){
        return credentialRepository
            .findByUsername(username).orElseThrow(()->new CredentialNotFoundException(username));
    }

    public Credential getByIdEntity(Long id){
        return credentialRepository
            .findById(id).orElseThrow(()->new CredentialNotFoundException(id));
    }

}
