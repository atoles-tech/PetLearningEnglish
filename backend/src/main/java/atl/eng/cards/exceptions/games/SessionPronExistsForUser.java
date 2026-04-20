package atl.eng.cards.exceptions.games;

public class SessionPronExistsForUser extends RuntimeException {
    public SessionPronExistsForUser(Long id) {
        super("Session pronunciation game exists for user id=" + id);
    }
}
