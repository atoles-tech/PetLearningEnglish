package atl.eng.cards.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import atl.eng.cards.dto.cards.WordResponse;
import atl.eng.cards.model.Word;

@Mapper(componentModel = "spring")
public interface WordMapper {
    WordResponse toWordResponse(Word word);
    List<WordResponse> toWordResponseList(List<Word> words);
}
