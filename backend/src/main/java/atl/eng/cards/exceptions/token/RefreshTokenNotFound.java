package atl.eng.cards.exceptions.token;

public class RefreshTokenNotFound extends RuntimeException{
    public RefreshTokenNotFound(Long userId){
        super("Token for user with id '" + userId + "' didn't find");
    }

    public RefreshTokenNotFound(String token){
        super("Token '" + token + "' didn't find");
    }
}
