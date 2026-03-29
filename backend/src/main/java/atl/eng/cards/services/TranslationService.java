package atl.eng.cards.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import atl.eng.cards.exceptions.dict.URIException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TranslationService{

    private static final String URL = "https://translate.googleapis.com/translate_a/single?client=gtx&dt=t&sl=eng&tl=ru&q=";

    private RestTemplate restTemplate;

    public String translate(String word){
        String trl = restTemplate.getForObject(URL + word, String.class);
        String[] strings = trl.split("\"");
        
        if(strings.length <= 2){
            throw new URIException();
        }

        return strings[1];
    }
}
