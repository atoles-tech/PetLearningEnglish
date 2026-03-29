package atl.eng.cards.exceptions.auth;


public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException(){
        super("Password is incorrect. Try again");
    }
}
