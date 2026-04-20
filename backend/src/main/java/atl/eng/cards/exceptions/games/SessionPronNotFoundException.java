package atl.eng.cards.exceptions.games;

public class SessionPronNotFoundException extends RuntimeException{

    public SessionPronNotFoundException(Long sessionId, Long credentialId){
        super("Session id='" + sessionId + "' not found (user id= '" + credentialId + "')");
    }
}
