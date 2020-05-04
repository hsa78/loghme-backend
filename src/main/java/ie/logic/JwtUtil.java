package ie.logic;

//import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import ie.repository.DAO.UserDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;

public class JwtUtil {

    private final String secret = "loghme";
    private static JwtUtil instance = null;

    private JwtUtil(){}

    public static JwtUtil getInstance(){
        if(instance == null)
            instance = new JwtUtil();
        return instance;
    }

    public UserDAO parseToken(String token) {
        if(token == null || token.equals(""))
            return null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            UserDAO u = new UserDAO();
            u.setEmail(body.getSubject());

            return u;

        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }

    public String generateToken(UserDAO u) {
        Claims claims = Jwts.claims().setSubject(u.getEmail());

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(dt)
                .setIssuedAt(new Date())
                .setIssuer("loghme.com")
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}