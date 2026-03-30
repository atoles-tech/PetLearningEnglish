package atl.eng.cards.exceptions.cards;

public class WordNotFoundException extends RuntimeException{
    public WordNotFoundException(String word){
        super("Word '" + word + "' not found");
    }

    public WordNotFoundException(Long id){
        super("Word id='" + id + "' not found");
    }
}
