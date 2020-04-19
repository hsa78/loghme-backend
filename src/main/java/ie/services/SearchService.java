package ie.services;

import ie.logic.Loghme;
import ie.logic.Resturant;
import ie.services.responses.RestaurantInfo;
import ie.services.responses.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class SearchService {

    @RequestMapping(value = "/search", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<RestaurantInfo>> search(@RequestParam(value = "foodName") String foodName,
                                                 @RequestParam(value = "restaurantName") String restaurantName){
        ArrayList<Resturant> foundRestaurants = Loghme.getInstance().searchRestaurants(restaurantName, foodName);
        ArrayList<RestaurantInfo> restaurantsInfo = new ArrayList<RestaurantInfo>();
        for(Resturant resturant: foundRestaurants)
            restaurantsInfo.add(new RestaurantInfo(resturant));
        return new ResponseEntity<ArrayList<RestaurantInfo>>(restaurantsInfo, HttpStatus.ACCEPTED);
    }

}
