package atl.eng.cards.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import atl.eng.cards.dto.cards.WordResponse;
import atl.eng.cards.services.WordService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/words")
@RequiredArgsConstructor
public class WordController {
    
    private final WordService wordService;

    @GetMapping
    public ResponseEntity<Page<WordResponse>> findWords(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "word") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(required = false) String word
    ){
        Sort sort = Sort.by(direction.equals("asc")?Direction.ASC:Direction.DESC, sortBy);

        if(word == null || word.isEmpty()){
            return ResponseEntity.ok(wordService.findAll(PageRequest.of(page, size, sort)));
        }

        return ResponseEntity.ok(wordService.findAllByWord(word, PageRequest.of(page, size, sort)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WordResponse> findWordById(
        @PathVariable Long id
    ){
        return ResponseEntity.ok(wordService.findById(id));
    }

}
