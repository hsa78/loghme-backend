package ie.services;

import ie.logic.Loghme;
import ie.logic.Resturant;
import ie.services.responses.JwtResponse;
import ie.services.responses.RestaurantInfo;
import ie.services.responses.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
