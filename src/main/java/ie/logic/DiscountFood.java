package ie.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ie.logic.Food;
import ie.logic.Loghme;
import ie.repository.DataManager;
import ie.repository.managers.FoodManager;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class DiscountFood extends Food {
    private long oldPrice;
    private int count;

    public long getOldPrice() {
        return oldPrice;
    }

    public int getCount() {
        return count;
    }

    public void setOldPrice(long oldPrice) {
        this.oldPrice = oldPrice;
    }


    @Override
    public Loghme.Status decreaseCount(int numOfFoods) {
        if(count < numOfFoods)
            return Loghme.Status.BAD_REQUEST;
        FoodManager.getInstance().updateDiscountFoodCount(id, count - numOfFoods);
        return Loghme.Status.OK;
    }

    @Override
    public boolean isAvailable(int numOfFoods) {
        return count >= numOfFoods;
    }

    @Override
    public boolean hasDiscount() {
        return true;
    }

    @Override
    public void setCount(int count){
        this.count = count;
    }
}