package ie.logic;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static ie.logic.Loghme.NOT_FOUND;

public class FoodParty {
    private ArrayList<Resturant> discountedRestaurants = new ArrayList<Resturant>();
    private Date startTime;

    public void setDiscountedRestaurants(ArrayList<Resturant> discountedRestaurants) {
        this.discountedRestaurants = discountedRestaurants;
    }

    public void setStartTime(Date startTime){
        this.startTime = startTime;
    }

    public ArrayList<Resturant> getDiscountedRestaurants() {
        return discountedRestaurants;
    }

    public Loghme.Status removeDiscount(){
        for(Resturant resturant: discountedRestaurants){
            Loghme.Status status = resturant.removeDiscount();
            if(status != Loghme.Status.OK)
                return status;
        }
        return Loghme.Status.OK;
    }

    public long getRemainedTime(Date currentTime){
        long diffInMilliSec = currentTime.getTime() - startTime.getTime();
        TimeUnit timeUnit = TimeUnit.SECONDS;
        return timeUnit.convert(diffInMilliSec, TimeUnit.MILLISECONDS);
    }
}