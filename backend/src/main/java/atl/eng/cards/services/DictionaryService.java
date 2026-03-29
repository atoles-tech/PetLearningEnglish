package atl.eng.cards.services;


import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import atl.eng.cards.dto.translation.Tip;
import atl.eng.cards.dto.translation.util.Tips;
import atl.eng.cards.exceptions.cards.WordNotFoundInDictException;
import atl.eng.cards.exceptions.dict.URIException;
import atl.eng.cards.model.Word;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DictionaryService{

    private static final String BASE_URL = "https://wooordhunt.ru/word/";
    private static final String BASE_URL_TIPS = "https://wooordhunt.ru/openscripts/forjs/get_tips.php?abc=";

    public Word getTranslation(String word) throws Exception{
        try {
            Document doc = Jsoup.connect(BASE_URL + word)
                .userAgent("Mozilla/5.0")
                .timeout(5000)
                .get();


            Elements wordFormsElements = doc.select("div#word_forms > a");

            if(!wordFormsElements.isEmpty()){
                String formWordURL = wordFormsElements.getFirst().attr("href");
                return getTranslation(formWordURL.substring(6));
            }

            Elements transcriptionElements = doc.select("span.transcription");
            Elements translationElements = doc.select("div.t_inline_en");
            Elements engSentences = doc.select("p.ex_o");
            Elements ruSentences = doc.select("p.ex_t");

            String result = translationElements.getFirst().text();
            String[] strings = result.split(",");
           
            if(strings.length > 2){
                result = strings[0] + ", " + strings[1];
            }

            return Word.builder()
                .word(word)
                .transcription(transcriptionElements.isEmpty()?null:transcriptionElements.getFirst().text())
                .translation(translationElements.isEmpty()?null:result)
                .engSentences(engSentences.isEmpty()?null:engSentences.getFirst().text().trim())
                .ruSentences(ruSentences.isEmpty()?null:ruSentences.getFirst().text().trim())
                .build();

        } catch (Exception e) {
            // situation when word doesn't exist in our dictinary
            throw new WordNotFoundInDictException(word);
        }
    }

    public List<Tip> getTips(String word){
        try {
            Document doc = Jsoup.connect(BASE_URL_TIPS + word)
                .userAgent("Mozilla/5.0")
                .timeout(5000)
                .get();

            Tips tips = new Gson().fromJson(doc.body().text(), Tips.class);
            return tips.getTips();
        } catch (Exception e) {
            throw new URIException();
        }
    } 
}
