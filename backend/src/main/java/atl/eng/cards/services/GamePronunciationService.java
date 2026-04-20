package atl.eng.cards.services;

import atl.eng.cards.exceptions.games.SessionPronExistsForUser;
import atl.eng.cards.exceptions.games.SessionPronNotFoundException;
import atl.eng.cards.model.Credential;
import atl.eng.cards.model.PronunciationSessionInfo;
import atl.eng.cards.model.Word;
import atl.eng.cards.model.util.PronunciationSessionStatus;
import atl.eng.cards.repositories.PronunciationSessionInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import atl.eng.cards.dto.game.StartGamePronunciationResponse;
import atl.eng.cards.services.store.WordGamePronunciationStorage;
import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GamePronunciationService {

    private final PronunciationSessionInfoRepository pronunciationSessionInfoRepository;
    private final WordService wordService;
    private final CredentialService credentialService;

    private final Random random = new Random();

    @Value("${app.pronunciation.min-session-id:100000}")
    private Long minSessionId;

    @Value("${app.pronunciation.max-session-id:1000000}")
    private Long maxSessionId;

    @Value("${app.pronunciation.max-number-attempt:3")
    private Integer maxNumberAttempt;

    public StartGamePronunciationResponse startGamePronunciation(List<String> words, Long credentialId){
        if(pronunciationSessionInfoRepository.existsActiveSessionForCredential(credentialId)){
            throw new SessionPronExistsForUser(credentialId);
        }

        String sessionWord = words.get(random.nextInt(words.size()));

        Word word = wordService.findByWord(sessionWord);
        Credential credential = credentialService.findEntityById(credentialId);

        PronunciationSessionInfo sessionInfo = PronunciationSessionInfo.builder()
                .status(PronunciationSessionStatus.STARTED)
                .createdAt(LocalDateTime.now())
                .credential(credential)
                .word(word)
                .sessionId(random.nextLong(minSessionId, maxSessionId))
                .attempt(0)
                .build();

        PronunciationSessionInfo savedSessionInfo =
                pronunciationSessionInfoRepository.save(sessionInfo);

        return new StartGamePronunciationResponse(savedSessionInfo.getSessionId());
    }

    public boolean checkSession(Long sessionId, Long credentialId, String word){
        PronunciationSessionInfo sessionInfo = pronunciationSessionInfoRepository
                .findByCredentialIdAndSessionId(credentialId, sessionId)
                .stream()
                .findFirst()
                .orElseThrow(()->new SessionPronNotFoundException(sessionId, credentialId));

        sessionInfo.setAttempt(sessionInfo.getAttempt() + 1);

        boolean isRight = Objects.equals(sessionInfo.getWord().getWord(), word);

        if(!isRight && sessionInfo.getAttempt() >= maxNumberAttempt){
            sessionInfo.setStatus(PronunciationSessionStatus.FAILED);
        }
        else if(isRight){
            sessionInfo.setStatus(PronunciationSessionStatus.SUCCEED);
        }

        pronunciationSessionInfoRepository.save(sessionInfo);

        return isRight;
    }

    public ByteArrayResource getAudio(Long sessionId, Long credentialId) {
        PronunciationSessionInfo sessionInfo = pronunciationSessionInfoRepository
                .findByCredentialIdAndSessionId(credentialId, sessionId)
                .stream()
                .findFirst()
                .orElseThrow(()->new SessionPronNotFoundException(sessionId, credentialId));

        Word word = sessionInfo.getWord();

        if (word == null) {
            throw new SessionPronNotFoundException(sessionId, credentialId);
        }

        try (InputStream in = new FileInputStream("audio/" + word.getWord() + ".mp3")) {
            return new ByteArrayResource(in.readAllBytes());
        } catch (IOException ex) {
            // TODO:
        }

        return null;
    }
}
