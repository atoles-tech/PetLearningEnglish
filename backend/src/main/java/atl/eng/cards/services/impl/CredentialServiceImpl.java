package atl.eng.cards.services.impl;

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
import atl.eng.cards.services.CredentialService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepository credentialRepository;
    private final CredentialMapper credentialMapper;
    private final BCryptPasswordEncoder encoder;

    @Override
    public List<CredentialResponse> findAllCredentials(){
        return credentialMapper
            .toCredentialResponseList(credentialRepository.findAll());
    }

    @Override
    public CredentialResponse findById(Long id){
        return credentialMapper.toCredentialResponse(credentialRepository
            .findById(id).orElseThrow(()->new CredentialNotFoundException(id)));
    }

    @Override
    public CredentialResponse findByUsername(String username){
        return credentialMapper.toCredentialResponse(credentialRepository
            .findByUsername(username).orElseThrow(()->new CredentialNotFoundException(username)));
    }

    @Transactional
    @Override
    public CredentialResponse createCredential(CredentialCreateRequest req){
        if(credentialRepository.existsByUsername(req.getUsername())){
            throw new CredentialAlreadyExists(req.getUsername());
        }

        Credential credential = new Credential(null, req.getUsername(), encoder.encode(req.getPassword()), Role.USER, null, LocalDateTime.now());
        return credentialMapper.toCredentialResponse(credentialRepository.save(credential));
    }

    @Transactional
    @Override
    public void deleteCredential(Long id){
        if(credentialRepository.existsById(id)){
            throw new CredentialNotFoundException(id);
        }

        credentialRepository.deleteById(id);
    }

    @Override
    public Credential findEntityByUsername(String username){
        return credentialRepository
            .findByUsername(username).orElseThrow(()->new CredentialNotFoundException(username));
    }

    @Override
    public Credential findEntityById(Long id){
        return credentialRepository
            .findById(id).orElseThrow(()->new CredentialNotFoundException(id));
    }

}
