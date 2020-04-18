package ie.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.logic.Cart;
import ie.logic.Delivery;
import ie.repository.DAO.FoodDAO;
import ie.repository.DAO.RestaurantDAO;
import ie.repository.DAO.UserDAO;
import ie.repository.DataManager;
import ie.repository.managers.FoodManager;
import ie.repository.managers.RestaurantManager;
import ie.repository.managers.UserManager;

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
    private Resturant[] mostPopularRestaurants;
    private User loginnedUser;
    static public ObjectMapper mapper;

    public static Loghme getInstance(){
        if(instance == null){
            instance = new Loghme();
        }
        return instance;
    }

    private Loghme(){
        resturants = new ArrayList<Resturant>();
        mapper = new ObjectMapper();
        loginnedUser = new User("Hosna","Azarmsa", "hsazarmsa@gmail.com", "09123456789");
    }

    public Resturant convertDAOToRestaurant(RestaurantDAO restaurantDAO){
        Resturant resturant = new Resturant();
        resturant.setName(restaurantDAO.getName());
        resturant.setId(restaurantDAO.getId());
        resturant.setLogo(restaurantDAO.getLogo());
        resturant.setLocation(restaurantDAO.getLocation());
        return resturant;
    }

    public User convertDAOToUser(UserDAO userDAO){
        User user = new User();
        user.setFirstName(userDAO.getFirstName());
        user.setLastName(userDAO.getLastName());
        user.setEmail(userDAO.getEmail());
        user.setPhoneNum(userDAO.getPhone());
        user.setLocation(userDAO.getLocation());
        user.setCredit(userDAO.getCredit());
        user.setPassword(userDAO.getPassword());
        return user;
    }

    public DiscountFood convertDAOTODiscountFood(FoodDAO foodDAO){
        DiscountFood food = new DiscountFood();
        food.setName(foodDAO.getName());
        food.setImage(foodDAO.getImage());
        food.setDescription(foodDAO.getDescription());
        food.setPopularity(foodDAO.getPopularity());
        food.setCount(foodDAO.getCount());
        food.setOldPrice(foodDAO.getOldPrice());
        food.setPrice(foodDAO.getPrice());
        food.setRestaurantId(foodDAO.getRestaurantId());
        return food;
    }

    public ArrayList<Resturant> getNearResturants() {
        ArrayList<Resturant> nearRestaurants = new ArrayList<Resturant>();
        ArrayList<RestaurantDAO> allRestaurants = RestaurantManager.getInstance().retrieve();
        HashMap<String, Integer> userLoc = UserManager.getInstance().retrieveLocation("hsazarmsa@gmail.com");
        HashMap<String, Integer> restaurantLoc;
        for(RestaurantDAO restaurant: allRestaurants){
            restaurantLoc = restaurant.getLocation();
            if(isNear(userLoc,restaurantLoc))
                nearRestaurants.add(convertDAOToRestaurant(restaurant));
        }
        return nearRestaurants;
    }

    private boolean isNear(HashMap<String, Integer> userLoc, HashMap<String, Integer> restaurantLoc) {
        System.out.println("X: " + restaurantLoc.get("x"));
        System.out.println("Y: " + restaurantLoc.get("y"));
        float distance = (float) Math.sqrt(Math.pow(restaurantLoc.get("x") - userLoc.get("x"), 2) +
                                           Math.pow(restaurantLoc.get("y") - userLoc.get("y"), 2));
        return distance < 50;
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

    public Resturant getRestaurantById(String restaurantId){
        RestaurantDAO restaurantDAO = RestaurantManager.getInstance().retrieveById(restaurantId);
        return convertDAOToRestaurant(restaurantDAO);
    }

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
        return DataManager.getInstance().getLoginnedUser().finalizeOrder();
    }

    public Status assignDeliveriesToCart(Cart cart){
        float distance, timeToDelivery = Float.POSITIVE_INFINITY;
        HashMap<String, Integer> restaurantLoc, deliveryLoc, userLoc;
        Delivery assignedDelivery = null;
        restaurantLoc = DataManager.getInstance().getRestaurantLocation(cart.getRestaurantId());
        User loggedUser = DataManager.getInstance().getLoginnedUser();
        userLoc = loggedUser.getLocation();
        ArrayList<Delivery> deliveries = DataManager.getInstance().getDeliveries();
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

        DataManager.getInstance().setTimeForDelivery(assignedDelivery, timeToDelivery);
        DataManager.setAssignedDelivery(loggedUser, cart, assignedDelivery);
        return Status.OK;
    }

    public Status loadDeliveries(){
        DataManager.getInstance().setListOfDeliveries();
        return Status.OK;
    }

    public Cart getLoginnedUserCart() {
        return DataManager.getInstance().getUserCurrentCart("");
    }

    public User getLoginnedUser() {
        UserDAO userDAO = UserManager.getInstance().retrieve("hsazarmsa@gmail.com");
        return convertDAOToUser(userDAO);
    }

    public Status increaseCredit(long plusCredit) {
        User user = getLoginnedUser();
        return user.increaseCredit(plusCredit);
    }

    public ArrayList<DiscountFood> getDiscountFoods(){
        ArrayList<DiscountFood> foods = new ArrayList<>();
        ArrayList<FoodDAO> foodDAOS = FoodManager.getInstance().retrieveDiscountFood();
        for(FoodDAO foodDAO: foodDAOS)
            foods.add(convertDAOTODiscountFood(foodDAO));
        return foods;
    }

    public ArrayList<Cart> getLoginnedUserCartHistory() { return DataManager.getInstance().getUserCartHistory(); }

    public Cart getUserPastCartById(int id){
        return DataManager.getInstance().getCart(id);
    }

    public long getFoodPartyRemainingTime(){
        return DataManager.getInstance().getFoodPartyRemainedTime();
    }

}
