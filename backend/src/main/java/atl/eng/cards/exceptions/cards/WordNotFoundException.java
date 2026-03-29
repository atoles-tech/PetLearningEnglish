package atl.eng.cards.exceptions.cards;

public class WordNotFoundException extends RuntimeException{
    public WordNotFoundException(String word){
        super("Word '" + word + "' not found");
    }
}
