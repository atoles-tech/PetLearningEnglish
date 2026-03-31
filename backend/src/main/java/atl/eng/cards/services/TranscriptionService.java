package atl.eng.cards.services;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atl.eng.cards.model.Word;

@Service
@Transactional(readOnly = true)
public class TranscriptionService {

    private final static Character SOFT_LETTER = '\'';
    private final static Character LONG_LETTER = ':';

    @Value("${eng.words.pron.soft-acc:0.75}")
    private Double softLetterAcc;

    @Value("${eng.words.pron.long-acc:0.9}")
    private Double longLetterAcc;

    public List<Word> findNearPronancuationWords(List<Word> words, Word word, Double accuracy) {
        List<Word> result = new ArrayList<>();

        for (Word w : words) {
            double fa = pronNearWord(w, word);
            double sa = pronNearWord(word, w);

            double finalAcc = (fa+sa)/2.;
            if(finalAcc >= accuracy){
                result.add(w);
            }
        }

        return result;
    }

    private double pronNearWord(Word firstWord, Word secondWord) {
        String firstTranscription = firstWord.getTranscription();
        String secondTranscription = secondWord.getTranscription();

        if (firstTranscription == null || firstTranscription.isBlank() ||
                secondTranscription == null || secondTranscription.isBlank()) {
            return 0.;
        }

        List<String> firstTrancriptionParts = getPartsOfTranscription(
                firstTranscription.substring(1, firstTranscription.length() - 2));
        List<String> secondTrancriptionParts = getPartsOfTranscription(
                secondTranscription.substring(1, secondTranscription.length() - 2));

        double countEq = 0;
        int prevIndex = -1;

        for (int i = 0; i < firstTrancriptionParts.size(); i++) {
            for (int j = prevIndex + 1; j < secondTrancriptionParts.size(); j++) {
                String ftp = firstTrancriptionParts.get(i);
                String stp = secondTrancriptionParts.get(j);

                if (ftp.contains("" + SOFT_LETTER)) {
                    ftp = ftp.substring(1);

                    if (stp.contains("" + SOFT_LETTER)) {
                        stp = stp.substring(1);

                        if ((int) ftp.charAt(1) == (int) stp.charAt(1)) {
                            countEq += 1;
                            break;
                        }
                    }

                    if ((int) ftp.charAt(1) == (int) stp.charAt(0)) {
                        countEq += softLetterAcc;
                        break;
                    }
                }

                if (ftp.contains("" + LONG_LETTER)) {
                    ftp = ftp.substring(1);

                    if (stp.contains("" + LONG_LETTER)) {
                        stp = stp.substring(1);

                        if ((int) ftp.charAt(1) == (int) stp.charAt(1)) {
                            countEq += 1;
                            break;
                        }
                    }

                    if ((int) ftp.charAt(1) == (int) stp.charAt(0)) {
                        countEq += softLetterAcc;
                        break;
                    }
                }

                if ((int) ftp.charAt(0) == (int) stp.charAt(0)) {
                    countEq += 1.;
                    prevIndex = j;
                    break;
                }

            }
        }

        double finalAcc = countEq / (double) firstTrancriptionParts.size();

        return finalAcc;
    }

    private List<String> getPartsOfTranscription(String transcription) {
        List<String> parts = new ArrayList<>();

        for (int i = 0; i < transcription.length(); i++) {
            if (transcription.charAt(i) == SOFT_LETTER) {
                parts.add("" + transcription.charAt(i) + transcription.charAt(i + 1));
                i++;
                continue;
            }

            if (transcription.charAt(i) == LONG_LETTER) {
                String last = parts.removeLast() + LONG_LETTER;
                parts.add(last);
                continue;
            }

            parts.add("" + transcription.charAt(i));
        }

        return parts;
    }

}
