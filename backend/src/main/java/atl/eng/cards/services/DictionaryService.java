package atl.eng.cards.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import atl.eng.cards.dto.translation.Tip;
import atl.eng.cards.dto.translation.util.Tips;
import atl.eng.cards.exceptions.cards.WordNotFoundInDictException;
import atl.eng.cards.exceptions.dict.URIException;
import atl.eng.cards.model.Word;
import atl.eng.cards.model.util.Level;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class DictionaryService {

    private static final String BASE_URL_DEFINITIONS = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    // private static final String BASE_URL = "https://wooordhunt.ru";

    private static final String BASE_URL_WORD = "https://wooordhunt.ru/word/";
    private static final String BASE_URL_TIPS = "https://wooordhunt.ru/openscripts/forjs/get_tips.php?abc=";

    private static final String BASE_URL_AUDIO = "https://wooordhunt.ru/data/sound/sow/us/";

    private static final String BASE_URL_LEVEL = "https://dictionary.cambridge.org/ru/%D1%81%D0%BB%D0%BE%D0%B2%D0%B0%D1%80%D1%8C/%D0%B0%D0%BD%D0%B3%D0%BB%D0%B8%D0%B9%D1%81%D0%BA%D0%B8%D0%B9/";

    private final RestTemplate restTemplate;

    private Queue<String> queueWords = new LinkedList<>();

    @PostConstruct
    public void checkDir(){
        File file = new File("audio");

        if(!file.isDirectory()){
            file.mkdir();
            log.info("Directory 'audio' created");
        }
    }

    public Word getTranslation(String word) throws Exception {
        try {
            Document doc = Jsoup.connect(BASE_URL_WORD + word)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            Elements transcriptionElements = doc.select("span.transcription");
            Elements translationElements = doc.select("div.t_inline_en");
            Elements engSentences = doc.select("p.ex_o");
            Elements ruSentences = doc.select("p.ex_t");

            String result = translationElements.getFirst().text();
            String[] strings = result.split(",");

            if (strings.length > 2) {
                result = strings[0] + ", " + strings[1];
            }

            queueWords.add(word);

            log.info("Word '{}' parsered", word);

            return Word.builder()
                    .word(word)
                    .transcription(transcriptionElements.isEmpty() ? null : transcriptionElements.getFirst().text())
                    .translation(translationElements.isEmpty() ? null : result)
                    .engSentences(engSentences.isEmpty() ? null : engSentences.getFirst().text().trim())
                    .ruSentences(ruSentences.isEmpty() ? null : ruSentences.getFirst().text().trim())
                    .audioUrl(BASE_URL_AUDIO + word + ".mp3")
                    .build();

        } catch (Exception e) {
            log.error("Error with finding word '{}': {}", word, e.getMessage());
            throw new WordNotFoundInDictException(word);
        }
    }

    public String getDefinition(String word) {
        try {
            String definitionJson = restTemplate.getForObject(BASE_URL_DEFINITIONS + word, String.class);
            JSONArray jsonArray = new JSONArray(definitionJson);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            JSONArray arrayEntries = jsonObject.getJSONArray("meanings");
            if (arrayEntries.isEmpty()) {
                log.warn("Definition not found for '{}'", word);
                return null;
            }

            JSONArray arraySenses = arrayEntries.getJSONObject(0).getJSONArray("definitions");
            if (arraySenses.isEmpty()) {
                log.warn("Definition not found for '{}'", word);
                return null;
            }

            String definition = null;

            for (int i = 0; i < arraySenses.length(); i++) {
                if (definition == null ||
                        definition.contains(";") ||
                        definition.contains("(") ||
                        definition.contains(")")) {
                    definition = arraySenses.getJSONObject(i).getString("definition");
                } else {
                    break;
                }

            }

            log.info("Found definition for word '{}': {}", word, definition);

            return definition;

        } catch (Exception e) {
            log.error("Error find definition for word '{}': {}", word, e.getMessage());
            return null;
        }
    }

    public Level getLevel(String word) {
        try {
            Document doc = Jsoup.connect(BASE_URL_LEVEL + word)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            Elements levels = doc.select("div.ddef_h > span > span");

            if (levels.isEmpty()) {
                return null;
            }

            Level level = Level.valueOf(levels.get(0).text());

            log.info("Found level for word '{}': {}", word, level);

            return level;
        } catch (Exception e) {
            log.error("Error with finding level '{}': {}", word, e.getMessage());
            throw new WordNotFoundInDictException(word);
        }
    }

    public List<Tip> getTips(String word) {
        try {
            Document doc = Jsoup.connect(BASE_URL_TIPS + word)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            Tips tips = new Gson().fromJson(doc.body().text(), Tips.class);

            if (tips == null || tips.getTips() == null) {
                return List.of();
            }

            log.info("Found tips for word '{}': {}", word, tips.getTips());

            return tips.getTips();
        } catch (Exception e) {
            log.error("Error find tips for word: '{}': {}", word, e.getMessage());
            throw new URIException();
        }
    }

    public void downloadAudioFile(String word, String audioUrl) {

        try {
            URL url = new URI(audioUrl).toURL();

            InputStream in = url.openConnection().getInputStream();
            OutputStream out = new FileOutputStream("audio/"+word + ".mp3");

            int n;
            byte[] buffer = new byte[4098];

            while(in.available() > 0){
                n = in.read(buffer);
                out.write(buffer, 0, n);
            }

            in.close();
            out.close();

            log.info("Audio file saved for word: '{}'", word);
        } catch (IOException ex) {
            log.error("Error save audio file {}",ex.getMessage());
        } catch (URISyntaxException ex){
            log.error("Error save audio file {}",ex.getMessage());
        }
    }

    public void addWordsToQueue(List<String> words) {
        this.queueWords.addAll(words);
    }

    public String peek() {
        return queueWords.remove();
    }

    public boolean available() {
        return queueWords.size() != 0;
    }

}
