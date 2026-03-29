package atl.eng.cards.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import atl.eng.cards.services.WordService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class TranslateController {
    
    private WordService wordService;

    @GetMapping("/translate")
    public ResponseEntity<?> translate(@RequestParam String q){
        return ResponseEntity.ok(wordService.getTranslation(q));
    }

}
