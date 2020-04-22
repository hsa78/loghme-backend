package ie.repository.managers;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import ie.repository.DAO.FoodDAO;
import ie.repository.DAO.RestaurantDAO;
import ie.repository.DataManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class RestaurantManager {
    private static RestaurantManager instance;
    private ComboPooledDataSource dataSource;


    private RestaurantManager(){
        this.dataSource = DataManager.getDataSource();
    }

    public static RestaurantManager getInstance() {
        if(instance == null){
            instance = new RestaurantManager();
        }
        return instance;
    }

    public void save(ArrayList<RestaurantDAO> restaurants, Boolean isForFoodParty){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement pStatLoc = connection.prepareStatement(
                    "insert ignore into Location (x, y) values (?, ?)"
            );
            PreparedStatement pStatRestaurant = connection.prepareStatement(
                    "insert ignore into Restaurant (id, name, logo, x, y) values (?, ?, ?, ?, ?)");
            for(RestaurantDAO restaurant: restaurants){
                pStatLoc.setInt(1, restaurant.getLocation().get("x"));
                pStatLoc.setInt(2, restaurant.getLocation().get("y"));
                pStatLoc.addBatch();

                pStatRestaurant.setString(1, restaurant.getId());
                pStatRestaurant.setNString(2, restaurant.getName());
                pStatRestaurant.setString(3, restaurant.getLogo());
                pStatRestaurant.setInt(4, restaurant.getLocation().get("x"));
                pStatRestaurant.setInt(5, restaurant.getLocation().get("y"));
                pStatRestaurant.addBatch();
                if(isForFoodParty)
                    FoodManager.getInstance().saveDiscountFoods(restaurant.getMenu(), restaurant.getId());
                else
                    FoodManager.getInstance().saveOrdinaryFoods(restaurant.getMenu(), restaurant.getId());
            }
            int[] locUpdateCount = pStatLoc.executeBatch();
            connection.commit();
            pStatLoc.close();

            int[] restaurantUpdateCount = pStatRestaurant.executeBatch();
            connection.commit();
            pStatRestaurant.close();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<RestaurantDAO> retrieve(int startIndex, int numOfItems){
        ArrayList<RestaurantDAO> restaurants = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from Restaurant r order by r.id limit ?,?"
            );
            queryStatement.setInt(1, startIndex);
            queryStatement.setInt(2, numOfItems);
            ResultSet result = queryStatement.executeQuery();
            while (result.next()){
                RestaurantDAO restaurant = new RestaurantDAO();
                HashMap<String, Integer> restaurantLoc = new HashMap<>();
                restaurant.setId(result.getString("id"));
                restaurant.setLogo(result.getString("logo"));
                restaurant.setName(result.getString("name"));
                restaurantLoc.put("x", result.getInt("x"));
                restaurantLoc.put("y", result.getInt("y"));
                restaurant.setLocation(restaurantLoc);
                restaurants.add(restaurant);
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public RestaurantDAO retrieveById(String restaurantId){
        RestaurantDAO restaurant = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement pStatRestaurant = connection.prepareStatement(
                    "select * from Restaurant r where r.id=?");
            pStatRestaurant.setString(1, restaurantId);
            ResultSet result = pStatRestaurant.executeQuery();
            if (result.next()){
                restaurant = new RestaurantDAO();
                HashMap<String, Integer> restaurantLoc = new HashMap<>();
                restaurant.setId(result.getString("id"));
                restaurant.setLogo(result.getString("logo"));
                restaurant.setName(result.getString("name"));
                restaurantLoc.put("x", result.getInt("x"));
                restaurantLoc.put("y", result.getInt("y"));
                restaurant.setLocation(restaurantLoc);
            }
            result.close();
            pStatRestaurant.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurant;
    }

    public ArrayList<RestaurantDAO> searchByName(String restaurantName, int startIndex, int numOfItems){
        ArrayList<RestaurantDAO> restaurants = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from Restaurant r where r.name = ? order by r.id limit ?,?"
            );
            queryStatement.setNString(1, restaurantName);
            queryStatement.setInt(2, startIndex);
            queryStatement.setInt(3, numOfItems);
            ResultSet result = queryStatement.executeQuery();
            while (result.next()){
                RestaurantDAO restaurant = new RestaurantDAO();
                HashMap<String, Integer> restaurantLoc = new HashMap<>();
                restaurant.setId(result.getString("id"));
                restaurant.setLogo(result.getString("logo"));
                restaurant.setName(result.getString("name"));
                restaurantLoc.put("x", result.getInt("x"));
                restaurantLoc.put("y", result.getInt("y"));
                restaurant.setLocation(restaurantLoc);
                restaurants.add(restaurant);
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public ArrayList<RestaurantDAO> searchByFoodName(String foodName, int startIndex, int numOfItems){
        ArrayList<RestaurantDAO> restaurants = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select distinct * from Restaurant r, Food f where f.restaurantId = r.id and f.name = ? " +
                            "order by r.id limit ?,?"
            );
            queryStatement.setNString(1, foodName);
            queryStatement.setInt(2, startIndex);
            queryStatement.setInt(3, numOfItems);
            ResultSet result = queryStatement.executeQuery();
            while (result.next()){
                RestaurantDAO restaurant = new RestaurantDAO();
                HashMap<String, Integer> restaurantLoc = new HashMap<>();
                restaurant.setId(result.getString("id"));
                restaurant.setLogo(result.getString("logo"));
                restaurant.setName(result.getString("name"));
                restaurantLoc.put("x", result.getInt("x"));
                restaurantLoc.put("y", result.getInt("y"));
                restaurant.setLocation(restaurantLoc);
                restaurants.add(restaurant);
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public ArrayList<RestaurantDAO> searchByFoodAndRestaurantName(String restaurantName, String foodName,
                                                                  int startIndex, int numOfItems){
        ArrayList<RestaurantDAO> restaurants = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select distinct * from Restaurant r, Food f where f.restaurantId = r.id and r.name = ? and f.name = ?" +
                            " order by r.id limit ?,?"
            );
            queryStatement.setNString(1, restaurantName);
            queryStatement.setNString(2, foodName);
            queryStatement.setInt(3, startIndex);
            queryStatement.setInt(4, numOfItems);
            ResultSet result = queryStatement.executeQuery();
            while (result.next()){
                RestaurantDAO restaurant = new RestaurantDAO();
                HashMap<String, Integer> restaurantLoc = new HashMap<>();
                restaurant.setId(result.getString("id"));
                restaurant.setLogo(result.getString("logo"));
                restaurant.setName(result.getString("name"));
                restaurantLoc.put("x", result.getInt("x"));
                restaurantLoc.put("y", result.getInt("y"));
                restaurant.setLocation(restaurantLoc);
                restaurants.add(restaurant);
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }
}
