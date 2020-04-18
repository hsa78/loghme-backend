package ie.repository.managers;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import ie.repository.DAO.FoodDAO;
import ie.repository.DAO.RestaurantDAO;
import ie.repository.DataManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoodManager {
    private static FoodManager instance;
    private ComboPooledDataSource dataSource;


    private FoodManager(){
        this.dataSource = DataManager.getDataSource();
    }

    public static FoodManager getInstance() {
        if(instance == null){
            instance = new FoodManager();
        }
        return instance;
    }

    public void saveOrdinaryFoods(ArrayList<FoodDAO> foods, String restaurantId){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement pStatFood = connection.prepareStatement(
                    "insert ignore into Food (name, restaurantId, image, price, description, popularity)" +
                       " values (?, ?, ?, ?, ?, ?)"
            );
            for(FoodDAO food: foods) {
                pStatFood.setNString(1, food.getName());
                pStatFood.setString(2, restaurantId);
                pStatFood.setString(3, food.getImage());
                pStatFood.setLong(4, food.getPrice());
                pStatFood.setNString(5, food.getDescription());
                pStatFood.setFloat(6, food.getPopularity());
                pStatFood.addBatch();
            }
            pStatFood.executeBatch();
            connection.commit();
            pStatFood.close();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveDiscountFoods(ArrayList<FoodDAO> foods, String restaurantId) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement pStatFood = connection.prepareStatement(
                    "insert ignore into Food (name, restaurantId, image, price, description, popularity, type, oldPrice, count)" +
                            " values (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            for(FoodDAO food: foods) {
                pStatFood.setNString(1, food.getName());
                pStatFood.setString(2, restaurantId);
                pStatFood.setString(3, food.getImage());
                pStatFood.setLong(4, food.getPrice());
                pStatFood.setNString(5, food.getDescription());
                pStatFood.setFloat(6, food.getPopularity());
                pStatFood.setString(7, "discount");
                pStatFood.setLong(8, food.getOldPrice());
                pStatFood.setInt(9, food.getCount());
                pStatFood.addBatch();
            }
            pStatFood.executeBatch();
            connection.commit();
            pStatFood.close();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<FoodDAO> retrieveMenu(String restaurantId, String type){
        ArrayList<FoodDAO> menu = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from Food f where f.type = ? and f.restaurantId = ?"
            );
            queryStatement.setString(1, type);
            queryStatement.setString(2, restaurantId);
            ResultSet result = queryStatement.executeQuery();
            while (result.next()){
                FoodDAO food = new FoodDAO();
                food.setDescription(result.getString("description"));
                food.setImage(result.getString("image"));
                food.setName(result.getString("name"));
                food.setPopularity(result.getFloat("popularity"));
                food.setPrice(result.getLong("price"));
                food.setRestaurantId(result.getString("restaurantId"));
                food.setType(result.getString("type"));
                if(food.getType().equals("discount")){
                    food.setOldPrice(result.getLong("oldPrice"));
                    food.setCount(result.getInt("count"));
                }
                menu.add(food);
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menu;
    }

    public FoodDAO retrieveFood(long foodId){
        FoodDAO food = new FoodDAO();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from Food f where f.id = ?"
            );
            queryStatement.setLong(1, foodId);
            ResultSet result = queryStatement.executeQuery();
            if(result.next()){
                food.setType(result.getString("type"));
                food.setRestaurantId(result.getString("restaurantId"));
                food.setPrice(result.getLong("price"));
                food.setPopularity(result.getFloat("popularity"));
                food.setName(result.getString("name"));
                food.setImage(result.getString("image"));
                food.setDescription(result.getString("description"));
                if(food.getType().equals("discount")){
                    food.setOldPrice(result.getLong("oldPrice"));
                    food.setCount(result.getInt("count"));
                }
            }
            else{
                food = null;
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return food;
    }

    public FoodDAO retrieveFood(String restaurantId, String foodName, String foodType){
        FoodDAO food = new FoodDAO();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from Food f where f.restaurantId = ? and f.name = ? and f.type = ?"
            );
            queryStatement.setString(1, restaurantId);
            queryStatement.setString(2, foodName);
            queryStatement.setString(3, foodType);
            ResultSet result = queryStatement.executeQuery();
            if(result.next()){
                food.setType(result.getString("type"));
                food.setRestaurantId(result.getString("restaurantId"));
                food.setPrice(result.getLong("price"));
                food.setPopularity(result.getFloat("popularity"));
                food.setName(result.getString("name"));
                food.setImage(result.getString("image"));
                food.setDescription(result.getString("description"));
                if(food.getType().equals("discount")){
                    food.setOldPrice(result.getLong("oldPrice"));
                    food.setCount(result.getInt("count"));
                }
            }
            else{
                food = null;
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return food;
    }

    public ArrayList<FoodDAO> retrieveDiscountFood(){
        ArrayList<FoodDAO> menu = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from Food f where f.type = 'discount'"
            );
            ResultSet result = queryStatement.executeQuery();
            while (result.next()){
                FoodDAO food = new FoodDAO();
                food.setDescription(result.getString("description"));
                food.setImage(result.getString("image"));
                food.setName(result.getString("name"));
                food.setPopularity(result.getFloat("popularity"));
                food.setPrice(result.getLong("price"));
                food.setRestaurantId(result.getString("restaurantId"));
                food.setType(result.getString("type"));
                food.setOldPrice(result.getLong("oldPrice"));
                food.setCount(result.getInt("count"));
                menu.add(food);
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menu;
    }

    public void deleteDiscountFoods(){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "delete from Food f where f.type = \'discount\'"
            );
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDiscountFoodCount(long foodId, int newCount){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "update Food set count = ? where id = ?"
            );
            statement.setInt(1, newCount);
            statement.setLong(2, foodId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFood(long foodId){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "delete from Food f where f.id = ?"
            );
            statement.setLong(1, foodId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
