package atl.eng.cards.exceptions.cards;


public class CardAlreadyExistsException extends RuntimeException{
    public CardAlreadyExistsException(String word){
        super("Word '"+word+"' already exists");
    }
}
