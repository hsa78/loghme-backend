package ie.logic;

import ie.logic.Loghme;
import ie.logic.Resturant;
import ie.logic.DiscountFood;

import java.util.ArrayList;
import java.util.List;

public class DiscountRestaurant extends Resturant {
    private List<DiscountFood> discountMenu = new ArrayList<DiscountFood>();

    public List<DiscountFood> getDiscountMenu() {
        return discountMenu;
    }

    public void setDiscountMenuMenu(List<DiscountFood> discountMenu) {
        this.discountMenu = discountMenu;
    }

    @Override
    public int findFoodIndex(String foodName) {
        for(int i = 0; i < discountMenu.size(); i++){
            if(discountMenu.get(i).getName().equals(foodName))
                return i;
        }
        return NOT_FOUND;
    }

    @Override
    public DiscountFood getFood(String foodName) {
        int foodIndex = findFoodIndex(foodName);
        if(foodIndex == NOT_FOUND)
            return null;
        else
            return discountMenu.get(foodIndex);
    }

    public Loghme.Status decreaseDiscountCount(String foodName){
        DiscountFood food = getFood(foodName);
        int foodIndex = findFoodIndex(foodName);
        if(food == null)
            return Loghme.Status.NOT_FOUND;
        if(food.getCount() <= 0)
            return Loghme.Status.BAD_REQUEST;
        else if(food.getCount() == 1){
            food = null;
            discountMenu.remove(foodIndex);
            return Loghme.Status.OK;
        }
        else{
            food.setCount(food.getCount() - 1);
            return Loghme.Status.OK;
        }
    }
}
