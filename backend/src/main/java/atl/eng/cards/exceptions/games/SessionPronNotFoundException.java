package atl.eng.cards.exceptions.games;

public class SessionPronNotFoundException extends RuntimeException{
    public SessionPronNotFoundException(Long sessionId){
        super("Session='" + sessionId + "' not found");
    }
}
