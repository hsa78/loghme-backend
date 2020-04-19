package ie.services;

import ie.logic.DiscountFood;
import ie.logic.Loghme;
import ie.logic.Resturant;
import ie.services.responses.FoodPartyRemainedTime;
import ie.services.responses.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FoodPartyService {

    @RequestMapping(value = "/foodParty/view", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<DiscountFood>> getFoodPartyData(){
        ArrayList<DiscountFood> foods = Loghme.getInstance().getDiscountFoods();
        return new ResponseEntity<ArrayList<DiscountFood>>(foods, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/foodParty/time", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodPartyRemainedTime> getFoodPartyRemainedTime() {
        long diff = Loghme.getInstance().getFoodPartyRemainingTime();
        diff = 5 * 60 - diff;
        int diffMinutes = (int) diff / 60;
        int diffSeconds = (int) diff % 60;
        return new ResponseEntity<FoodPartyRemainedTime>(new FoodPartyRemainedTime(diffSeconds, diffMinutes), HttpStatus.ACCEPTED);
    }
}

