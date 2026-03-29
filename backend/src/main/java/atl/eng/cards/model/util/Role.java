package atl.eng.cards.model.util;

public enum Role {
    ADMIN(), USER();

    public String getName(){
        return "ROLE_" + toString();
    }
}
