package ie.services;

import ie.logic.Food;
import ie.logic.Loghme;
import ie.logic.Resturant;
import ie.services.responses.RestaurantInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RestaurantService {

    @RequestMapping(value = "/restaurant/brief/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantInfo> getRestaurantSummaryInfo(@PathVariable(value = "id") String id){
        Resturant resturant = Loghme.getInstance().getRestaurantById(id);
        if(resturant == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        RestaurantInfo response = new RestaurantInfo(resturant);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/restaurant/detail/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resturant> getRestaurantCompleteInfo(@PathVariable(value = "id") String id){
        Resturant resturant = Loghme.getInstance().getRestaurantById(id);
        if(resturant == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        ArrayList<Food> menu = resturant.getMenu();
        resturant.setMenu(menu);
        return new ResponseEntity<>(resturant, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/restaurant/all", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<RestaurantInfo>> getAllRestaurantsInfo(@RequestParam(value = "page") int pageNum,
                                                                           @RequestParam(value = "numOfItems") int numOfItems)
    {
        ArrayList<Resturant> allRestaurants = Loghme.getInstance().getNearResturants(pageNum, numOfItems);
        ArrayList<RestaurantInfo> allRestaurantsInfo = new ArrayList<RestaurantInfo>();
        for(Resturant resturant: allRestaurants)
            allRestaurantsInfo.add(new RestaurantInfo(resturant));
        return new ResponseEntity<ArrayList<RestaurantInfo>>(allRestaurantsInfo, HttpStatus.ACCEPTED);
    }

}
