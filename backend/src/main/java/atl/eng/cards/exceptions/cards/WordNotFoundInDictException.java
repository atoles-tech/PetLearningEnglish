package atl.eng.cards.exceptions.cards;

public class WordNotFoundInDictException extends RuntimeException{
    public WordNotFoundInDictException(String word){
        super("Word '" + word + "' didn't find in dictionary. Check yourself!");
    }
}
