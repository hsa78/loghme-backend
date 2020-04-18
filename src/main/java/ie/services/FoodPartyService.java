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

    @RequestMapping(value = "/foodParty/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusCode> addToCartFromFoodParty(
            @RequestParam(value = "foodName") String foodName,
            @RequestParam(value = "restaurantId") String restaurantId,
            @RequestParam(value = "count") int count){
        Loghme.Status result = Loghme.getInstance().addToCart("{\"foodName\":\"" + foodName + "\", \"restaurantId\":\"" + restaurantId + "\"}", true, count);
        return resultDecoder(result);
    }

    @RequestMapping(value = "/foodParty/time", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodPartyRemainedTime> getFoodPartyRemainedTime(){
        long diff = Loghme.getInstance().getFoodPartyRemainingTime();
        diff = 5 * 60 - diff;
        int diffMinutes = (int) diff / 60;
        int diffSeconds = (int) diff % 60;
        return new ResponseEntity<FoodPartyRemainedTime>(new FoodPartyRemainedTime(diffSeconds, diffMinutes), HttpStatus.ACCEPTED);
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

