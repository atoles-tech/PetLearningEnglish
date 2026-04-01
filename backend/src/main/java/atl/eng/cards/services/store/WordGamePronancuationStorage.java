package atl.eng.cards.services.store;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import atl.eng.cards.exceptions.games.SessionPronNotFoundException;
import atl.eng.cards.model.Word;

@Service
public class WordGamePronancuationStorage {

    private Map<Long, Word> currentWords;
    private Random random;

    public WordGamePronancuationStorage() {
        this.currentWords = new ConcurrentHashMap<Long, Word>();
        this.random = new Random();
    }

    public Long generateNewSession(List<Word> words) {
        if (words == null || words.isEmpty()) {
            throw new NoSuchElementException();
        }

        Word word = words.get(random.nextInt(0, words.size() - 1));
        Long id = Long.valueOf(random.nextInt(100000, 9999999));

        currentWords.put(id, word);

        return id;
    }

    public boolean checkSession(Long id, String word) {
        Word sessionWord = currentWords.get(id);

        if (sessionWord.getWord().equals(word)) {
            currentWords.remove(id);
            return true;
        }

        currentWords.remove(id);
        return false;
    }

    public void remove(Long id) {
        currentWords.remove(id);
    }

    public byte[] getAudio(Long sessionId) {
        try {
            Word word = currentWords.get(sessionId);

            if (word == null) {
                throw new SessionPronNotFoundException(sessionId);
            }

            InputStream in = new FileInputStream("audio/" + word.getWord() + ".mp3");

            return in.readAllBytes();
        } catch (FileNotFoundException ex) {
            // TODO: 
        } catch (IOException ex){
            // TODO:
        }

        return null;
    }

}
