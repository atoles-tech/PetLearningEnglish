package atl.eng.cards.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import atl.eng.cards.dto.cards.WordResponse;
import atl.eng.cards.model.Word;

@Mapper(componentModel = "spring")
public interface WordMapper {
    WordResponse toWordResponse(Word word);
    List<WordResponse> toWordResponseList(List<Word> words);
    
    default Page<WordResponse> toWordResponsePage(Page<Word> words){
        if(words.isEmpty()){
            return Page.empty();
        }

        List<Word> wordsList = words.getContent();

        return new PageImpl<WordResponse>(
            toWordResponseList(wordsList), 
            words.getPageable(), 
            words.getSize()
        );
    }
}
