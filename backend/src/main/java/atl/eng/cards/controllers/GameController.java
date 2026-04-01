package atl.eng.cards.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import atl.eng.cards.dto.game.StartGamePronancuationResponse;
import atl.eng.cards.services.GamePronancuationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GameController {
    
    private final GamePronancuationService gamePronancuationService;

    @PostMapping("/games/pron")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StartGamePronancuationResponse> generateNewGameSession(){
        return ResponseEntity.ok(gamePronancuationService.startGamePronancuationFromAllWords());
    }

    @PostMapping("/games/pron/{sessionId}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Boolean> checkGame(@PathVariable Long sessionId, @RequestParam String word){
        return ResponseEntity.ok(gamePronancuationService.checkWord(sessionId, word));
    }
    

    @GetMapping("/games/pron/{sessionId}/audio")
    public ResponseEntity<byte[]> getAudio(@PathVariable Long sessionId){
        return ResponseEntity.status(200)
            .contentType(MediaType.parseMediaType("audio/mpeg"))
            .body(gamePronancuationService.getAudio(sessionId));
    }

}
