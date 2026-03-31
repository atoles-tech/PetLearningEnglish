package atl.eng.cards.services;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class DictionaryService {

    private static final String BASE_URL_DEFINITIONS = "https://freedictionaryapi.com/api/v1/entries/en/";

    private static final String BASE_URL = "https://wooordhunt.ru";

    private static final String BASE_URL_WORD = "https://wooordhunt.ru/word/";
    private static final String BASE_URL_TIPS = "https://wooordhunt.ru/openscripts/forjs/get_tips.php?abc=";

    private final RestTemplate restTemplate;

    private Queue<String> queueWords = new LinkedList<>();

    public Word getTranslation(String word) throws Exception {
        try {
            Document doc = Jsoup.connect(BASE_URL_WORD + word)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            // Elements wordFormsElements = doc.select("div#word_forms > a");

            // if (!wordFormsElements.isEmpty()) {
            // String formWordURL = wordFormsElements.getFirst().attr("href");
            // return getTranslation(formWordURL.substring(6));
            // }
            //

            Elements transcriptionElements = doc.select("span.transcription");
            Elements translationElements = doc.select("div.t_inline_en");
            Elements engSentences = doc.select("p.ex_o");
            Elements ruSentences = doc.select("p.ex_t");
            Elements audioElements = doc.select("source");

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
                    .audioUrl(audioElements.isEmpty() ? null : BASE_URL + audioElements.getFirst().attr("src"))
                    .build();

        } catch (Exception e) {
            log.error("Error with finding word '{}': {}", word, e.getMessage());
            throw new WordNotFoundInDictException(word);
        }
    }

    public String getDefinition(String word) {
        try {
            String definitionJson = restTemplate.getForObject(BASE_URL_DEFINITIONS + word, String.class);
            JSONObject jsonObject = new JSONObject(definitionJson);

            JSONArray arrayEntries = jsonObject.getJSONArray("entries");
            if (arrayEntries.isEmpty()) {
                log.warn("Definition not found for '{}'", word);
                return null;
            }

            JSONArray arraySenses = arrayEntries.getJSONObject(0).getJSONArray("senses");
            if (arraySenses.isEmpty()) {
                log.warn("Definition not found for '{}'", word);
                return null;
            }

            String definition = null;

            for (int i = 0; i < arraySenses.length(); i++) {
                if (definition == null ||
                    definition.contains(";") ||
                    definition.contains("(") ||
                    definition.contains(")")
                ) {
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

    public String peek() {
        return queueWords.remove();
    }

    public boolean available() {
        return queueWords.size() != 0;
    }
}
