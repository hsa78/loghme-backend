package ie.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.logic.Cart;
import ie.logic.Delivery;
import ie.repository.DataManager;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.*;

public class Loghme {
    private static Loghme instance;

    private static final float STANDARD_DISTANCE = 170;
    private static final int NUM_OF_POPULAR_RESTAURANTS = 3;
    public static final int NOT_FOUND = -1;
    public static enum Status {INTERNAL_ERROR, NOT_FOUND, ACCESS_DENIED,OK, BAD_REQUEST, CONFLICT};

    private ArrayList<Resturant> resturants;
    private ArrayList<Delivery> deliveries;
    private Resturant[] mostPopularRestaurants;
    private User loginnedUser;
    private FoodParty foodParty = null;
    static public ObjectMapper mapper;

    public static Loghme getInstance(){
        if(instance == null){
            instance = new Loghme();
        }
        return instance;
    }

    private Loghme(){
        resturants = new ArrayList<Resturant>();
        deliveries = new ArrayList<Delivery>();
        mapper = new ObjectMapper();
        loginnedUser = new User("Hosna","Azarmsa", "hsazarmsa@gmail.com", "09123456789");
    }

    public ArrayList<Resturant> getNearResturants() {
        ArrayList<Resturant> nearRestaurants = new ArrayList<Resturant>();
        ArrayList<Resturant> allRestaurants = DataManager.getInstance().getResturants();
        HashMap<String, Integer> userLoc = DataManager.getInstance().getLoginnedUser().getLocation();
        HashMap<String, Integer> restaurantLoc;
        for(Resturant resturant: allRestaurants){
            restaurantLoc = resturant.getLocation();
            if(isNear(userLoc,restaurantLoc))
                nearRestaurants.add(resturant);
        }
        return nearRestaurants;
    }

    private boolean isNear(HashMap<String, Integer> userLoc, HashMap<String, Integer> restaurantLoc) {
        float distance = (float) Math.sqrt(Math.pow(restaurantLoc.get("x") - userLoc.get("x"), 2) +
                                           Math.pow(restaurantLoc.get("y") - userLoc.get("y"), 2));
        return distance < STANDARD_DISTANCE;
    }

