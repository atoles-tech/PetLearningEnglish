package atl.eng.cards.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import atl.eng.cards.model.Credential;
import atl.eng.cards.model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByCredential(Credential credential);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Boolean existsByCredential(Credential credential);
}
