package atl.eng.cards.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import atl.eng.cards.model.Card;
import atl.eng.cards.model.Credential;
import atl.eng.cards.model.Word;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>{
        
    Optional<Card> findByWordAndCredential(Word word, Credential credential);

    @Query(
        value = "SELECT c FROM cards c WHERE c.credential.id=:userId"
    )
    List<Card> findCardsByUserId(@Param("userId") Long userId);

    @Query(
        value = "SELECT * FROM cards WHERE user_id=:userId ORDER BY knowledge ASC LIMIT :limit",
        nativeQuery = true
    )
    List<Card> findCardsForLearn(@Param("limit") Integer limit, @Param("userId") Long userId);

    @Query(
        value = "SELECT * FROM cards WHERE user_id=:userId ORDER BY last_used ASC LIMIT :limit",
        nativeQuery = true
    )
    List<Card> findCardsForRepeat(@Param("limit") Integer limit, @Param("userId") Long userId);

    @Query(
        value = "SELECT COUNT(c)>0 FROM cards c WHERE c.credential.id=:userId AND c.word=:word"
    )
    boolean existsByWordAndUserId(@Param("word")Word word, @Param("userId")Long userId);
}