    public Map<String,String> convertJsonToMap(String jsonString){
        try {
            return mapper.readValue(jsonString, new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            return null;
        }
    }

    public boolean validMap(Map<String,String> map){
        if(map == null){
            System.out.println("Map in invalid. There was a problem in converting json to map.");
            return false;
        }
        return true;
    }

    public boolean validFields(Map<String,String> map, String[] fields){
        if(map.size() != fields.length){
            System.out.println("Number of field does not match to expected. expected:" + fields.length + "  but was:" + map.size());
            return false;
        }
        for(int i = 0; i < fields.length; i++){
            if(map.get(fields[i]) == null){
                System.out.println("Field with name " + fields[i] + " is missing.");
                return false;
            }
        }
        return true;
    }

    public int findRestaurantIndex(String restaurantId){
        ArrayList<Resturant> allRestaurants = DataManager.getInstance().getResturants();
        for(int i = 0; i < allRestaurants.size(); i++){
            if(allRestaurants.get(i).getId().equals(restaurantId))
                return i;
        }
        return NOT_FOUND;
    }

    public Resturant getRestaurantByIndex(int index){
        ArrayList<Resturant> allRestaurants = DataManager.getInstance().getResturants();
        if(index == NOT_FOUND)
            return null;
        if(index < allRestaurants.size())
            return allRestaurants.get(index);
        return null;
    }

//    public int findProperIndexToAdd(float newPopularity){
//        ArrayList<Resturant> allRestaurants = DataManager.getInstance().getResturants();
//        for(int i = 0; i < allRestaurants.size(); i++){
//            if(allRestaurants.get(i).getPopularity() < newPopularity)
//                return i;
//        }
//        return allRestaurants.size();
//    }

//    public void updateRestaurantList(int restaurantIndex){
//        Resturant updatedRestaurant = resturants.get(restaurantIndex);
//        float newPopularity = updatedRestaurant.getPopularity();
//        resturants.remove(restaurantIndex);
//        int newIndex = findProperIndexToAdd(newPopularity);
//        resturants.add(newIndex, updatedRestaurant);
//    }

    public Status addToCart(String foodInfo, boolean isFoodParty, int count){
        Map<String,String> foodInfoMap = convertJsonToMap(foodInfo);
        if(!validMap(foodInfoMap) || !validFields(foodInfoMap, new String[]{"restaurantId", "foodName"}))
            return Status.INTERNAL_ERROR;
        if(count < 1)
            return Status.BAD_REQUEST;
        Food order = DataManager.getInstance().findFood(foodInfoMap.get("foodName"), foodInfoMap.get("restaurantId"), isFoodParty);
        if(order == null)
            return Status.NOT_FOUND;
        HashMap<String, Integer> userLoc = DataManager.getInstance().getLoginnedUser().getLocation();
        if(! isNear(userLoc, DataManager.getInstance().getRestaurantLocation(foodInfoMap.get("restaurantId"))))
            return Status.BAD_REQUEST;
        if(! order.isAvailable(count))
            return Status.BAD_REQUEST;
        return DataManager.getInstance().getUserCurrentCart("").addOrder(order, count);
    }

    public Status deleteFromCart(String foodName, String restaurantId){
        Food order = DataManager.getInstance().findFoodAllType(foodName, restaurantId);
        if(order == null){
            System.out.println("Food with name " + foodName + "in restaurant with id " + restaurantId + " does not exist.");
            return Status.NOT_FOUND;
        }
        return DataManager.getInstance().getUserCurrentCart("").deleteFromCart(restaurantId, order);
    }

    public Status finalizeOrder(){
        return loginnedUser.finalizeOrder();
    }

//    public ArrayList<String> getMostPopularRestaurants(){
//        ArrayList<String> mostPopularRestaurants = new ArrayList<String>();
//        for(int i = 0; i < Math.min(NUM_OF_POPULAR_RESTAURANTS, resturants.size()); i++)
//            mostPopularRestaurants.add(resturants.get(i).getName());
//        return mostPopularRestaurants;
//    }
//
//    public Status getRecommendedRestaurants(){
//        ArrayList<String> mostPopularRestaurants = getMostPopularRestaurants();
//        for(int i = 0; i < mostPopularRestaurants.size(); i++){
//            System.out.println((i + 1) + "-" + mostPopularRestaurants.get(i));
//        }
//        return Status.OK;
//    }

    public Status assignDeliveriesToCart(Cart cart){
        float distance, timeToDelivery = Float.POSITIVE_INFINITY;
        HashMap<String, Integer> restaurantLoc, deliveryLoc, userLoc;
        Delivery assignedDelivery = null;
        Resturant resturant = getRestaurantByIndex(findRestaurantIndex(cart.getRestaurantId()));
        restaurantLoc = resturant.getLocation();
        userLoc = loginnedUser.getLocation();
        for(Delivery delivery: deliveries){
            deliveryLoc = delivery.getLocation();
            distance = (float) (Math.sqrt(Math.pow((userLoc.get("x") - restaurantLoc.get("x")) , 2) +
                                          Math.pow((userLoc.get("y") - restaurantLoc.get("y")) , 2)) +
                                Math.sqrt(Math.pow((restaurantLoc.get("x") - deliveryLoc.get("x")) , 2) +
                                          Math.pow((restaurantLoc.get("y") - deliveryLoc.get("y")) , 2)));
            if(distance / delivery.getVelocity() < timeToDelivery)
            {
                timeToDelivery = distance / delivery.getVelocity();
                assignedDelivery = delivery;
            }
        }

        if(assignedDelivery == null || deliveries.size() == 0)
            return Status.NOT_FOUND;

        assignedDelivery.setTimeToDest(timeToDelivery);
        loginnedUser.assignDeliveryToCart(assignedDelivery, cart);
        return Status.OK;
    }

    public Status loadDeliveries(){
        DataManager.getInstance().setListOfDeliveries();
        return Status.OK;
    }

    public Cart getLoginnedUserCart() {
        return loginnedUser.getCart();
    }

    public User getLoginnedUser() {
        return loginnedUser;
    }

    public Status increaseCredit(long plusCredit) {
        return loginnedUser.increaseCredit(plusCredit);
    }

    public ArrayList<Resturant> getDiscountRestaurants(){
        return foodParty.getDiscountedRestaurants();
    }

    public ArrayList<Cart> getLoginnedUserCartHistory() { return loginnedUser.getCartsHistory(); }

    public Cart getUserPastCartById(int id){
        return loginnedUser.findCartById(id);
    }

    public long getFoodPartyRemainingTime(){
        return foodParty.getRemainedTime(new Date());
    }

}
