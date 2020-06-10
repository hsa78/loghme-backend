package ie.services;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import ie.logic.Loghme;
import ie.logic.Resturant;
import ie.services.responses.JwtResponse;
import ie.services.responses.RestaurantInfo;
import ie.services.responses.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import java.util.ArrayList;
import java.util.Collections;

@RestController
public class SignService {
    @RequestMapping(value = "/registration", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusCode> registerUser(@RequestParam(value = "firstName") String firstName,
                                                   @RequestParam(value = "lastName") String lastName,
                                                   @RequestParam(value = "email") String email,
                                                   @RequestParam(value = "phone") String phoneNumber,
                                                   @RequestParam(value = "password") String password)
    {
        Loghme.Status resultStatus = Loghme.getInstance().register(firstName, lastName, email, phoneNumber, password);
        return resultDecoder(resultStatus);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> loginUser(@RequestParam(value = "email") String email,
                                                 @RequestParam(value = "password") String password)
    {
        String jwtToken = Loghme.getInstance().login(email, password);
        if(jwtToken == null)
            return new ResponseEntity<JwtResponse>(new JwtResponse(jwtToken), HttpStatus.CONFLICT);
        else
            return new ResponseEntity<JwtResponse>(new JwtResponse(jwtToken), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/googleLogin", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> loginUserWithGoogle(@RequestParam(value = "email") String email,
                                                           @RequestParam(value="idToken") String idTokenString)
    {
        JwtResponse fakeResponse = null;
        try{
            System.out.println(1);
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList("822539019781-se58ntb5ml53t7es2odus494vkifdtso.apps.googleusercontent.com"))//CLIENT_ID
                    .build();
            System.out.println(2);
            GoogleIdToken idToken = verifier.verify(idTokenString);
            System.out.println(3);
            if(idToken != null){
                System.out.println(4);
                Payload payload = idToken.getPayload();
                String emailPayload = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String jwtToken = Loghme.getInstance().loginWithGoogle(emailPayload);
                System.out.println(5);
                if(jwtToken == null || !emailVerified){
                    System.out.println(6);
                    return new ResponseEntity<JwtResponse>(fakeResponse, HttpStatus.CONFLICT);
                }
                else {
                    System.out.println(7);
                    return new ResponseEntity<JwtResponse>(new JwtResponse(jwtToken), HttpStatus.ACCEPTED);
                }
            }
            System.out.println(8);
            return new ResponseEntity<JwtResponse>(fakeResponse, HttpStatus.CONFLICT);
        }catch (Exception e){
            System.out.println(9);
            System.out.println("Exception in googleLogin function");
            System.out.println(e);
            return new ResponseEntity<JwtResponse>(fakeResponse, HttpStatus.CONFLICT);
        }
    }

    private ResponseEntity<StatusCode> resultDecoder(Loghme.Status status){
        if(status.equals(Loghme.Status.OK))
            return new ResponseEntity<StatusCode>(new StatusCode(200), HttpStatus.ACCEPTED);
        else if(status.equals(Loghme.Status.NOT_FOUND))
            return new ResponseEntity<StatusCode>(new StatusCode(404), HttpStatus.NOT_FOUND);
        else if(status.equals(Loghme.Status.BAD_REQUEST))
            return new ResponseEntity<StatusCode>(new StatusCode(400), HttpStatus.BAD_REQUEST);
        else if(status.equals(Loghme.Status.ACCESS_DENIED))
            return new ResponseEntity<StatusCode>(new StatusCode(403), HttpStatus.FORBIDDEN);
        else if(status.equals(Loghme.Status.CONFLICT))
            return new ResponseEntity<StatusCode>(new StatusCode(409), HttpStatus.CONFLICT);
        return new ResponseEntity<StatusCode>(new StatusCode(500), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
