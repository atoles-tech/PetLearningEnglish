package atl.eng.cards.controllers;

import atl.eng.cards.dto.game.StartGamePronunciationRequest;
import atl.eng.cards.jwt.UserDetailsImpl;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import atl.eng.cards.dto.game.StartGamePronunciationResponse;
import atl.eng.cards.services.GamePronunciationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/games/pronuncioation")
@RequiredArgsConstructor
public class GamePronunciationController {
    
    private final GamePronunciationService gamePronunciationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StartGamePronunciationResponse> generateNewGameSession(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody StartGamePronunciationRequest request
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(gamePronunciationService.startGamePronunciation(
                        request.getWords(),
                        userDetails.getId()
                ));
    }

    @PostMapping("/{sessionId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Boolean> checkGame(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long sessionId,
            @RequestParam String word
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gamePronunciationService.checkSession(
                        sessionId,
                        userDetails.getId(),
                        word
                ));
    }
    

    @GetMapping("/{sessionId}/audio")
    public ResponseEntity<Resource> getAudio(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long sessionId
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(gamePronunciationService.getAudio(
                        sessionId,
                        userDetails.getId()
                ));
    }

}
