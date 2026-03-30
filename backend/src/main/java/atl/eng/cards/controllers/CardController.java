package atl.eng.cards.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import atl.eng.cards.jwt.UserDetailsImpl;
import atl.eng.cards.services.CardService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CardController {

    private CardService cardService;

    @PostMapping("/cards")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CardResponse> createCard(
        @RequestParam String word, 
        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        return ResponseEntity.ok(cardService.addCard(word, Long.valueOf(userDetailsImpl.getUsername())));
    }

    @GetMapping("/users/{id}/cards")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userDetailsImpl.getId().equals(#id))")
    public ResponseEntity<List<CardResponse>> getCards(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "learn") String type,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) { // type in [learn, repeat, all]
                
        if (type.equals("learn")) {
            return ResponseEntity.ok(cardService.learnCards(limit, id));
        }

        if (type.equals("all")) {

            return ResponseEntity.ok(cardService.findAllCardsByUserId(id));
        }

        return ResponseEntity.ok(cardService.repeatCards(limit, id));

    }

    @PutMapping("/cards")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @cardService.isOwnerCards(#cards, #userDetailsImpl.getUsername()))")
    public ResponseEntity<?> updateCards(
        @RequestBody List<UpdateCardDto> cards, 
        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        cardService.updateCards(cards);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
