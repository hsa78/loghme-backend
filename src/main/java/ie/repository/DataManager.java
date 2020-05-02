package ie.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import ie.logic.*;
import ie.repository.DAO.*;
import ie.repository.managers.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DataManager {

    private static DataManager instance;

    static public ObjectMapper mapper;
    private static ComboPooledDataSource dataSource;
    private Date foodPartyStartTime;

    public static DataManager getInstance(){

        if(instance == null){
            instance = new DataManager();
        }
//        System.out.println("time:");
//        System.out.println(instance.getFoodPartyRemainedTime());
        return instance;
    }

    public Date getFoodPartyStartTime() {
        return foodPartyStartTime;
    }

    private DataManager(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/loghme");
        dataSource.setUser("root");
        dataSource.setPassword("hena1378");
//        dataSource.setPassword("MFfm3722119@");

        dataSource.setInitialPoolSize(5);
        dataSource.setMinPoolSize(5);
        dataSource.setAcquireIncrement(5);
        dataSource.setMaxPoolSize(20);
        dataSource.setMaxStatements(100);

        mapper = new ObjectMapper();
        foodPartyStartTime = new Date();
    }

    public static ComboPooledDataSource getDataSource() {
        return dataSource;
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
        ArrayList<RestaurantDAO> newRestaurants = new ArrayList<RestaurantDAO>();
        try {
            newRestaurants.addAll(new ArrayList<RestaurantDAO>(Arrays.asList(mapper.readValue(restaurantsJson, RestaurantDAO[].class))));
        } catch (Exception  e) {
            System.out.println("Exception in set list of restaurants");
        }

        RestaurantManager.getInstance().save(newRestaurants, false);
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
            ArrayList<DeliveryDAO> deliveries;
            deliveries = new ArrayList<DeliveryDAO>(Arrays.asList(mapper.readValue(deliveriesJson, DeliveryDAO[].class)));
            DeliveryManager.getInstance().save(deliveries);
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
        try {
            System.out.println("start food party");
            FoodManager.getInstance().deleteDiscountFoods();
            Loghme.getInstance().checkCarts();
            ArrayList<RestaurantDAO> discountRestaurants;
            discountRestaurants = new ArrayList<RestaurantDAO>(Arrays.asList(mapper.readValue(foodPartyJson, RestaurantDAO[].class)));
            RestaurantManager.getInstance().save(discountRestaurants, true);
        } catch (Exception  e) {
            System.out.println("Exception in start foodParty");
        }
        foodPartyStartTime = new Date();
    }



    public long getFoodPartyRemainedTime() {
        Date currentTime = new Date();
//        System.out.println("in get time");
//        System.out.println(foodPartyStartTime);
        long diffInMilliSec = currentTime.getTime() - foodPartyStartTime.getTime();
        TimeUnit timeUnit = TimeUnit.SECONDS;
        return timeUnit.convert(diffInMilliSec, TimeUnit.MILLISECONDS);
    }
}
