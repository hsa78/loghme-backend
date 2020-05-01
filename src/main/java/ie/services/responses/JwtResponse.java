package ie.services.responses;

import ie.logic.Loghme;

public class JwtResponse {
    private final String jwtToken;

    public JwtResponse(String jwttoken) {
        this.jwtToken = jwttoken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
