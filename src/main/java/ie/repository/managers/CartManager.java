package ie.repository.managers;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import ie.repository.DAO.CartDAO;
import ie.repository.DAO.FoodDAO;
import ie.repository.DataManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartManager {
    private static CartManager instance;
    private ComboPooledDataSource dataSource;


    private CartManager(){
        this.dataSource = DataManager.getDataSource();
    }

    public static CartManager getInstance() {
        if(instance == null){
            instance = new CartManager();
        }
        return instance;
    }

    public void save(CartDAO cart){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(
                    "insert ignore into Cart (userEmail)" +
                            " values (?)"
            );
            pStatement.setString(1, cart.getUserEmail());
            pStatement.executeUpdate();
            pStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CartDAO retrieveCurrentCart(String userEmail){
        CartDAO cart = new CartDAO(userEmail);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from Cart c where c.userEmail = ? and c.status = 'OnProgress'"
            );
            queryStatement.setString(1, userEmail);
            ResultSet result = queryStatement.executeQuery();
            if(result.next()){
                cart.setRestaurantId(result.getString("restaurantId"));
                cart.setId(result.getInt("id"));
                cart.setRestaurantName(result.getNString("restaurantName"));
            }
            else{
                cart = null;
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    public CartDAO retrieveCartById(int cartID){
        CartDAO cart = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from Cart c where c.id = ?"
            );
            queryStatement.setInt(1, cartID);
            ResultSet result = queryStatement.executeQuery();
            if(result.next()){
                cart = new CartDAO(result.getString("userEmail"));
                cart.setRestaurantId(result.getString("restaurantId"));
                cart.setId(result.getInt("id"));
                cart.setRestaurantName(result.getNString("restaurantName"));
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    public ArrayList<CartDAO> retrieveCartHistory(String userEmail){
        ArrayList<CartDAO> carts = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from Cart c where c.userEmail = ? and c.status <> 'OnProgress'"
            );
            queryStatement.setString(1, userEmail);
            ResultSet result = queryStatement.executeQuery();
            while(result.next()){
                CartDAO cart = new CartDAO(userEmail);
                cart.setRestaurantId(result.getString("restaurantId"));
                cart.setId(result.getInt("id"));
                cart.setRestaurantName(result.getNString("restaurantName"));
                cart.setCartStatus(result.getString("status"));
                cart.setDeliveryId(result.getString("deliveryId"));
                carts.add(cart);
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carts;
    }

    public void updateStatus(int cartId, String newStatus){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "update Cart set status = ? where id = ?"
            );
            statement.setString(1, newStatus);
            statement.setInt(2, cartId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDeliveryId(int cartId, String deliveryId){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "update Cart set deliveryId = ? where id = ?"
            );
            statement.setString(1, deliveryId);
            statement.setInt(2, cartId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRestaurant(int cartId, String restaurantName, String restaurantId){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "update Cart set restaurantName = ? , restaurantId = ? where id = ?"
            );
            statement.setNString(1, restaurantName);
            statement.setString(2, restaurantId);
            statement.setInt(3, cartId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CartDAO> retrieveCartsByStatus(String status) {
        ArrayList<CartDAO> carts = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from Cart c where c.status = ?"
            );
            queryStatement.setString(1, status);
            ResultSet result = queryStatement.executeQuery();
            while(result.next()){
                CartDAO cart = new CartDAO();
                cart.setRestaurantId(result.getString("restaurantId"));
                cart.setId(result.getInt("id"));
                cart.setRestaurantName(result.getNString("restaurantName"));
                cart.setCartStatus(result.getString("status"));
                cart.setDeliveryId(result.getString("deliveryId"));
                cart.setUserEmail(result.getString("userEmail"));
                carts.add(cart);
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  carts;
    }
}
