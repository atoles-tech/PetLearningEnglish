package atl.eng.cards.exceptions.credential;

public class CredentialNotFoundException extends RuntimeException{
    public CredentialNotFoundException(Long id){
        super("Credential with id '" + id +"' not found");
    }

    public CredentialNotFoundException(String username){
        super("Credential with username '" + username +"' not found");
    }
}
