package atl.eng.cards.exceptions.credential;

public class CredentialAlreadyExists extends RuntimeException{
    public CredentialAlreadyExists(String username){
        super("User with username '" + username + "' already exists");
    }
}
