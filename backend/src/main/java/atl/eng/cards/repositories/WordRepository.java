package atl.eng.cards.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import atl.eng.cards.model.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByWord(String word);
    
    boolean existsByWord(String word);

    @Modifying
    @Query("DELETE FROM words w WHERE w.word = :word")
    void deleteByWord(@Param("word") String word);  
}
