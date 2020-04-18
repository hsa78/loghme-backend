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

    public ArrayList<RestaurantDAO> retrieve(){
        ArrayList<RestaurantDAO> restaurants = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            Statement queryStatement = connection.createStatement();
            ResultSet result = queryStatement.executeQuery(
                    "select * from Restaurant"
            );
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

    public static void checkUpdateCounts(int[] updateCounts) {
        for (int i = 0; i < updateCounts.length; i++) {
            if (updateCounts[i] >= 0) {
                System.out.println("OK; updateCount=" + updateCounts[i]);
            } else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
                System.out.println("OK; updateCount=Statement.SUCCESS_NO_INFO");
            } else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                System.out.println("Failure; updateCount=Statement.EXECUTE_FAILED");
            }
        }
    }
}
