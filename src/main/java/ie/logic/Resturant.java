package ie.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import ie.logic.DiscountFood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Resturant {
    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private HashMap<String, Integer> location = new HashMap<String, Integer>();
    @JsonProperty(required = true)
    private List<Food> menu = new ArrayList<Food>();
    @JsonProperty(required = true)
    private String logo;

    private float popularity;
    private float averageFoodPopularity;
    private float distanceFromUser;

    static int NOT_FOUND = -1;

    public String getName() {
        return name;
    }


    public List<Food> getMenu() {
        return menu;
    }

    public HashMap<String, Integer> getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public String getLogo() {
        return logo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setLocation(HashMap<String, Integer> location) {

        this.location = location;
        distanceFromUser = (float)(Math.sqrt(Math.pow(location.get("x"),2) + Math.pow((location.get("y")), 2)));
    }

    public void setMenu(List<Food> menu) {
        this.menu = menu;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int findFoodIndex(String foodName){
        for(int i = 0; i < menu.size(); i++){
            if(menu.get(i).getName().equals(foodName))
                return i;
        }
        return NOT_FOUND;
    }

    public Loghme.Status addFood(Food newFood){
        if(findFoodIndex(newFood.getName()) != NOT_FOUND){
            System.out.println("Food with name " + newFood.getName() + " in restaurant " + name + " already exist.");
            return Loghme.Status.BAD_REQUEST;
        }
        menu.add(newFood);
        return Loghme.Status.OK;
    }

    public Food getFood(String foodName){
        int foodIndex = findFoodIndex(foodName);
        if(foodIndex == NOT_FOUND)
            return null;
        return menu.get(foodIndex);

    }

    public void setRestaurantIdForFoods(){
        for (Food menu1 : menu) {
            if (menu1.getRestaurantId() == null)
                menu1.setRestaurantId(id);
        }
    }

    public List<Food> getOrdinaryMenu() {
        ArrayList<Food> ordinaryMenu = new ArrayList<Food>();
        for(Food food: menu){
            if(!food.hasDiscount())
                ordinaryMenu.add(food);
        }
        return ordinaryMenu;
    }

    public List<DiscountFood> getDiscountMenu(){//TODO check with alternative method
        ArrayList<DiscountFood> discountMenu = new ArrayList<DiscountFood>();
        for(Food food: menu){
            if(food.hasDiscount() && food.isAvailable(1))
                discountMenu.add((DiscountFood) food);
        }
        return discountMenu;
    }

    public Loghme.Status removeDiscount() {//TODO also check this
        Iterator<Food> iter = menu.iterator();
        while (iter.hasNext()) {
            Food food = iter.next();
            if (food.hasDiscount()) {
                ((DiscountFood) food).setCount(0);
                iter.remove();
            }
        }
        return Loghme.Status.OK;
    }

    public void addMenu(List<Food> newMenu){
        menu.addAll(newMenu);
    }

    public Food getDiscountFood(String foodName) {
        List<DiscountFood> discountFoods = getDiscountMenu();
        for(Food food: discountFoods){
            if(food.getName().equals(foodName))
                return food;
        }
        return null;
    }

    public Food discountFoodAvailable(String foodName, int count){
        List<DiscountFood> discountFoods = getDiscountMenu();
        for(DiscountFood food: discountFoods){
            if(food.getName().equals(foodName) && food.isAvailable(count))
                return food;
        }
        return null;
    }

    public Food getOrdinaryFood(String foodName) {
        List<Food> foods = getOrdinaryMenu();
        for(Food food: foods){
            if(! food.hasDiscount()){
                if(food.getName().equals(foodName))
                    return food;
            }
        }
        return null;
    }

    public Food hasFood(String foodName){
        List<Food> foods = getOrdinaryMenu();
        for(Food food: foods){
            if(! food.hasDiscount()){
                if(food.getName().equals(foodName))
                    return food;
            }
        }
        List<DiscountFood> discountFoods = getDiscountMenu();
        for(Food food: discountFoods){
            if(food.getName().equals(foodName))
                return food;
        }
        return null;
    }
}