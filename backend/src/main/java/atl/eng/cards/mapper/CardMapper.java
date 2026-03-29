package atl.eng.cards.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import atl.eng.cards.dto.cards.CardResponse;
import atl.eng.cards.model.Card;

@Mapper(componentModel = "spring", uses = {WordMapper.class})
public interface CardMapper {
    @Mapping(target = "userId", source = "credential.id")
    CardResponse toCardResponse(Card card);
    List<CardResponse> toCardResponseList(List<Card> cards);
}
