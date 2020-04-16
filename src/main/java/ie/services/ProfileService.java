package ie.services;

import ie.logic.Loghme;
import ie.services.responses.TestResponse;
import ie.services.responses.UserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ie.services.responses.CreditInfo;
import ie.services.responses.StatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileService {

    @RequestMapping(value = "/bea", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestResponse> testResponse(){
        return new ResponseEntity<TestResponse>(new TestResponse("Fatemeh"), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/user/info", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> getUserInfo() {
        return new ResponseEntity<UserInfo>(new UserInfo(Loghme.getInstance().getLoginnedUser()), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/user/credit", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditInfo> getUserCredit(){
        return new ResponseEntity<CreditInfo>(new CreditInfo(Loghme.getInstance().getLoginnedUser().getCredit()), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/user/credit", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusCode> increaseCredit(@RequestParam(value = "value") long value){
        Loghme.Status result = Loghme.getInstance().increaseCredit(value);
        if(result.equals(Loghme.Status.OK))
            return new ResponseEntity<StatusCode>(new StatusCode(200), HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<StatusCode>(new StatusCode(500), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


