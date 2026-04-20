package atl.eng.cards.repositories;

import atl.eng.cards.model.PronunciationSessionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PronunciationSessionInfoRepository extends JpaRepository<PronunciationSessionInfo, Long> {

    @Query("""
        select count(s) > 0 from PronunciationSessionInfo s
        where s.credential.id = :credentialId and s.status = 'STARTED'
        """)
    boolean existsActiveSessionForCredential(@Param("credentialId") Long credentialId);

    @Query("""
        select s from PronunciationSessionInfo s
        where s.credential.id = :credentialId and s.sessionId = :sessionId and s.status = 'STARTED'
        """)
    Set<PronunciationSessionInfo> findByCredentialIdAndSessionId(@Param("credentialId") Long credentialId,
                                                                 @Param("sessionId") Long sessionId);
}
