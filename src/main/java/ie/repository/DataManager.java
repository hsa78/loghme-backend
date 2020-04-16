package ie.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.logic.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class DataManager {

    private static DataManager instance;

    private static final int NOT_FOUND = -1;
    private ArrayList<Resturant> resturants;
    private ArrayList<Delivery> deliveries;
    private User loginnedUser;
    private FoodParty foodParty = null;
    static public ObjectMapper mapper;

    public static DataManager getInstance(){
        if(instance == null){
            instance = new DataManager();
        }
        return instance;
    }

    private DataManager(){
        resturants = new ArrayList<Resturant>();
        deliveries = new ArrayList<Delivery>();
        mapper = new ObjectMapper();
        loginnedUser = new User("Hosna","Azarmsa", "hsazarmsa@gmail.com", "09123456789");
    }

    public ArrayList<Resturant> getResturants() {
        return resturants;
    }

    public ArrayList<Delivery> getDeliveries() {
        return deliveries;
    }

    public User getLoginnedUser() {
        return loginnedUser;
    }

    public FoodParty getFoodParty() {
        return foodParty;
    }

    public Resturant findRestaurantById(String restaurantId){
        for(int i = 0; i < resturants.size(); i++){
            if(resturants.get(i).getId().equals(restaurantId))
                return resturants.get(i);
        }
        return null;
    }

    public String loadRestaurantsJson(){
        try {
            URL url = new URL("http://138.197.181.131:8080/restaurants");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                content.append(inputLine);
            in.close();
            con.disconnect();
            return content.toString();
        }catch (Exception e){
            System.out.println("Exception in loadRestaurantJson");
        }
        return "";
    }

    public void setListOfRestaurants(){
        String restaurantsJson = loadRestaurantsJson();
        ArrayList<Resturant> newRestaurants = new ArrayList<Resturant>();
        try {
            newRestaurants.addAll(new ArrayList<ie.logic.Resturant>(Arrays.asList(mapper.readValue(restaurantsJson, Resturant[].class))));
        } catch (Exception  e) {
            System.out.println("Exception in set list of restaurants");
        }
        for(Resturant resturant: newRestaurants){
            if(findRestaurantById(resturant.getId()) == null)
                resturants.add(resturant);
        }
    }

    public String loadDeliveriesJson(){
        try {
            URL url = new URL("http://138.197.181.131:8080/deliveries");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                content.append(inputLine);
            in.close();
            con.disconnect();
            return content.toString();
        }catch(Exception e) {
            System.out.println("Exception in adding deliveriesJson");
            return "";
        }
    }

    public void setListOfDeliveries(){
        String deliveriesJson = loadDeliveriesJson();
        try {
            System.out.println(deliveriesJson);
            deliveries = new ArrayList<Delivery>(Arrays.asList(mapper.readValue(deliveriesJson, Delivery[].class)));
        } catch (Exception  e) {
            System.out.println("Exception in addDeliveries");
        }
    }

    public String loadFoodPartyJson(){
        try {
            URL url = new URL("http://138.197.181.131:8080/foodparty");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                content.append(inputLine);
            in.close();
            con.disconnect();
            return content.toString();
        }catch (Exception e){
            System.out.println("Exception in loadFoodPartyJson");
            return "";
        }
    }

    public void startFoodParty(){
        String foodPartyJson = loadFoodPartyJson();
        foodParty = new FoodParty();
        try {
            System.out.println("start food party");
            foodParty.removeDiscount();
            ArrayList<Resturant> discountRestaurants;
            ArrayList<Resturant> foodPartyRestaurants = new ArrayList<Resturant>();
            discountRestaurants = new ArrayList<Resturant>(Arrays.asList(mapper.readValue(foodPartyJson, Resturant[].class)));
            for(Resturant resturant: discountRestaurants) {
                Resturant foundRestaurant = findRestaurantById(resturant.getId());
                if (foundRestaurant == null){
                    resturants.add(resturant);
                    System.out.println("restaurant added with id " + resturant.getId());
                    foodPartyRestaurants.add(resturant);
                    resturant.setRestaurantIdForFoods();
                }
                else{
                    foundRestaurant.addMenu(resturant.getMenu());
                    foodPartyRestaurants.add(foundRestaurant);
                    foundRestaurant.setRestaurantIdForFoods();
                }
            }
            foodParty.setDiscountedRestaurants(foodPartyRestaurants);
        } catch (Exception  e) {
            System.out.println("Exception in start foodParty");
        }
        foodParty.setStartTime(new Date());
    }

    public HashMap<String, Integer> getRestaurantLocation(String restaurantId){
        return findRestaurantById(restaurantId).getLocation();
    }

    public Food findFood(String foodName, String restaurantId, boolean isFoodParty){
        Resturant resturant = findRestaurantById(restaurantId);
        Food food = null;
        if(isFoodParty){
            food = resturant.getDiscountFood(foodName);
            return food;
        }else{
            food = resturant.getOrdinaryFood(foodName);
            if(food == null)
                System.out.println("Food with name " + foodName + "in restaurant with id " + restaurantId +" does not exist.");
            return food;
        }
    }

    public String getRestaurantName(String restaurantId){
        return findRestaurantById(restaurantId).getName();
    }

    public Cart getUserCurrentCart(String userId){
        return loginnedUser.getCart();
    }
}
