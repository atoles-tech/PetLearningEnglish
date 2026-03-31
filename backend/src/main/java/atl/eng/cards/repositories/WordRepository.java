package atl.eng.cards.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import atl.eng.cards.model.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    
    Page<Word> findAll(Pageable pageable);

    @Query("SELECT w FROM words w WHERE w.word LIKE CONCAT('%', :word, '%')")
    Page<Word> findAllByWord(@Param("word") String word, Pageable pageable);

    Optional<Word> findByWord(String word);
    
    boolean existsByWord(String word);

    @Modifying
    @Query("DELETE FROM words w WHERE w.word = :word")
    void deleteByWord(@Param("word") String word);  

    @Query("SELECT w FROM words w WHERE w.definition IS null")
    List<Word> findWordsWhereDefinitionIsNull();
}
