package atl.eng.cards.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import atl.eng.cards.dto.credential.CredentialResponse;
import atl.eng.cards.model.Credential;

@Mapper(componentModel = "spring")
public interface CredentialMapper {
    
    CredentialResponse toCredentialResponse(Credential credential);
    List<CredentialResponse> toCredentialResponseList(List<Credential> credentials);
}
