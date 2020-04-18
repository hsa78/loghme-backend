package ie.logic;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class OrdinaryFood extends Food {
    @Override
    public boolean hasDiscount(){ return false;}

    @Override
    public void setCount(int count) {
        return;
    }

    @Override
    public Loghme.Status decreaseCount(int numOfFoods){
        return Loghme.Status.OK;
    }

    @Override
    public boolean isAvailable(int numOfFoods) {
        return true;
    }
}
