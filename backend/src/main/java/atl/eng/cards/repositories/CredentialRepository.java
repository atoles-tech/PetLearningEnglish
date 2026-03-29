package atl.eng.cards.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import atl.eng.cards.model.Credential;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long>{
    Optional<Credential> findByUsername(String username);
    Boolean existsByUsername(String username);
}
