package ie.services;

import ie.logic.Loghme;
import ie.logic.Resturant;
import ie.services.responses.RestaurantInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class RestaurantService {

    @RequestMapping(value = "/restaurant/brief/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantInfo> getRestaurantSummaryInfo(@PathVariable(value = "id") String id){
        int restaurantIndex = Loghme.getInstance().findRestaurantIndex(id);
        Resturant resturant = Loghme.getInstance().getRestaurantByIndex(restaurantIndex);
        if(resturant == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        RestaurantInfo response = new RestaurantInfo(resturant);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/restaurant/detail/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resturant> getRestaurantCompleteInfo(@PathVariable(value = "id") String id){
        int restaurantIndex = Loghme.getInstance().findRestaurantIndex(id);
        Resturant resturant = Loghme.getInstance().getRestaurantByIndex(restaurantIndex);
        if(resturant == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(resturant, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/restaurant/all", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<RestaurantInfo>> getAllRestaurantsInfo(){
        ArrayList<Resturant> allRestaurants = Loghme.getInstance().getNearResturants();
        ArrayList<RestaurantInfo> allRestaurantsInfo = new ArrayList<RestaurantInfo>();
        for(Resturant resturant: allRestaurants)
            allRestaurantsInfo.add(new RestaurantInfo(resturant));
        return new ResponseEntity<ArrayList<RestaurantInfo>>(allRestaurantsInfo, HttpStatus.ACCEPTED);
    }

}
