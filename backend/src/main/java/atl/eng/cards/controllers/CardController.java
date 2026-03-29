package atl.eng.cards.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import atl.eng.cards.dto.UpdateCardDto;
import atl.eng.cards.dto.cards.CardResponse;
import atl.eng.cards.mapper.CardMapper;
import atl.eng.cards.services.impl.CardServiceImpl;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CardController {

    private CardServiceImpl cardService;
    private CardMapper cardMapper;

    @PostMapping("/cards")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> createCard(@RequestParam String word, Principal principal) {
        return ResponseEntity.ok(cardService.addCard(word, Long.valueOf(principal.getName())));
    }

    @GetMapping("/users/{id}/cards")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #principal.getName().equals(#id.toString()))")
    public ResponseEntity<List<CardResponse>> getCards(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "learn") String type,
            @PathVariable Long id,
            Principal principal) { // type in [learn, repeat]
                
        if (type.equals("learn")) {
            return ResponseEntity.ok(cardMapper.toCardResponseList(cardService.learnCards(limit, id)));
        }

        if (type.equals("all")) {

            return ResponseEntity.ok(cardMapper.toCardResponseList(cardService.findAllCardsByUserId(id)));
        }

        return ResponseEntity.ok(cardMapper.toCardResponseList(cardService.repeatCards(limit, id)));

    }

    // TODO
    @PutMapping("/cards")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @cardService.isOwnerCards(#cards, #principal.name))")
    public ResponseEntity<?> updateCards(@RequestBody List<UpdateCardDto> cards, Principal principal) {
        cardService.updateCards(cards);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
